// -*-JAVA-*-
// Copyright 2005, 2006, 2007, 2012 Xerox Corporation
// Copyright 2012 Leigh L. Klotz, Jr. http://dexss.org
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.

package org.dexss;

/**
 * Objects implementing this interface are suitable for Property {@link DeXSSFilterPipeline#DeXSS_CHANGE_LISTENER}.
 * Useful mostly for debugging, or to log XSS events.
 * 
 * TODO: An upgrade that accepts a SAX2 Location would be nice.
 */
public interface DeXSSChangeListener {
  /**
   * Called when a change happens but there is no other information.
   * @param message Main message
   */
  public void logXSSChange(String message);

  /**
   * Called when a change happens and there is one other informational item.
   * @param message Main message
   * @param item1 Information item
   */
  public void logXSSChange(String message, String item1);

  /**
   * Called when a change happens and there are two informational items.
   * @param message Main message
   * @param item1 Information item 1
   * @param item2 Information item 2
   */
  public void logXSSChange(String message, String item1, String item2);
}
