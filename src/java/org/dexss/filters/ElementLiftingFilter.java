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

import java.util.Set;
import java.util.HashSet;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.ext.LexicalHandler;

import org.dexss.*;

/**
 * Element Lifting Filter; Lifts content of matching element (and its attributes) by eliding it and replacing it with its own content.
 */
public class ElementLiftingFilter extends DeXSSFilterImpl {
  private final Set<String> tagnames;

  public ElementLiftingFilter(DeXSSChangeListener xssChangeListener) {
    this(xssChangeListener, new HashSet<String>());
  }

  public ElementLiftingFilter(DeXSSChangeListener xssChangeListener, Set<String> tagnames) {
    super(xssChangeListener);
    this.tagnames = tagnames;
  }

  /**
   * Adds tagname to the list of names for element names that this filter should "lift".
   * @param tagname tagname to add
   */
  public void add(String tagname) {
    tagnames.add(tagname);
  }

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (! tagnames.contains(localName)) {
      logXSSChange("Lifting element", localName);
      super.startElement(namespaceURI, localName, qName, atts);
    }
  }
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (! tagnames.contains(localName))
      super.endElement(namespaceURI, localName, qName);
  }
}


  
