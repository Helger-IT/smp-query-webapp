package com.helger.peppol.rest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.Nonempty;
import com.helger.http.CHttp;
import com.helger.mime.CMimeType;
import com.helger.peppol.api.rest.APIParamException;
import com.helger.peppol.api.rest.PeppolSharedRestAPI;
import com.helger.peppol.ui.types.mgr.PhotonPeppolMetaManager;
import com.helger.peppol.ui.types.smlconfig.ISMLConfiguration;
import com.helger.peppol.ui.types.smlconfig.ISMLConfigurationManager;
import com.helger.peppol.ui.types.smp.SMPQueryParams;
import com.helger.peppolid.CIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.factory.PeppolIdentifierFactory;
import com.helger.peppolid.peppol.PeppolIdentifierHelper;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.photon.app.PhotonUnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import jakarta.annotation.Nonnull;

/**
 * Determine if a participant ID is registered in the Peppol Network or not.
 *
 * @author Philip Helger
 */
public final class APICheckPeppolParticipantRegistered extends AbstractAppAPIExecutor
{
  private static final Logger LOGGER = LoggerFactory.getLogger (APICheckPeppolParticipantRegistered.class);

  @Override
  public void invokeAPI (@Nonnull @Nonempty final String sLogPrefix,
                         @Nonnull final IAPIDescriptor aAPIDescriptor,
                         @Nonnull @Nonempty final String sPath,
                         @Nonnull final Map <String, String> aPathVariables,
                         @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                         @Nonnull final PhotonUnifiedResponse aUnifiedResponse) throws IOException
  {
    final ISMLConfigurationManager aSMLConfigurationMgr = PhotonPeppolMetaManager.getSMLConfigurationMgr ();
    final String sSMLID = aPathVariables.get (PeppolSharedRestAPI.PARAM_SML_ID);
    final boolean bSMLAutoDetect = ISMLConfigurationManager.ID_AUTO_DETECT.equals (sSMLID);
    final ISMLConfiguration aSML = aSMLConfigurationMgr.getSMLInfoOfID (sSMLID);
    if (aSML == null && !bSMLAutoDetect)
      throw new APIParamException ("Unsupported SML ID '" + sSMLID + "' provided.");

    String sPPID = aPathVariables.get (PeppolSharedRestAPI.PARAM_PARTICIPANT_ID);
    if (sPPID != null)
    {
      // Add prefix on demand
      if (!sPPID.startsWith (PeppolIdentifierHelper.PARTICIPANT_SCHEME_ISO6523_ACTORID_UPIS))
        sPPID = CIdentifier.getURIEncoded (PeppolIdentifierHelper.PARTICIPANT_SCHEME_ISO6523_ACTORID_UPIS, sPPID);
    }

    final IParticipantIdentifier aPPID = PeppolIdentifierFactory.INSTANCE.parseParticipantIdentifier (sPPID);
    if (aPPID == null)
    {
      LOGGER.error (sLogPrefix + "The provided Peppol Participant ID '" + sPPID + "' is invalid");
      aUnifiedResponse.createBadRequest ();
      return;
    }

    boolean bRegistered;
    if (bSMLAutoDetect)
    {
      bRegistered = false;
      for (final ISMLConfiguration aCurSML : aSMLConfigurationMgr.getAllSorted ())
      {
        bRegistered = SMPQueryParams.isSMPRegisteredInDNSViaNaptr (aPPID, aCurSML.getSMLInfo ().getDNSZone ());
        if (bRegistered)
          break;
      }
    }
    else
    {
      bRegistered = SMPQueryParams.isSMPRegisteredInDNSViaNaptr (aPPID, aSML.getSMLInfo ().getDNSZone ());
    }

    if (bRegistered)
    {
      LOGGER.info (sLogPrefix + "Peppol Participant ID '" + sPPID + "' is registered in DNS");
      aUnifiedResponse.setStatus (CHttp.HTTP_OK).disableCaching ();
    }
    else
    {
      LOGGER.warn (sLogPrefix + "Peppol Participant ID '" + sPPID + "' is NOT registered in DNS");
      aUnifiedResponse.setStatus (CHttp.HTTP_NOT_FOUND)
                      .setContentAndCharset ("Not found: '" + aPPID.getURIEncoded () + "'", StandardCharsets.UTF_8)
                      .setMimeType (CMimeType.TEXT_PLAIN)
                      .disableCaching ();
    }
  }
}
