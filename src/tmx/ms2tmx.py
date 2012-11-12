# Python code to read Microsoft Terminology.csv and save selected columns in TMX file

header = '''<?xml version="1.0" encoding="UTF-8"?>
<tmx version="1.4">
 <header
  creationtool="tm"
  creationtoolversion="1.0"
  datatype="PlainText"
  segtype="sentence"
  adminlang="en-us"
  srclang="EN"
  o-tmf="Transmem"
  creationdate="2007"
  creationid="TM"
  changedate="2007">
  <prop type="Domain">IT</prop>
 </header>
 <body>
'''
footer = '''
 </body>
</tmx>
'''

def saveunit(fo,col,lang,term):
	if term.find('&')>0:
		term = term.replace('&','&amp;')
	if term.find('<')>0:
		term = term.replace('<','&lt;')
	fo.write('\t<tuv xml:lang="%s">\n'%lang)
	fo.write('\t\t<seg>%s</seg>\n'%term.encode('utf-8'))
	fo.write('\t</tuv>\n')

#Main

filespec = 'Microsoft Terminology.csv'
firstrow = 16
domain = 'IT'
#columns = [[0,'EN'],[6,'DE'],[12,'FR'],[19,'JA'],[32,'RU'],[43,'ZH']]
columns = [[0,'EN'],[43,'ZH']]
tuid = 0	#first ID number default to 1

import codecs

fi = codecs.open(filespec,'r','utf-16')
fo = open(filespec+'.tmx', 'wt')
fo.write(header)
for i in range(firstrow):
	line = fi.readline()
while True:
	line = fi.readline()
	if line == '': break
	segs = line.split('\t')
	if len(segs)<46:
		#print len(segs),segs[0]
		continue
	#print len(segs),segs[0]
	tuid += 1
	fo.write('<tu tuid="%d" datatype="Text">\n'%tuid)
	for col,lang in columns:
		saveunit(fo,col,lang,segs[col])
	fo.write('</tu>\n')
fo.write(footer)
fo.close()
fi.close()
