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
import java.util.ArrayList;
import java.io.*;
import java.net.URL;
import java.util.Set;
import java.util.HashSet;
import java.net.URLConnection;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.ext.LexicalHandler;

import org.dexss.*;

/**
 * Attribute Start Filter; removes attributes whose name starts with the specified name
 */
public class AttributeNameStartFilter extends DeXSSFilterImpl {
  private final List<String> starts;

  public AttributeNameStartFilter(DeXSSChangeListener xssChangeListener) {
    this(xssChangeListener, new ArrayList<String>());
  }


  public AttributeNameStartFilter(DeXSSChangeListener xssChangeListener, List<String> starts) {
    super(xssChangeListener);
    this.starts = starts;
  }

  public AttributeNameStartFilter(DeXSSChangeListener xssChangeListener, String startsWith) {
    this(xssChangeListener, new ArrayList<String>());
    add(startsWith);
  }

  /**
   * Adds startsWith to the list of starting strings of attribute names that this filter should remove.
   * @param startsWith String to add
   */
  public void add(String startsWith) {
    starts.add(startsWith);
  }

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    int nAttrs = atts.getLength();
    for (int attNum = 0; attNum < nAttrs; attNum++) {
      String attName = atts.getLocalName(attNum);
      String attValue = atts.getValue(attNum);
      for (String startsWith : starts) {
	if (attName.startsWith(startsWith)) {
	  atts = Utils.removeAttribute(atts, attNum);
	  if (xssChangeListener != null)
            xssChangeListener.logXSSChange("Removing attribute", localName, attName);
	  attNum--;
	  nAttrs--;
	  break;
	}
      }
    }
    super.startElement(namespaceURI, localName, qName, atts);
  }
}
