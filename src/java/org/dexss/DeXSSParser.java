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

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.ccil.cowan.tagsoup.Parser;

/**
 * The DeXSSParser object.
 * This class can be used as a SAX2 XML Parser that first applies TagSoup, then applies the {@link DeXSSFilterPipeline}.
 * <p>Example:
 * <pre>{
 *  DeXSSParser dexssParser = new DeXSSParser();
 *  dexssParser.setContentHandler(new XMLWriter(writer));
 *  InputSource inputSource = new InputSource();
 *  inputSource.setCharacterStream(new StringReader(inputString));
 *  dexssParser.parse(inputSource);
 *}
 * </pre>
 * </p>
 */
public class DeXSSParser extends DeXSSFilterPipeline {
  /**
   * Creates a DeXSSParser with the following feature set:
   * <ul>
   * <li>{@link DeXSSFilterPipeline#BODY_ONLY} <code>true</code></li>
   * </ul>
   * And uses as parent a {@link org.ccil.cowan.tagsoup.Parser} with the following feature set:
   * <ul>
   * <li>{@link org.ccil.cowan.tagsoup.Parser#ignoreBogonsFeature} <code>true</code></li>
   * <li>{@link org.ccil.cowan.tagsoup.Parser#defaultAttributesFeature} <code>false</code></li>
   * </ul>
   * TODO: Should be made more configurable.
   */
  public DeXSSParser() throws SAXNotRecognizedException, SAXNotSupportedException {
    super();
    setFeature(DeXSSFilterPipeline.BODY_ONLY, true);
    Parser parser = new Parser();
    parser.setFeature(Parser.ignoreBogonsFeature, true);
    parser.setFeature(Parser.defaultAttributesFeature, false);
    setParent(parser);
  }
}
