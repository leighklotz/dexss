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
import java.net.URLConnection;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.ext.LexicalHandler;

import org.dexss.*;

/**
 * Body Contents Filter; drops everything that's not in &lt;body&gt;
 * <p>When used with TagSoup, will drop elements such as &lt;style&gt; that should be in &lt;head&gt; but are written in &lt;body&gt;.
 */
public class BodyContentsFilter extends DeXSSFilterImpl {
  int inHead = 0;
  int inBody = 0;

  public BodyContentsFilter(DeXSSChangeListener xssChangeListener) {
    super(xssChangeListener);
  }

  public void startElement(String namespaceURI, String localName,
   String qName, Attributes atts) throws SAXException {
    if (localName.equalsIgnoreCase("body")) {
      inBody++;
    } else {
      if (localName.equalsIgnoreCase("head")) {
	inHead++;
      } else {
	if (inBody > 0 && inHead == 0)
	  super.startElement(namespaceURI, localName, qName, atts);
      }
    }
  }

  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (localName.equalsIgnoreCase("body")) {
      inBody--;
    } else {
      if (localName.equalsIgnoreCase("head")) {
	inHead--;
      } else {
	if (inBody > 0 && inHead == 0)
	  super.endElement(namespaceURI, localName, qName);
      }
    }
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    if (inBody > 0 && inHead == 0)
      super.characters(ch, start, length);
  }

  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    if (inBody > 0 && inHead == 0)
      super.ignorableWhitespace(ch, start, length);
  }
}
 
