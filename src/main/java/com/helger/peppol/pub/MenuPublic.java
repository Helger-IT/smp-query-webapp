/*
 * Copyright (C) 2023-2026 Philip Helger (www.helger.com)
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
package com.helger.peppol.pub;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.concurrent.Immutable;
import com.helger.io.resource.ClassPathResource;
import com.helger.peppol.ui.AppPageViewExternal;
import com.helger.photon.core.menu.IMenuTree;

@Immutable
public final class MenuPublic
{
  public static final String MENU_INDEX = "index";

  private MenuPublic ()
  {}

  public static void init (@NonNull final IMenuTree aMenuTree)
  {
    // Common stuff
    aMenuTree.createRootItem (new AppPageViewExternal (MENU_INDEX,
                                                       "Overview",
                                                       new ClassPathResource ("viewpages/en/index.xml")));

    // Set default
    aMenuTree.setDefaultMenuItemID (MENU_INDEX);
  }
}
