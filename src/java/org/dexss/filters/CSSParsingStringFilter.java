// -*-JAVA-*-
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

import java.util.regex.Pattern;
import java.util.List;

import org.dexss.DeXSSChangeListener;

import com.osbcp.cssparser.*;

/**
 * CSS String value replacement Filter.
 */
public class CSSParsingStringFilter implements StringFilter {
  private final DeXSSChangeListener xssChangeListener;

  public CSSParsingStringFilter(DeXSSChangeListener xssChangeListener) {
    this.xssChangeListener = xssChangeListener;
  }

  /**
   * Runs string through corgrath-osbcp-css-parser.
   */
  public String filter(String string) {
    StringBuilder result = new StringBuilder();
    final List<Rule> rules;
    try {
      rules = CSSParser.parse(updateString(string));
    } catch (IncorrectFormatException e) {
      // Syntax errors get logged.
      xssChangeListener.logXSSChange(e.toString());
      return "";
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    for (Rule rule : rules) {
      for (PropertyValue propertyValue : rule.getPropertyValues()) {
        result.append(propertyValue.getProperty());
        result.append(":");
        result.append(propertyValue.getValue());
        result.append(";");
      }
    }
    return result.toString();
  }

  // make end with semicolon if it doesn't already; usually that means we will show a change on style, 
  // because almost nobody puts in a trailing semicolon.
  // add a selector to convert to a CSS rule.
  private String updateString(String string) {
    // make end with semicolon if it doesn't already
    String end = (Pattern.matches(".*;\\s*", string)) ? "}" : "; }"; // 
    // add a selector to convert to a CSS rule.
    String css = "body {" + string + end;
    return css;
  }
}
