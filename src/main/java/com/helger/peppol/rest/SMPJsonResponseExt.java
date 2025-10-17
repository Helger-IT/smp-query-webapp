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

import java.util.Map;

import com.helger.annotation.concurrent.Immutable;
import com.helger.json.IJsonArray;
import com.helger.json.IJsonObject;
import com.helger.peppol.photon.nicename.NiceNameEntry;
import com.helger.peppol.photon.nicename.NiceNameManager;
import com.helger.peppol.sml.ESMPAPIType;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.factory.IIdentifierFactory;
import com.helger.smpclient.json.SMPJsonResponse;

import jakarta.annotation.Nonnull;

@Immutable
public final class SMPJsonResponseExt
{
  private static final String JSON_NICE_NAME = "niceName";
  private static final String JSON_STATE = "state";
  private static final String JSON_IS_DEPRECATED = "isDeprecated";

  private SMPJsonResponseExt ()
  {}

  @Nonnull
  public static IJsonObject convert (@Nonnull final ESMPAPIType eSMPAPIType,
                                     @Nonnull final IParticipantIdentifier aParticipantID,
                                     @Nonnull final Map <String, String> aSGHrefs,
                                     @Nonnull final IIdentifierFactory aIF)
  {
    // Main conversion
    final IJsonObject aJson = SMPJsonResponse.convert (eSMPAPIType, aParticipantID, aSGHrefs, aIF);

    // Add nice names from code list
    final IJsonArray aURLsArray = aJson.getAsArray (SMPJsonResponse.JSON_URLS);
    if (aURLsArray != null)
      for (final IJsonObject aUrlEntry : aURLsArray.iteratorObjects ())
      {
        final String sDocType = aUrlEntry.getAsString (SMPJsonResponse.JSON_DOCUMENT_TYPE_ID);
        if (sDocType != null)
        {
          final NiceNameEntry aNN = NiceNameManager.docTypeNames ().get (sDocType);
          if (aNN != null)
          {
            aUrlEntry.add (JSON_NICE_NAME, aNN.getName ());
            aUrlEntry.add (JSON_STATE, aNN.getState ().getID ());
            aUrlEntry.add (JSON_IS_DEPRECATED, aNN.getState ().isDeprecated ());
          }
        }
      }
    return aJson;
  }
}
