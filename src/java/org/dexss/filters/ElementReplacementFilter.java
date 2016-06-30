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

import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.ext.LexicalHandler;

import org.dexss.*;

/**
 * Element Replacement filter; replaces one element name with another, but leaves content alone.
 * Only local name is compared; namespace is ignored.
 */
public class ElementReplacementFilter extends DeXSSFilterImpl {
  private final Map<String,String> replacements;

  public ElementReplacementFilter(DeXSSChangeListener xssChangeListener) {
    this(xssChangeListener, new HashMap<String,String>());
  }

  public ElementReplacementFilter(DeXSSChangeListener xssChangeListener, Map<String,String> replacements) {
    super(xssChangeListener);
    this.replacements = replacements;
  }

  /**
   * Adds from and to to the list of element names for elements names that this filter should rename.
   * Only local name is compared; namespace is ignored.
   * @param from old name
   * @param to new name
   */
  public void add(String from, String to) {
    replacements.put(from, to);
  }

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    String replacement = replacements.get(localName);
    if (replacement != null) {
      // TODO: What about namespace and qName?
      logXSSChange("Replacing element", localName, replacement);
      super.startElement(namespaceURI, replacement, replacement, atts);
    }
    else
      super.startElement(namespaceURI, localName, qName, atts);
  }
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    String replacement = replacements.get(localName);
    if (replacement != null)
      super.endElement(namespaceURI, replacement, replacement);
    else
      super.endElement(namespaceURI, localName, qName);
  }
}


  
