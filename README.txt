Copyright (C) 2005, 2006, 2007, 2012 Xerox Corporation
Copyright (C) 2012 Leigh L. Klotz, Jr. http://dexss.org

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

How to build:
1. If you need to change the lib/*.jar versions, also edit the file etc/build/build.properties.
2. Type "ant dist -emacs"


How to test:
1. Test for false positives
 java -classpath lib/tagsoup-1.2.1.jar:lib/osbcp-css-parser-1.4.jar:dist/lib/dexss-1.2.jar org.dexss.Test tests/benign/*.txt 

2. Test for false negatives
 java -classpath lib/tagsoup-1.2.1.jar:lib/osbcp-css-parser-1.4.jar:dist/lib/dexss-1.2.jar org.dexss.Test tests/xss/*.txt 
