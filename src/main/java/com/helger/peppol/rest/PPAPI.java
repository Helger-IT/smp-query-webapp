/*
 * Copyright (C) 2014-2024 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.peppol.rest;

import com.helger.peppol.api.rest.APIGetCheckPeppolParticipantRegistered;
import com.helger.peppol.api.rest.APIExceptionMapper;
import com.helger.peppol.api.rest.APISMPQueryGetBusinessCard;
import com.helger.peppol.api.rest.APISMPQueryGetDocTypes;
import com.helger.peppol.api.rest.APISMPQueryGetServiceInformation;
import com.helger.peppol.api.rest.PeppolSharedRestAPI;
import com.helger.photon.api.APIDescriptor;
import com.helger.photon.api.APIPath;
import com.helger.photon.api.IAPIExceptionMapper;
import com.helger.photon.api.IAPIRegistry;

import jakarta.annotation.Nonnull;

public final class PPAPI
{
  private static final String USER_AGENT = "Helger-IT-SMP-Query-WebApp/1.0 (https://github.com/Helger-IT/smp-query-webapp)";

  private PPAPI ()
  {}

  public static void init (@Nonnull final IAPIRegistry aAPIRegistry)
  {
    final IAPIExceptionMapper aExceptionMapper = new APIExceptionMapper ();

    // More specific to less specific
    {
      final APIDescriptor aSMPQueryEndpoints = new APIDescriptor (APIPath.get ("/smpquery/{" +
                                                                               PeppolSharedRestAPI.PARAM_SML_ID +
                                                                               "}/{" +
                                                                               PeppolSharedRestAPI.PARAM_PARTICIPANT_ID +
                                                                               "}/{" +
                                                                               PeppolSharedRestAPI.PARAM_DOCTYPE_ID +
                                                                               "}"),
                                                                  new APISMPQueryGetServiceInformation (USER_AGENT));
      aSMPQueryEndpoints.setExceptionMapper (aExceptionMapper);
      aAPIRegistry.registerAPI (aSMPQueryEndpoints);
    }

    {
      final APIDescriptor aSMPQueryDocTypes = new APIDescriptor (APIPath.get ("/smpquery/{" +
                                                                              PeppolSharedRestAPI.PARAM_SML_ID +
                                                                              "}/{" +
                                                                              PeppolSharedRestAPI.PARAM_PARTICIPANT_ID +
                                                                              "}"),
                                                                 new APISMPQueryGetDocTypes (USER_AGENT));
      aSMPQueryDocTypes.setExceptionMapper (aExceptionMapper);
      aAPIRegistry.registerAPI (aSMPQueryDocTypes);
    }

    {
      final APIDescriptor aSMPQueryBusinessCard = new APIDescriptor (APIPath.get ("/businesscard/{" +
                                                                                  PeppolSharedRestAPI.PARAM_SML_ID +
                                                                                  "}/{" +
                                                                                  PeppolSharedRestAPI.PARAM_PARTICIPANT_ID +
                                                                                  "}"),
                                                                     new APISMPQueryGetBusinessCard (USER_AGENT));
      aSMPQueryBusinessCard.setExceptionMapper (aExceptionMapper);
      aAPIRegistry.registerAPI (aSMPQueryBusinessCard);
    }

    {
      final APIDescriptor aSMPQueryBusinessCard = new APIDescriptor (APIPath.get ("/ppidexistence/{" +
                                                                                  PeppolSharedRestAPI.PARAM_SML_ID +
                                                                                  "}/{" +
                                                                                  PeppolSharedRestAPI.PARAM_PARTICIPANT_ID +
                                                                                  "}"),
                                                                     new APIGetCheckPeppolParticipantRegistered (USER_AGENT));
      aSMPQueryBusinessCard.setExceptionMapper (aExceptionMapper);
      aAPIRegistry.registerAPI (aSMPQueryBusinessCard);
    }
  }
}
