import sys
import codecs

header = '''<?xml version="1.0" encoding="UTF-8"?>
<tmx version="1.4">
 <header creationtool="transmem" creationtoolversion="1.0" datatype="PlainText"
  segtype="sentence" adminlang="ZH" srclang="EN" o-tmf="Transmem">
  <prop type="Domain">00</prop>
 </header>
<body>'''

if len(sys.argv)<2:
	print 'usage: <dic_file> <src_lang(EN)> <tgt_lang(ZH)>'
else:
	print 'parsing ',sys.argv[1]
	if len(sys.argv)>2:
		srclang = sys.argv[2]
	else:
		srclang = 'EN'
	if len(sys.argv)>3:
		tgtlang = sys.argv[3]
	else:
		tgtlang = 'ZH'
	fo = codecs.open(sys.argv[1]+'_ex.xml','wt','utf-8')
	fo.write('%s'%header)
	fi = codecs.open(sys.argv[1],'r','utf-8')
	line = fi.readline()
	tuid = 1
	while line!="":
		if line.startswith(u'<例句原型>'):
			line2 = fi.readline()
			if line2.startswith(u'<例句解释>'):
				n11 = line.find('<![CDATA[')
				n12 = line.find(u'</例句原型>')
				n21 = line2.find('<![CDATA[')
				n22 = line2.find(u'</例句解释>')
				if n11>0 and n12>n11 and n21>0 and n22>n21:
					#print line[n11+7:n12]
					#print line2[n21+7:n22]
					fo.write('<tu tuid="%d">\n'%tuid)
					fo.write(' <tuv xml:lang="%s">\n  <seg>%s</seg>\n </tuv>\n'%(srclang,line[n11:n12]))
					fo.write(' <tuv xml:lang="%s">\n  <seg>%s</seg>\n </tuv>\n'%(tgtlang,line2[n21:n22]))
					fo.write('</tu>\n')
					tuid += 1
					#break
		line = fi.readline()
	fi.close()
	fo.write('</body>\n</tmx>\n')
	fo.close()
