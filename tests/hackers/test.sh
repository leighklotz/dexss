wget http://ha.ckers.org/xssAttacks.xml
java -classpath ../../lib/tagsoup-1.2.1.jar\;../../lib/osbcp-css-parser-1.4.jar;../../dist/lib/dexss-1.2.jar org.dexss.Test --control xssAttacks.xml 
