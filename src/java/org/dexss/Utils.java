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

import org.xml.sax.Attributes;

/**
 * Utility functions
 */
public abstract class Utils {
  /**
   * Given a SAX2 Attributes and an index, remove the specified attribute as best we can.
   * If the implementation is the SAX Helpers AttributesImpl, use its removeAttribute.
   * If the implementation is TagSoup's, use its removeAttribute.
   * Otherwise, convert it to a SAX Helpers implementation and use its removeAttribute.
   */
  public static Attributes removeAttribute(Attributes atts, int attNum) {
    if (atts instanceof org.ccil.cowan.tagsoup.AttributesImpl) {
      ((org.ccil.cowan.tagsoup.AttributesImpl)atts).removeAttribute(attNum);
      return atts;
    } else if (atts instanceof org.xml.sax.helpers.AttributesImpl) {
      ((org.xml.sax.helpers.AttributesImpl)atts).removeAttribute(attNum);
      return atts;
    } else {
      org.xml.sax.helpers.AttributesImpl newatts = new org.xml.sax.helpers.AttributesImpl(atts);
      newatts.removeAttribute(attNum);
      return newatts;
    }
  }

  /**
   * Given a SAX2 Attributes and an index, remove the specified attribute as best we can.
   * If the implementation is the SAX Helpers AttributesImpl, use its setAttribute
   * If the implementation is TagSoup's, use its setAttribute
   * Otherwise, convert it to a SAX Helpers implementation and use its setAttribute
   */
  public static Attributes clearAttribute(Attributes atts, int attNum) {
    return setAttributeValue(atts, attNum, "");
  }

  /**
   * Given a SAX2 Attributes and an index, set the specified attribute as best we can.
   * If the implementation is the SAX Helpers AttributesImpl, use its setAttribute
   * If the implementation is TagSoup's, use its setAttribute
   * Otherwise, convert it to a SAX Helpers implementation and use its setAttribute
   */
  public static Attributes setAttributeValue(Attributes atts, int attNum, String value) {
    if (atts instanceof org.ccil.cowan.tagsoup.AttributesImpl) {
      ((org.ccil.cowan.tagsoup.AttributesImpl)atts).setValue(attNum, value);
      return atts;
    } else if (atts instanceof org.xml.sax.helpers.AttributesImpl) {
      ((org.xml.sax.helpers.AttributesImpl)atts).setValue(attNum, value);
      return atts;
    } else {
      org.xml.sax.helpers.AttributesImpl newatts = new org.xml.sax.helpers.AttributesImpl(atts);
      newatts.setValue(attNum, value);
      return newatts;
    }
  }
}
