#
# Validate references in tables.xml and Generate DropTable.sql
#

import os, string, re
from xml.dom.minidom import parse

tableClasses = []
tableNames = []
sequences = []

pfields = re.compile('(\w+)\s*=\s*\?\d')
ptables1 = re.compile('from\s*(\w+)\s*')
ptables2 = re.compile('from\s*(\w+)\s*\w?\s*left join (\w+)')

class Table:
	def __init__(self, tableNode):
		self.tableName = tableNode.getAttribute('name')
		tableNames.append(self.tableName)
		self.tableFields = self.parseFields(tableNode.getElementsByTagName('field'))
		self.primaryKeys = self.parsePrimaryKeys(tableNode.getElementsByTagName('pk'))
		self.parseForeignKeys(tableNode.getElementsByTagName('fk'))
		self.verifyFieldRefs(tableNode.getElementsByTagName('index'),'index')
		self.verifyFieldRefs(tableNode.getElementsByTagName('col'),'col')
		self.verifyQueries(tableNode.getElementsByTagName('sql'))
	def parseFields(self, fieldNodes):
		fields = []
		for fnode in fieldNodes:
			fieldName = fnode.getAttribute('name')
			fields.append(fieldName)
			fieldType = fnode.getAttribute('type')
			defaultValue = fnode.getAttribute('default')
			if defaultValue != '':
				if fieldType=='string' or fieldType=='char':
					if not defaultValue.startswith("'") or not defaultValue.endswith("'"):
						print '---Error: default value not quoted in field "%s" of table "%s"'%(fieldName,self.tableName)
				elif defaultValue.startswith('nextval') or defaultValue.startswith('NEXTVAL'):
					x1 = defaultValue.find("'")
					x2 = defaultValue.rfind("'")
					sname = defaultValue[x1+1:x2]
					if not sname in sequences:
						print '---Error: sequence %s int table %s not found!'%(sname,self.tableName)
		return fields
	def parsePrimaryKeys(self, pkNodes):
		pkeys = []
		for pknode in pkNodes:
			fieldName = pknode.getAttribute('field')
			pkeys.append(fieldName)
			if not fieldName in self.tableFields:
				print '---Error: wrong primary key field "%s" in table "%s"'%(fieldName,self.tableName)
		return pkeys
	def parseForeignKeys(self, fkNodes):
		self.foreignTables = []
		self.foreignFields = []
		for fknode in fkNodes:
			fieldName = fknode.getAttribute('field')
			if not fieldName in self.tableFields:
				print '---Error: wrong foreign key field "%s" in table "%s"'%(fieldName,self.tableName)
			self.foreignTables.append(fknode.getAttribute('ref-table'))
			self.foreignFields.append(fknode.getAttribute('ref-field'))
	def verifyFieldRefs(self,nodes,tagName):
		for node in nodes:
			fname = node.getAttribute('field')
			if not fname in self.tableFields:
				print '---Error: <%s field="%s">, field not found in table "%s"'%(tagName,fname,self.tableName)
	def verifyQueries(self,sqlNodes):
		for snode in sqlNodes:
			sql = snode.firstChild.nodeValue
			fields = pfields.findall(sql)
			for fname in fields:
				if not fname in self.tableFields:
					print '---Warning: "%s" not defined in table "%s" in query "%s"'%(fname,self.tableName,sql)

def validateDependencies():
	for tc in tableClasses:
		idx = 0
		for ft in tc.foreignTables:
			found = False
			for ftc in tableClasses:
				if ftc.tableName==ft:
					ff = tc.foreignFields[idx]
					idx += 1
					if not ff in ftc.primaryKeys:
						print '---Error in table "%s": ref-field "%s" not a PK in ref-table "%s"'%(tc.tableName,ff,ft)
					elif len(ftc.primaryKeys)>1:
						print '---Error in table "%s": ref-field "%s" not a UNIQUE PK in ref-table "%s"'%(tc.tableName,ff,ft)
					found = True
			if not found:
				print '---Error: ref-table "%s" in table "%s" not defined'%(ft,t.tableName)

def validateQueryTables(sqlNodes):
	for snode in sqlNodes:
		sql = snode.firstChild.nodeValue
		tables = ptables1.findall(sql)
		if len(tables)>0:
			if not tables[0] in tableNames:
				print '---Error: "%s" not defined in %s'%(tables[0],sql)
		else:
			tables = ptables2.findall(sql)
			if len(tables)>0:
				for tn in tables:
					if not tn in tableNames:
						print '---Error: "%s" not defined in %s'%(tn,sql)
			else:
				print '---Warning: check %s'%sql

def findTable(ft):
	for t in tableClasses:
		if t.tableName == ft:
			return t
	return None

orderedTables = []

def checkTable(table):
	for ft in table.foreignTables:
		ftable = findTable(ft)
		if ftable == None:
			print '---Error: ref-table "%s" not found'%ft
			continue
		checkTable(ftable)
		if not ft in orderedTables:
			orderedTables.append(ft)
	tn = table.tableName
	if not tn in orderedTables:
		orderedTables.append(tn)

def generatedrops():
	for tc in tableClasses:
		checkTable(tc)

def pickSequences(nodes):
	global sequences
	for node in nodes:
		sname = node.getAttribute('name')
		sequences.append(sname)

# main function
tableDom = parse('tables.xml') # parse file tables.XML

pickSequences(tableDom.getElementsByTagName('sequence'))

tables = tableDom.getElementsByTagName('table')
for table in tables:
	t = Table(table)
	tableClasses.append(t)

validateDependencies()
validateQueryTables(tableDom.getElementsByTagName('sql'))

generatedrops()
n = len(orderedTables)-1
while n >= 0:
	ot = orderedTables[n]
	print 'DROP TABLE %s;'%ot
	n -= 1
for s in sequences:
	print 'DROP SEQUENCE %s;'%s
