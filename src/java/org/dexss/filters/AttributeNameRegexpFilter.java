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
 * Attribute Removal Filter;
 * Removes attributes matching regexps added with {@link #add(String)}.
 */
public class AttributeNameRegexpFilter extends DeXSSFilterImpl {
  private final List<Pattern> regexps;
  private final int flags;

  public AttributeNameRegexpFilter(DeXSSChangeListener xssChangeListener, List<Pattern> regexps) {
    super(xssChangeListener);
    this.flags = 0;
    this.regexps = regexps;
  }

  public AttributeNameRegexpFilter(DeXSSChangeListener xssChangeListener, String regexp) {
    this(xssChangeListener, new ArrayList<Pattern>());
    add(regexp);
  }

  /**
   * Adds regexp to the list of regexps for attribute names that this filter should remove.
   * @param regexp regexp to add
   */
  public void add(String regexp) {
    regexps.add(Pattern.compile(regexp, flags));
  }

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    int nAttrs = atts.getLength();
    for (int attNum = 0; attNum < nAttrs; attNum++) {
      String attName = atts.getLocalName(attNum);
      for (Pattern pattern : regexps) {
	if (pattern.matcher(attName).find()) { // TODO: Is there a way to re-use matcher in a single thread?
	  atts = Utils.removeAttribute(atts, attNum);
          logXSSChange("Removing attribute", localName, attName);
	  attNum--;
	  nAttrs--;
	  break;
	}
      }
    }
    super.startElement(namespaceURI, localName, qName, atts);
  }
}
