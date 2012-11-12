f = open('MsTerms.tmx.sql')
i = 1
while True:
	line = f.readline()
	if line == '': break
	if line.startswith('insert into T_EnglishTerms'):
		n1 = line.find('values(')+7
		n2 = line.find(',',n1)
		enn = line[n1:n2]
		n1 = n2+2
		n2 = line.find(',',n1)-1
		ens = line[n1:n2]
	elif line.startswith('insert into T_ChineseTerms'):
		n1 = line.find('values(')+7
		n2 = line.find(',',n1)
		zhn = line[n1:n2]
		n1 = n2+2
		n2 = line.find(',',n1)-1
		zhs = line[n1:n2]
		if zhn==enn:
			#print '%d\t%s\t%s\tIT\t0\t0\t0'%(i,ens,zhs)
			print "insert into T_ENZH(F_SID,F_Source,F_Target,F_Domain) values(%d,'%s','%s','IT');"%(i,ens,zhs)
			i += 1
f.close()
#print 'Please update S_ENZH nextval to %d'%i