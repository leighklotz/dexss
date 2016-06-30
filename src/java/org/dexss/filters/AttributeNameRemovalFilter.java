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

import java.util.HashMap;
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
 * Attribute Removal Filter;
 * Removes attributes matching names added with {@link #add(String)}.
 */
public class AttributeNameRemovalFilter extends DeXSSFilterImpl {
  private final Set<String> attributeLocalNames;

  public AttributeNameRemovalFilter(DeXSSChangeListener xssChangeListener) {
    this(xssChangeListener, new HashSet<String>());
  }

  /**
   * Adds name to the list names regexps for attribute names that this filter should remove.
   * @param name name to add
   */
  public void add(String name) {
    attributeLocalNames.add(name);
  }

  public AttributeNameRemovalFilter(DeXSSChangeListener xssChangeListener, Set<String> attributeLocalNames) {
    super(xssChangeListener);
    this.attributeLocalNames = attributeLocalNames;
  }

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    int nAttrs = atts.getLength();
    for (int attNum = 0; attNum < nAttrs; attNum++) {
      String attName = atts.getLocalName(attNum);
      if (attributeLocalNames.contains(attName)) {
	if (atts instanceof org.ccil.cowan.tagsoup.AttributesImpl) {
	  atts = Utils.removeAttribute(atts, attNum);
	  if (xssChangeListener != null)
            xssChangeListener.logXSSChange("Removing attribute", localName, attName);
	  nAttrs--;
	  attNum--;
	}
      }
    }
    super.startElement(namespaceURI, localName, qName, atts);
  }
}
