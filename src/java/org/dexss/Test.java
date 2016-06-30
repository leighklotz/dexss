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

import java.io.StringWriter;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.*;
import java.util.Properties;

import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.ext.LexicalHandler;
import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.ccil.cowan.tagsoup.XMLWriter;


/**
 * This class satisfies the @link DeXSSChangeListener interface and offers
 * a command-line utility for applying DeXSS to files.  It reports a possible
 * failure for any files that <em>don't</em> change.
 *
 * TODO: Do a better job of testing or expected removal and non-removal
 * of XSS code.
 */
public class Test implements DeXSSChangeListener {
  boolean changed = false;
  boolean showChanges = true;
  PrintWriter changeLogWriter;
  StringWriter changeLog = null;
  PrintWriter resultsWriter;
  boolean showRender=false;

  public Test() {
    changeLogWriter = new PrintWriter(System.err);
    resultsWriter = new PrintWriter(System.out);
  }

  public void logXSSChange(String message) {
    if(showChanges)
      changeLogWriter.println("* " + message);
    changed = true;
  }

  public void logXSSChange(String message, String item1) {
    if(showChanges)
      changeLogWriter.println("* " + message + " " + item1);
    changed = true;
  }

  public void logXSSChange(String message, String item1, String item2) {
    if(showChanges)
      changeLogWriter.println("* " + message + " " + item1 + " " + item2);
    changed = true;
  }

  private boolean isChanged() {
    return changed;
  }

  private void resetChanged() {
    changed = false;
  }

  /**
   * This command-line test program processes the specified files or literals, and for each one
   * prints to System.out the following:
   * <ul>
   * <li>the file name (if any)</li>
   * <li>Any change messages from {@link DeXSSChangeListener}</li>
   * <li>Serialized XML result</li>
   * <li>A summary indicating whether the input changed or not (based on whether there were any XSSChangeListener messages)</li>
   * </ul>
   * TODO: A better test and regression harness.  More Test cases.
   * 
   * @param argv Command-line args are files to process, or if first arg is hypen, strings to process.
   */
  public static void main(String[] argv) throws Exception {
    Test test = new Test();

    test.test(argv);
  }

  private void test(String[] argv) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
    if (argv.length > 0) {
      boolean isLiteral = argv[0].equals("-");
      boolean isControlFile = argv[0].equals("--control");
      boolean isHTMLOut = argv[0].equals("--htmlout");
      String controlFileName = argv.length > 1 ? argv[1] : null;
      String htmlOutDir = isHTMLOut ? argv[2] : null;
      String htmlOutFileName = isHTMLOut ? htmlOutDir + "/"+"index.html" : null;
      if (isLiteral) {
	for (int i = 1; i < argv.length; i++) {
          testOne(argv[i], "arg " + i);
        }
      } else if (isControlFile || isHTMLOut) {
        if (isHTMLOut) {
          resultsWriter.close();
          resultsWriter = new PrintWriter(new FileWriter(htmlOutFileName));
          resultsWriter.println("<html><head><title>DeXSS Results</title>");
          resultsWriter.println("<style type='text/css'>"+
                             ".scroll {overflow:auto; width:100%;}"+
                             ".even {background:#EAEAEA;}"+
                             "thead th {border-bottom:1px solid #000;}"+
                             "pre strong {color:#00C;}"+
                             "pre .linebreak {color:#AAA;font-weight:100;}"+
                             "</style>");
          resultsWriter.println("</head>");
          resultsWriter.println("<body>");
          resultsWriter.println("<h1>DeXSS Results</h1>");
          resultsWriter.println("<p>"+
                             "This page attempts to mimic for formatting of "+
                             "<a href=\"http://htmlpurifier.org/live/smoketests/xssAttacks.php\">HTML Purifier</a> "+
                             "results."+
                             "</p>");
          resultsWriter.println("<table>");
          resultsWriter.println("<tr>");
          resultsWriter.println("<th>Name</th>");
          resultsWriter.println("<th>Input</th>");
          resultsWriter.println("<th>Output</th>");
          resultsWriter.println("<th>Comments</th>");
          if (showRender) {
            resultsWriter.println("<th>Render</th>");
          }
          resultsWriter.println("</tr>");
          resultsWriter.close();
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document control = db.parse(new File(controlFileName));
        NodeList attacks = selectNodes(control, "/xss/attack");
        int nAttacks = attacks.getLength();
        for (int i = 0; i < nAttacks; i++) {
          Element attack = (Element)(attacks.item(i));
          String name = valueOf(attack, "name");
          String code = valueOf(attack, "code");
          if (isControlFile) 
            testOne(code, name);
          else if (isHTMLOut) 
            printOne(code, name, i, htmlOutDir);
          else throw new RuntimeException("can't happen");
        }
        resultsWriter.println("</table></body></html>");
      } else {
        StringWriter sw = new StringWriter();
	for (int i = 0; i < argv.length; i++) {
	  String filename = argv[i];
	  String code = readFile(filename);
          testOne(code, filename);
	}
      }
    }
    resultsWriter.flush();
  }

  private void testOne(String code, String name) throws IOException, SAXException {
    resultsWriter.println("* " + name);
    DeXSS xss = new DeXSS();
    String out = xss.process(code, this); // ignore output; just check for changes
    if (! isChanged()) {
      if (out.length() == 0) {
        resultsWriter.println("- " + name + " deleted ");
        resultsWriter.println("In: " + code);
      } else if (out.equalsIgnoreCase(code)) {
        resultsWriter.println("+ " + name + " unchanged ");
        resultsWriter.println("In: " + code);
      } else {
        // TagSoup might have cleaned it up for us, or BodyContentsFilter
        // TODO: case, xmlns
        resultsWriter.println("+ " + name + " possibly unchanged ");
        resultsWriter.println("In: " + code);
        resultsWriter.println("Out: " + out);
      }
    } else {
      resultsWriter.println("- " + name + " changed");
      resultsWriter.println("In: " + code);
      resultsWriter.println("Out: " + out);
    }
    resetChanged();
  }

  private String wrap(String out) {
    int max = 32;
    if (out.length() < max) return out;
    return out.substring(0,32) + "<br />" + wrap(out.substring(32));
  }

  private String escape (String out) {
    return out.
      replace("&", "&amp;").
      replace("<", "&lt;").
      replace(">", "&gt;").
      replace("\"", "&#34;").
      replace("'", "&#39;").
      replace("\\", "&#92;");
  }
  private void printOne(String code, String name, int i, String htmlOutDir) throws IOException, SAXException {
    String cssclass = ((i%2 == 0) ? "even" : "odd");
    // Column 1: name
    resultsWriter.print("<tr class=\""+cssclass+"\"><td>" + name+ "</td>");
    changeLog = new StringWriter();
    changeLogWriter = new PrintWriter(changeLog);
    Writer sw = new StringWriter(); // ignore output
    DeXSS xss = new DeXSS();
    xss.process(code, this);
    sw.close();
    String out = sw.toString();
    // We have patched it not to put newline after elemnets if INDENT=no, but
    // tagsoup XMLWriter always appends a newline to end of document.
    {
      int len1 = out.length()-1;
      if (out.charAt(len1) == '\n') out = out.substring(0, len1);
    }

    // Column 2: input
    resultsWriter.print("<td>");
    String wrappedEscapedCode = wrap(escape(code));
    resultsWriter.print(wrappedEscapedCode);
    resultsWriter.print("</td>");

    // Column 3: output
    resultsWriter.print("<td>");
    resultsWriter.print(wrap(escape(out)));
    resultsWriter.print("</td>");

    if (true) {
      // Column 4: Comments
      resultsWriter.print("<td>");
      {
        resultsWriter.print(changeLog.toString());
        if (! isChanged()) {
          if (out.length() == 0) {
            resultsWriter.println("deleted");
          } else if (out.equalsIgnoreCase(code)) {
            resultsWriter.println("unchanged");
          } else {
            // TagSoup might have cleaned it up for us, or BodyContentsFilter
            // TODO: case, xmlns
            resultsWriter.println("possibly unchanged, length "+out.length());
          }
        } else {
          resultsWriter.println("changed");
        }
      }
      resultsWriter.println("</td>");
    }
    changeLogWriter.flush();
    resetChanged();

    // Column 5: Render
    {
      String fn =  + i + ".html";
      {
        resultsWriter.println("<td>");
        resultsWriter.print("<a href=\"" + fn + "\">" + i + "</a>");
        resultsWriter.println("</td>");
        resultsWriter.println("</tr>");
      }
      {
        File f = new File(htmlOutDir + "/" + fn);
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        pw.println("<html><head><title>Test " + name + "</title></head>");
        pw.println("<body>");
        pw.print("<h1>Test " + name + "</h1>");
        pw.print("<h2>Code</h2>");
        pw.print("<pre>");
        pw.println(wrappedEscapedCode);
        pw.println("</pre><hr />");
        pw.println("<h2>Rendered</h2>");
        pw.println("<!-- XSS Begin -->");
        pw.print(out);
        pw.println("<!-- XSS End -->");
        pw.println("</body></html>");
        pw.close();
      }
    }
  }

  private NodeList selectNodes(Document document, String path) throws XPathExpressionException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    return (NodeList)(xpath.evaluate(path, document, XPathConstants.NODESET));
  }

  private NodeList selectNodes(Element element, String path) throws XPathExpressionException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    return (NodeList)(xpath.evaluate(path, element, XPathConstants.NODESET));
  }

  private String valueOf(Element element, String path) throws XPathExpressionException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    return (String)(xpath.evaluate(path, element, XPathConstants.STRING));
  }

  
  private String readFile(String file) throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(file));
    StringWriter out = new StringWriter();
    int c;

    while ((c = in.read()) != -1)
      out.write(c);

    in.close();
    out.close();
    return out.toString();
  }
}
