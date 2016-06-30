# DeXSS -- Java program for removing JavaScript from HTML

Dynamic web sites which allow users to enter text content
containing HTML are at risk for so-called *cross-site scripting* attacks
([Wikipedia](http://en.wikipedia.org/wiki/Cross_Site_Scripting)),
[Securitydocs](http://www.securitydocs.com/library/3472)[attacks.

A common approach taken to mitigate this risk is to allow
some HTML content, but block content that is potentially
harmful. One problem with a straightforward approach to blocking such
content is that HTML parsing in browsers differs from the ideal,
and nefarious individuals can take advantage of these differences
to obscure content.

DeXSS uses [TagSoup](http://tagsoup.info)) an
open-source HTML parser that attempts to mimic how web browsers
work. TagSoup reads wild HTML and generates [SAX2](http://www.saxproject.org)events. DeXSS invokes
TagSoup and follows it with a pipeline of SAX2 filters to remove HTML
tags such as <code>script</code> and attribute values containing such
scripts.

## Download
See also [DeXSS](https://dexss.org)

## Status
DeXSS 1.2 is an Alpha release.  You should be aware of the following issues:

- This release implements a <em>blacklist</em> approach, which has advantages over a whitelist approach, but also has inherent risks.  There are still a number of known XSS attacks that DeXSS does not yet detect.
- DeXSS is agressive about removing style attributes that fail the CSS analyzer.  There are probably other CSS attacks that DeXSS does not protect against.
- Elements that TagSoup thinks should be in the <em>head</em> are discarded by the default settings; changing the BODY_ONLY flag to allow <em>head</em> content will reduce effectiveness greatly.  Consequently, DeXSS should not be used to parse entire user-provided HTML files, but only parts that are destined for inclusion.
- The output of DeXSS is intended for browsers, not for storage.  As a result, some constructs may be overly verbose.
- Configurability and test suites are lacking.
- DeXSS does not specially handle any HTML5 elements or attributes not present in HTML4; **see HTMLCleaner below**.

If you have an interest in working on these issues, please consider contributing to the project.

## DeXSS API
DeXSS includes the following classes for direct use:


- <a href="blob/master/docs/api/org/dexss/Test.html">Test</a>, a command-line utility for testing XSS
removal.

- <a href="blob/master/docs/api/org/dexss/DeXSS.html">DeXSS</a>, which implements a string-to-string
conversion of HTML, with XSS removal.

- <a href="blob/master/docs/api/org/dexss/DeXSSParser.html">DeXSSParser</a>, which can be used directly as a SAX2 parser to
produce SAX2 events from an input stream.

- <a href="blob/master/docs/api/org/dexss/DeXSSFilterPipeline.html">DeXSSFilterPipeline</a>, which can be used as a SAX2 filter if you have already used TagSoup to produce SAX2 events

	
## Documentation

- <a href="blob/master/LICENSE-2.0">LICENSE</a>
- <a href="blob/master/README.txt">README.txt</a>
- <a href="blob/master/CHANGES">CHANGES</a>
- <a href="blob/master/docs/api">JavaDoc</a>


## Download
<h3>Current Version</h3>

- <a href="blob/master/.jar">dexss-1.2.jar</a>
- <a href="blob/master/dexss-1.2-src.zip">dexss-1.2-src.zip</a>
- <a href="blob/master/dexss-1.2-docs.zip">dexss-1.2-docs.zip</a>


## How to build
<ol>
- Type <code>ant dist -emacs</code>
</ol>

# Dependencies

- dexss includes <code>tagsoup-1.2.1.jar</code> from <a href="http://tagsoup.info">http://tagsoup.info</a>  If you need to change the TagSoup version, edit the file etc/build/build.properties.
- dexss includes <code>osbcp-css-paser-1.4.jar</code> from <a href="http://github.com/corgrath/osbcp-css-parser">http://github.com/corgrath/osbcp-css-parser</a>  If you need to change the OSBCP CSS Parser version, edit the file etc/build/build.properties.



## How to test
- Test for false positives
````
 java -classpath lib/tagsoup-1.2.1.jar:lib/osbcp-css-parser-1.4.jar:dist/lib/dexss-1.2.jar org.dexss.Test tests/benign/*.txt 
````
or
````
 java -classpath lib\tagsoup-1.2.1.jar\;lib\osbcp-css-parser-1.4.jar\;dist\lib\dexss-1.2.jar org.dexss.Test tests/benign/*.txt 
````


- Test for false negatives
````
 java -classpath lib/tagsoup-1.2.1.jar:lib/osbcp-css-parser-1.4.jar:dist/lib/dexss-1.2.jar org.dexss.Test tests/xss/*.txt 
````
or
````
 java -classpath lib\tagsoup-1.2.1.jar\;lib\osbcp-css-parser-1.4.jar\;dist/lib/dexss-1.2.jar org.dexss.Test tests/xss/*.txt 
````

</ol>


## Other Similar Projects
If DeXSS does not meet your needs, see <a
href="http://freecode.com/search/?q=xss&amp;section=projects">freecode.com</a>
for a list of similar libraries in other languages such as PHP and
Perl.

## Todo
- CSS analyzer is still not applied to style elements, only style attributes.
- Should upgrade from TagSoup to HTMLCleaner to get HTML5
- Should offer both blacklist and whitelist configurations for pipeline.
- Should upgrade build and test system to modern standards.


## Warranty
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

## Copyright
- This software is released under Apache 2.0 License (see file <a href="LICENSE">LICENSE</a>).
- Copyright (C) 2005, 2006, 2007, 2012 Xerox Corporation
- Copyright (C) 2012 Leigh L. Klotz, Jr.
- Portions of the file build.xml were derived from TagSoup http://tagsoup.info Copyright (c) 2007 John Cowan licensed under Apache 2.0.

## LICENSE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Portions of the file build.xml were derived from TagSoup http://tagsoup.info Copyright (c) 2005-2008 John Cowan licensed under Apache 2.0.

