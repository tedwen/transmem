import sys
#count the maximum term length in a TMX file

if len(sys.argv)<2:
	print 'tmxtermlen <tmx_file>'
else:
	fi = open(sys.argv[1])
	langcounts = {}
	while True:
		line = fi.readline()
		if line=='': break
		if line.find('<tuv ')>0:
			n = line.find('"')
			lang = line[n+1:n+3]
			line = fi.readline()
			n = line.find('>')
			n2 = line.rfind('<')
			term = line[n+1:n2]
			n2 = len(term)
			if langcounts.has_key(lang):
				n = langcounts[lang]
				if n2 > n:
					langcounts[lang] = n2
			else:
				langcounts[lang] = n2
	fi.close()
	for ln in langcounts.keys():
		print ln, langcounts[ln]
