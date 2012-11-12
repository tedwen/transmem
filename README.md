Transmem
========

### Introduction

Transmem is a translation memory and management application designed for the translation houses to speed up their translation projects by collaboratively reusing and sharing translation memories.

### Features

+  Multilingual support, 16 languages;
+  Online translation, anywhere, anytime;
+  Sentence-by-sentence translating and editing;
+  Automatic translation examples from the memories;
+  Copy-and-paste from the example translations;
+  Download translated text into Word, PDF etc;
+  Forward the translated text to an email address;
+  Submit translations to share as memory;
+  Edit and manage translation memories (TM);
+  Import/Export TM data in TMX format;
+  Document management;
+  Project management;
+  Translator's community, blogs, messaging.

### Installation

You need Java Runtime, Tomcat or JBoss, PostgreSQL installed on the server.

You also need JDK and Apache Ant to build and deploy the application.

Download the package, run ant install.

### Configuration

There are many Python scripts to help with various configuration and data manipulation.

Datasource example in Tomcat/conf/server.xml (under Host):
        <Context path="/transmem" docBase="transmem" debug="5" reloadable="true" crossContext="true">
            <Logger className="org.apache.catalina.logger.FileLogger" prefix="localhost_transmem_log." suffix=".txt" timestamp="true"/>
            
            <Resource name="jdbc/Transmem" auth="Container" type="javax.sql.DataSource"/>

            <ResourceParams name="jdbc/Transmem">
                <parameter>
                  <name>factory</name>
                  <value>org.apache.commons.dbcp.BasicDataSourceFactory</value>
                </parameter>
                <parameter>
                  <name>driverClassName</name>
                  <value>org.postgresql.Driver</value>
                </parameter>
                <parameter>
                  <name>url</name>
                  <value>jdbc:postgresql://127.0.0.1:5432/transmem</value>
                </parameter>
                <parameter>
                  <name>maxActive</name>
                  <value>20</value>
                </parameter>
                <parameter>
                  <name>maxIdle</name>
                  <value>10</value>
                </parameter>
                <parameter>
                  <name>maxWait</name>
                  <value>-1</value>
                </parameter>
                <parameter>
                  <name>username</name>
                  <value>postgres</value>
                </parameter>
                <parameter>
                  <name>password</name>
                  <value>postgres</value>
                </parameter>
            </ResourceParams>

        </Context>


### Utilities
\Transmem\tm\src\utils

gb2unicode.py  - convert GB2312 text into HTML unicode format, such as #&ffff;
				It uses gb-unicode.table cross-ref table
ms2tmx.py  - convert Microsoft multilingual terminology file (csv) into TMX file for selected languages
				Output file MsTerms.tmx contains en,de,fr,ja,ru,zh (in UTF-8 encoding)
tmx2sql.py  - generate SQL insert statement from MS terminology TMX file
tmxtermlen.py  - count maximum length of the terms in the <seg> element.

src\db\sentences\
totmx1.py - grab sentences from a list of html files to generate TMX
stmx2sql.py - generate SQL insert to example tables from TMX by totmx1.py

GB2UTF8.exe (additional 3rd-party tool to convert GB to UTF8)

### Note about this project

This project was developed originally for a Chinese-based public web service but was canceled at the beta stage. The web pages and documents are still in Chinese language.  The almost-ready-to-use version is now released as open source in the hope that interested parties can get involved to use it, improve it, and translate it into other languages.

### Author

**Ted Wen**

+ http://twitter.com/tedwen
+ http://github.com/tedwen

### License

This release is licensed under the ASL.
