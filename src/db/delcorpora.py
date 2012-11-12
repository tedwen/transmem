import psycopg2

langs = ['EN','ZH','FR','DE','IT','ES','RU','JA','KO','TH','SV','PT','NO','FI','NL','DA']

def remove_corpora():
	conn = psycopg2.connect('dbname=transmem user=tm password=wu1li4fh1 host=localhost port=5432')
	curs = conn.cursor()
	for i in range(len(langs)):
		for j in range(len(langs)):
			if i != j:
				tname = ''.join(['T_',langs[i],langs[j]])
				sql = "SELECT * FROM pg_tables WHERE tablename='%s'"%tname.lower()
				#print sql
				curs.execute(sql)
				rows = curs.fetchall()
				if len(rows) > 0:
					#print 'rows = %d'%len(rows)
					tname1 = tname + '_' + langs[i] + 'X'
					tname2 = tname + '_' + langs[j] + 'X'
					sql1 = 'DROP TABLE %s'%tname1
					sql2 = 'DROP TABLE %s'%tname2
					sql3 = 'DROP TABLE %s'%tname
					sql4 = 'DROP SEQUENCE S%s'%tname[1:]
					print '%s;\n%s;\n%s;\n%s;\n'%(sql1,sql2,sql3,sql4)
					#curs.execute(sql1)
					#curs.execute(sql2)
					#curs.execute(sql3)
					#curs.execute(sql4)
	conn.close()

remove_corpora()

#curs = conn.cursor()
#curs.execute('SELECT * FROM T_Planets')
#rows = curs.fetchall()
#for i in range(len(rows)):
#	r = rows[i]
#	for c in r:
#		print c,
#	print
#conn.close()
