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

import java.io.*;
import java.util.Properties;
import org.xml.sax.XMLReader;
import org.xml.sax.XMLFilter;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.ccil.cowan.tagsoup.XMLWriter;

/**
 * Call createInstance to specify an xssChangeWriter and a Writer.
 * Call process to parse inputString using TagSoup.
 * DeXSS notes any changes to xssChangeWriter, and serializes the resulting document to the Writer. 
 */
public class DeXSS {

  private final DeXSSParser dexssParser;
  private final InputSource inputSource;

  /**
   * Create a DeXSS object.  Modifications will be noted to xssChangeListener, and the
   * resulting document serialized to w.
   * @param xssChangeListener the DeXSSChangeListener which is informed of changes
   * @param w the Writer to which the parsed document is serialized
   */
  public DeXSS() throws IOException, SAXException {
    dexssParser = new DeXSSParser();
    //dexssParser.setProperty(Parser.lexicalHandlerProperty, h);
    inputSource = new InputSource();
  }

  private static XMLWriter wrapXMLWriter(Writer w) {
    XMLWriter x = new XMLWriter(w);
    x.setOutputProperty(XMLWriter.OMIT_XML_DECLARATION, "yes");
    x.setOutputProperty(XMLWriter.ENCODING, "utf-8");
    x.setOutputProperty(XMLWriter.INDENT, "no");
    x.forceNSDecl("http://www.w3.org/1999/xhtml");
    return x;
  }

  /**
   * Call process to parse inputString using TagSoup.
   * @param inputString the String to parse
   * @return parsed string
   */
  public String process(String inputString) throws IOException, SAXException {
    return process(inputString, null);
  }

  /**
   * @param inputString the String to parse
   * @param xssChangeListener the DeXSSChangeListener which is informed of changes
   * Notes any changes to xssChangeWriter, and serializes the resulting document to the Writer. 
   * @return parsed string
   */
  public String process(String inputString, DeXSSChangeListener xssChangeListener) throws IOException, SAXException {
    StringWriter sw = new StringWriter();
    dexssParser.setDeXSSChangeListener(xssChangeListener);
    dexssParser.setContentHandler(wrapXMLWriter(sw));
    // http://java.sun.com/j2se/1.4.2/docs/api/index.html
    // "Once a parse is complete, an application may reuse the same XMLReader object, possibly with a different input source."
    // Might need to set the reuse flag.
    // XMLWriter.startDocument resets the XMLWriter, but doesn't clear its namespaces.
    inputSource.setCharacterStream(new StringReader(inputString));
    inputSource.setEncoding("UTF-8");
    dexssParser.parse(inputSource);
    return sw.toString();
  }
}

