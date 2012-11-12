import sys
from xml.dom.minidom import parse
#generate SQL insert statements from a TMX file

langtabs = {'EN':'T_EnglishTerms','DE':'T_GermanTerms','FR':'T_FrenchTerms','JA':'T_JapaneseTerms','RU':'T_RussianTerms','ZH':'T_ChineseTerms'}

if len(sys.argv)<2:
	print 'tmx2sql <tmx_file>'
else:
	domain = 'IT'
	print 'Parsing "%s"'%sys.argv[1]
	dom = parse(sys.argv[1]) # parse file TMX(XML)
	print 'Generating SQL insert statements'
	header = dom.getElementsByTagName('header')
	props = header[0].getElementsByTagName('prop')
	for prop in props:
		if prop.getAttribute('type')=='Domain':
			domain = prop.firstChild.nodeValue
			#print domain
	tus = dom.getElementsByTagName('tu')
	fo = open(sys.argv[1]+'.sql','wt')
	for k in langtabs.keys():
		fo.write('create table %s(\n\tF_TermID int8 not null,\n\tF_Term varchar(255),\n\tF_Domain char(2),\n\tprimary key (F_TermID)\n);\n'%langtabs[k])
	for tu in tus:
		tuid = tu.getAttribute('tuid')
		tuvs = tu.getElementsByTagName('tuv')
		for tuv in tuvs:
			lang = tuv.getAttribute('xml:lang')
			nodes = tuv.childNodes
			for node in nodes:
				#print node.nodeName
				if node.nodeName=='seg':
					if len(node.childNodes)>0:
						#print lang, node.firstChild.nodeValue
						txt = node.firstChild.nodeValue
						if txt.find("'")>0:
							txt = txt.replace("'","''");
						sql = "insert into %s(F_TermID,F_Term,F_Domain) values(%s,'%s','%s');\n"%(langtabs[lang],tuid,txt,domain)
						fo.write(sql.encode('utf-8'));
	fo.close()
