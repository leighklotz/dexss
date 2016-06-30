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
 * Base class for XSS filters
 * Extends {@link XMLFilterImpl} and provides the methods for DeXSSChangeListener.
 */
public class DeXSSFilterImpl extends XMLFilterImpl implements XMLFilter {
  protected DeXSSChangeListener xssChangeListener;

  public DeXSSFilterImpl(DeXSSChangeListener xssChangeListener) {
    super();
    this.xssChangeListener = xssChangeListener;
  }

  /*
   * Permit re-use by resetting state on setParent
   */
  public void setParent(XMLReader r) {
    super.setParent(r);
  }

  public void setDeXSSChangeListener(DeXSSChangeListener xssChangeListener) {
    this.xssChangeListener = xssChangeListener;
  }

  public DeXSSChangeListener getXSSChangeListener() {
    return xssChangeListener;
  }

  protected void logXSSChange(String message) {
    xssChangeListener.logXSSChange(message);
  }

  protected void logXSSChange(String message, String item1) {
    if (xssChangeListener != null)
      xssChangeListener.logXSSChange(message, item1);
  }

  protected void logXSSChange(String message, String item1, String item2) {
    if (xssChangeListener != null)
      xssChangeListener.logXSSChange(message, item1, item2);
  }

}
 
