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

package org.dexss.filters;

import java.util.List;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.net.URLConnection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.ext.LexicalHandler;

import org.dexss.*;

/**
 * Attribute Name/Value replacement Filter;
 * Updates attribute values with specified name by with the specified callback.
 */
public class AttributeNameValueReplacementFilter extends DeXSSFilterImpl {
  private final String targetAttributeName;
  private final StringFilter stringFilter;

  public AttributeNameValueReplacementFilter(DeXSSChangeListener xssChangeListener, String targetAttributeName, StringFilter stringFilter) {
    super(xssChangeListener);
    this.targetAttributeName = targetAttributeName;
    this.stringFilter = stringFilter;
  }

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    int nAttrs = atts.getLength();
    for (int attNum = 0; attNum < nAttrs; attNum++) {
      String attName = atts.getLocalName(attNum);
      if (attName.equals(targetAttributeName)) {
        String value = atts.getValue(attNum);
        String newValue = stringFilter.filter(value);
        if (! newValue.equals(value)) {
          atts = Utils.setAttributeValue(atts, attNum, value);
          logXSSChange("Updating attribute", localName, attName);
        }
        attNum--;
        nAttrs--;
        break;
      }
    }
    super.startElement(namespaceURI, localName, qName, atts);
  }
}
