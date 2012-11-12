# python to generate Java data class based on tables.xml
# output: .java files in specified subdirectory

import sys, os, string, time, re
from xml.dom.minidom import parse

#to be used by classes to implement Serializable interface
serializableuid = 100

#def tabs(indent):
#	return ''.join(['\t' for x in range(indent)])
tab = ['','\t','\t\t','\t\t\t','\t\t\t\t','\t\t\t\t\t']

imports = ['java.util.logging.Logger',\
	'java.sql.Connection',\
	'java.sql.SQLException',\
	'java.sql.PreparedStatement',\
	'java.sql.ResultSet']

def addImportClass(importclass):
	if not importclass in imports:
		imports.append(importclass)

#change from tagged type to Java type
def toJavaType(typ):
	if typ=='char' or typ=='string':
		return 'String'
	elif typ=='date':
		return 'java.sql.Date'
	elif typ=='timestamp':
		return 'java.sql.Time'
	elif typ=='auto' or typ=='auto4':
		return 'int'
	elif typ=='auto8':
		return 'long'
	elif typ=='text':
		return 'String'
	elif typ=='blob':
		return 'java.sql.Blob'
	elif typ=='smallint':
		return 'short'
	else:
		return typ

#change java type to SQL single word type with capitalization
def sqlType(javatype):
	if javatype.find('.')>0:
		return javatype[javatype.rfind('.')+1:].capitalize()
	elif javatype=='smallint':
		return 'Short'
	else:
		return javatype.capitalize()

#write java-style comments to the file, comments can have more lines, extras is a [] of lines
def writeComments(fo,_tab,comments,extras):
	fo.write('%s/**\n'%_tab)
	lines = comments.split('\n')
	for line in lines:
		fo.write('%s * %s\n'%(_tab,line.strip()))
	fo.write('%s *\n'%_tab)
	for extra in extras:
		fo.write('%s * %s\n'%(_tab,extra))
	fo.write('%s */\n'%_tab)

def writeConnectionTest(fo):
	fo.write('%sif (this.con_ == null)\n%s{\n'%(tab[2],tab[2]))
	fo.write('%slog_.severe("Connetion object null");\n'%tab[3])
	fo.write('%sthrow new SQLException("Connection object not set.");\n'%tab[3])
	fo.write('%s}\n'%tab[2])

class TableField:
	"""
		parse a <field> element to make a field struct (fieldType,fieldName,javaType,varName,parmName,comment).
		TableField.varName is used for member variable in the java class, it has a suffix '_'.
		TableField.parmName is used for parameter in java methods, it has a prefix '_'.
		The <field> element schema:
		<field name="F_Username" type="string" size="16">comment</field>
	"""
	def __init__(self,node):
		self.fieldName = fname = node.getAttribute('name')
		self.fieldType = node.getAttribute('type')
		if (self.fieldName.startswith('F_')):
			fname = self.fieldName[2:]
		self.varName = fname.lower()+'_'
		self.parmName = '_'+fname.lower()
		self.javaType = toJavaType(self.fieldType)
		if (node.hasChildNodes()):
			self.comment = node.childNodes[0].nodeValue.strip()
		else:
			self.comment = ''
	def sqlRsGetType(self):
		if self.javaType.find('.')>0:
			return self.javaType[self.javaType.rfind('.')+1:]
		return self.javaType.capitalize()

def parseTablePrimaryKey(pkNode,tableFields):
	"""
		Parse a <pk> element into a TableField structure.
		<pk field="F_FieldName"/>
	"""
	keyFieldName = pkNode.getAttribute('field')
	for kfield in tableFields:
		if kfield.fieldName==keyFieldName:
			return kfield
	return None

def parseAnnotation(parentNode):
	"""
		Parse <annotation> under a certain node, which can be <table>, <query>, <update.
		For example:
			<table>
			  <annotation>comments here</annotation>
	"""
	annotation = ''
	for node in parentNode.childNodes:
		if node.nodeType == 1:
			if node.nodeName == 'annotation':
				annotation = node.childNodes[0].nodeValue.strip()
				break
	return annotation

def parseParamNode(paramNode):
	"""
		Parse <param type="string" var="name"/> into (type,name) tuple with type converted into java type.
	"""
	pname = paramNode.getAttribute('var')
	ptype = toJavaType(paramNode.getAttribute('type'))
	return (ptype, pname)

class Struct:
	"""
		Parse <struct name=""><var type="" name=""/></struct> into Struct (structName, members(javatype, name) tuple).
	"""
	def __init__(self,structNode):
		self.structName = structNode.getAttribute('name')
		self.parseVars(structNode.getElementsByTagName('var'))
	def parseVars(self,varNodes):
		self.members = []
		for vnode in varNodes:
			vtype = toJavaType(vnode.getAttribute('type'))
			vname = vnode.getAttribute('name')
			self.members.append((vtype,vname))
	def generateStructFile(self,outdir,package,className):
		fo = open(''.join([outdir,'/',self.structName,'.java']),'wt')
		fo.write('//Generated code by table2java.py, do not modify.\n')
		fo.write('package %s;\n\n'%package)
		fo.write('/**\n * Data encapsulation class from struct in database table definition.\n')
		fo.write(' * This class is generated for %s.\n */\n'%className)
		fo.write('public class %s\n{\n'%self.structName)
		properties = []
		methods = []
		for mtype,mname in self.members:
			properties.append('\tprivate %s %s_;\n'%(mtype,mname))
			methods.append('\tpublic %s get%s()\n\t{\n\t\treturn %s_;\n\t}\n\n'%(mtype,mname.capitalize(),mname))
			methods.append('\tpublic void set%s(%s _%s)\n\t{\n\t\t%s_ = _%s;\n\t}\n\n'%(mname.capitalize(),\
				mtype,mname,mname,mname))
		for p in properties:
			fo.write(p)
		fo.write('\n')
		for m in methods:
			fo.write(m)
		fo.write('}\n')
		fo.close()

class TableAccessor:
	"""
		Base class for TableQuery and TableUpdate with shared methods of parsing comment,params and sql,sqlParams,sqlType.
		TableAccessor.name: string, the name of update or query
		TableAccessor.comment: string
		TableAccessor.params: [(type,name)]
		TableAccessor.sql: string of SQL statement with ? updated
		TableAccessor.sqlType: string of 'SQL' or 'SP'
		TableAccessor.sqlParams: [(type,name)] that match each ? in sql statement
	"""
	def __init__(self, quNode):
		self.name = quNode.getAttribute('name')
		self.comment = parseAnnotation(quNode)
		self.parseParams(quNode.getElementsByTagName('param'))
		self.parseSql(quNode.getElementsByTagName('sql'))
	def parseParams(self,paramNodes):
		self.params = []
		for pnode in paramNodes:
			self.params.append(parseParamNode(pnode))
	def parseSql(self,sqlNodes):
		sqlNode = sqlNodes[0]
		if sqlNode.hasAttribute('type'):
			self.sqlType = sqlNode.getAttribute('type').upper()	#can be 'SP'
			if self.sqlType=='SP':
				addImportClass('java.sql.CallableStatement')
		else:
			self.sqlType = 'SQL'
		osql = sqlNode.childNodes[0].nodeValue
		qms = re.findall(r'\?(\d+)',osql)
		self.sqlParams = []
		if qms!=[]:
			self.sql = re.sub(r'\?\d+', '?', osql)
			for qm in qms:
				x = int(qm) - 1
				if x < 0 or x >=len(self.params):
					print 'Error in SQL index: sql="%s"'%osql
				self.sqlParams.append(self.params[x])
		else:
			self.sql = osql
	def useStoredProcedure(self):
		return self.sqlType=='SP';
	def makeParamCommentList(self):
		cmts = []
		for p in self.params:
			cmts.append('@param %s - %s'%(p[1],p[0]))
		return cmts
	def makeParamString(self):
		plist = []
		for p in self.params:
			plist.append('%s %s'%(p[0],p[1]))
			plist.append(', ')
		return ''.join(plist[:-1])

class TableQuery(TableAccessor):
	"""
		Parse a <query> node into TableQuery structure.
		TableQuery.prefix: string to use as prefix of method instead of 'query'
		TableQuery.returnType: string of return type of Java method, like 'Hashtable' of struct
		TableQuery.keyName: variable name (in struct) as the key to Hashtable if necessary
		TableQuery.struct: instance of Struct type containing a struct/class of sub-fields in the table to return.

		A <query> example:
			<query name="Username" prefix="check" return="Hashtable" hashkey="name">
				<annotation>Check whether ...</annotation>
				<params>
					<param type="String" var="uname"/>
				</params>
				<struct name="mystruct">
					<var type="string" name="name"/>
				</struct>
				<sql type="SQL"><![CDATA[select * from T_Users where F_Username=?1]]></sql>
			</query>
	"""
	def __init__(self, queryNode):
		TableAccessor.__init__(self, queryNode)
		self.prefix = queryNode.getAttribute('prefix')
		self.hashKey = queryNode.getAttribute('hashkey')
		self.returnType = queryNode.getAttribute('return')
		if self.returnType=='ArrayList':
			addImportClass('java.util.ArrayList')
		elif self.returnType=='Hashtable':
			addImportClass('java.util.Hashtable')
		elif self.returnType=='':
			if not self.hashKey=='':
				addImportClass('java.util.Hashtable')
			else:
				addImportClass('java.util.ArrayList')
		self.defaultValue = queryNode.getAttribute('default')
		if self.defaultValue=='':
			if self.returnType in ['int','long','short','float','double']:
				self.defaultValue = '0'
			elif self.returnType=='String':
				self.defaultValue = '""'
			else:
				self.defaultValue = 'null'
		self.parseStruct(queryNode.getElementsByTagName('struct'))
	def parseStruct(self,structNodes):
		self.struct = None	#only one if there is
		for snode in structNodes:
			self.struct = Struct(snode)
			break
	def validateReturnType(self,cname):
		"""
			Validate a return type for JDK 1.5.
		"""
		returntype = self.returnType
		if returntype=='':
			if self.hashKey != '':
				returntype = 'Hashtable<%s>'%cname
			elif self.prefix == 'count':
				returntype = 'int'
			else:
				returntype = 'ArrayList<%s>'%cname
		elif returntype=='ArrayList':
			returntype = 'ArrayList<%s>'%cname
		elif returntype=='Hashtable':
			returntype = 'Hashtable<%s>'%cname
		elif returntype=='self':
			returntype = cname
		elif returntype=='string':
			returntype = 'String'
		return returntype
	def makeMethodName(self):
		if self.prefix=='':
			methodname = 'query%s'%self.name
		else:
			methodname = '%s%s'%(self.prefix,self.name)
		return methodname

class TableUpdate(TableAccessor):
	"""
		Parse a <update> node into TableUpdate structure.
		A <update> example:
			<update name="Username">
				<annotation>Check whether ...</annotation>
				<params>
					<param type="String" var="uname"/>
				</params>
				<sql><![CDATA[]]></sql>
			</update>
	"""
	def __init__(self, updateNode):
		TableAccessor.__init__(self, updateNode)
	def makeMethodName(self):
		return self.name

class TableClass:
	"""
		Parse a <table> element into python TableClass structure (tableName,tableComment,tableFields[],tableKeys[],tableQueries,tableUpdates.
		The XML schema:
			<table name="xxx">
			  <annotation>table commant</annotation>
			  <fields>
			    <field name="" type="">field comment</field>
			  </fields>
			  <primary-key>
			    <pk field="fieldname"/>
			  </primary-key>
			  <queries>
			    <query name="">
				</query>
			  </queries>
			  <updates>
			    <update name="">
			    </update>
			  </updates>
			</table>
	"""
	def __init__(self,tableNode):
		self.parseTableName(tableNode.getAttribute('name'))
		self.comment = parseAnnotation(tableNode)
		self.parseTableFields(tableNode.getElementsByTagName('field'))
		self.parseTablePrimaryKeys(tableNode.getElementsByTagName('pk'))
		self.parseTableQueries(tableNode.getElementsByTagName('query'))
		self.parseTableUpdates(tableNode.getElementsByTagName('update'))
	def parseTableName(self,tableName):
		self.tableName = tableName
		if tableName.startswith('T_'):
			self.className = tableName[2:]
	def parseTableFields(self,fieldNodes):
		self.tableFields = []
		for fnode in fieldNodes:
			self.tableFields.append(TableField(fnode))
	def parseTablePrimaryKeys(self,pkNodes):
		self.tableKeys = []
		for knode in pkNodes:
			self.tableKeys.append(parseTablePrimaryKey(knode,self.tableFields))
	def parseTableQueries(self,queryNodes):
		self.tableQueries = []
		for qnode in queryNodes:
			self.tableQueries.append(TableQuery(qnode))
	def parseTableUpdates(self,updateNodes):
		self.tableUpdates = []
		for unode in updateNodes:
			self.tableUpdates.append(TableUpdate(unode))

	def writeImports(self, fo):
		for imps in imports:
			fo.write('import %s;\n'%imps)
		fo.write('\n')

	def writeProperties(self, fo):
		fo.write('%sprivate Connection con_;\n'%(tab[1]))
		for f in self.tableFields:
			fo.write('%sprivate %s %s; ///%s\n'%(tab[1],f.javaType,f.varName,f.comment))
		fo.write('\n')

	def makePrimaryKeyParamString(self):
		plist = []
		for p in self.tableKeys:
			plist.append('%s %s'%(p.javaType,p.parmName))
			plist.append(', ')
		return ''.join(plist[:-1])

	def writeConstructors(self, fo):
		writeComments(fo,tab[1],'Construct an empty %s object.'%self.className,[])
		fo.write('%spublic %s()\n%s{\n%s}\n\n'%(tab[1],self.className,tab[1],tab[1]))
		writeComments(fo,tab[1],'Construct a %s object with a Connection instance.'%self.className,['@param con - Connection object'])
		fo.write('%spublic %s(Connection con)\n%s{\n'%(tab[1],self.className,tab[1]))
		fo.write('%scon_ = con;\n'%tab[2])
		fo.write('%s}\n\n'%tab[1])
		# constructor with primary key for direct query and load
		writeComments(fo,tab[1],'Construct with a query on primary key(s)',[])
		pkparam = self.makePrimaryKeyParamString()
		fo.write('%spublic %s(Connection con, %s) throws SQLException\n%s{\n'%(tab[1],self.className,pkparam,tab[1]))
		s = ''.join([','+p.parmName for p in self.tableKeys])
		fo.write('%scon_ = con;\n'%tab[2])
		fo.write('%squeryByPrimaryKey(%s);\n'%(tab[2],s[1:]))
		fo.write('%s}\n\n'%tab[1])
		# setConnection method
		writeComments(fo,tab[1],'Setter for Connection object',[])
		fo.write('%spublic void setConnection(Connection con)\n%s{\n'%(tab[1],tab[1]))
		fo.write('%sthis.con_ = con;\n%s}\n\n'%(tab[2],tab[1]))

	def writeQueryPrimaryKey(self, fo):
		pkcond,pksets = self.makePrimaryKeySettings()
		pkparam = self.makePrimaryKeyParamString()
		pkps = []
		for pkp in self.tableKeys:
			pkps.append('@param %s - %s'%(pkp.parmName,pkp.javaType))
		writeComments(fo,tab[1],'Query by primary key(s), and populate current instance.',pkps)
		fo.write('%spublic void queryByPrimaryKey(%s) throws SQLException\n%s{\n'%(tab[1],pkparam,tab[1]))
		writeConnectionTest(fo)
		fo.write('%sString sql = "select * from %s where%s";\n'%(tab[2],self.tableName,pkcond))
		fo.write('%sPreparedStatement stmt = null;\n%sResultSet rs = null;\n'%(tab[2],tab[2]))
		fo.write('%stry\n%s{\n'%(tab[2],tab[2]))
		fo.write('%sstmt = con_.prepareStatement(sql);\n'%tab[3])
		i = 1
		for sm in self.tableKeys:
			fo.write('%sstmt.set%s(%i,%s);\n'%(tab[3],sm.sqlRsGetType(),i,sm.parmName))
			i += 1
		fo.write('%srs = stmt.executeQuery();\n'%tab[3])

		fo.write('%sif (rs.next())\n%s{\n'%(tab[3],tab[3]))
		i = 1
		for v in self.tableFields:
			fo.write('%sset%s(rs.get%s(%i));\n'%(tab[4],v.fieldName[2:],v.sqlRsGetType(),i))
			i += 1
		fo.write('%s}\n'%tab[3])
		msg1 = 'log_.severe(e.toString())'
		msg2 = 'throw e'
		fo.write('%s}\n%scatch (SQLException e)\n%s{\n%s%s;\n%s%s;\n%s}\n'%(tab[2],tab[2],tab[2],tab[3],msg1,tab[3],msg2,tab[2]))
		#fo.write('%sfinally\n%s{\n%stry {rs.close();stmt.close();} catch (SQLException x) {}\n%s}\n'%(tab[2],tab[2],tab[3],tab[2]))
		self.writeFinally(fo, 2)
		fo.write('%s}\n\n'%tab[1])
		# no need for a constructor of all field values, use setters

	def writeDefaultDelete(self, fo):
		pkcond,pksets = self.makePrimaryKeySettings()
		pkparam = self.makePrimaryKeyParamString()
		pkps = []
		for pkp in self.tableKeys:
			pkps.append('@param %s - %s'%(pkp.parmName,pkp.javaType))
		writeComments(fo,tab[1],'Delete record by primary key(s).',pkps)
		fo.write('%spublic void delete(%s) throws SQLException\n%s{\n'%(tab[1],pkparam,tab[1]))
		writeConnectionTest(fo)
		fo.write('%sString sql = "delete from %s where%s";\n'%(tab[2],self.tableName,pkcond))
		fo.write('%sPreparedStatement stmt = null;\n'%tab[2])
		fo.write('%stry\n%s{\n'%(tab[2],tab[2]))
		fo.write('%sstmt = con_.prepareStatement(sql);\n'%tab[3])
		i = 1
		for sm in self.tableKeys:
			fo.write('%sstmt.set%s(%i,%s);\n'%(tab[3],sm.sqlRsGetType(),i,sm.parmName))
			i += 1
		fo.write('%sstmt.executeUpdate();\n'%tab[3])
		msg1 = 'log_.severe(e.toString())'
		msg2 = 'throw e'
		fo.write('%s}\n%scatch (SQLException e)\n%s{\n%s%s;\n%s%s;\n%s}\n'%(tab[2],tab[2],tab[2],tab[3],msg1,tab[3],msg2,tab[2]))
		self.writeFinally(fo, 2, False)
		fo.write('%s}\n\n'%tab[1])
		#second delete method, delete current record with a connection
		writeComments(fo,tab[1],'Delete current record by PK with a connection.',[])
		fo.write('%spublic void delete(Connection _con) throws SQLException\n%s{\n'%(tab[1],tab[1]))
		fo.write('%sString sql = "delete from %s where%s";\n'%(tab[2],self.tableName,pkcond))
		fo.write('%sPreparedStatement stmt = null;\n'%tab[2])
		fo.write('%stry\n%s{\n'%(tab[2],tab[2]))
		fo.write('%sstmt = con_.prepareStatement(sql);\n'%tab[3])
		i = 1
		for sm in self.tableKeys:
			fo.write('%sstmt.set%s(%i,%s);\n'%(tab[3],sm.sqlRsGetType(),i,sm.varName))
			i += 1
		fo.write('%sstmt.executeUpdate();\n'%tab[3])
		msg1 = 'log_.severe(e.toString())'
		msg2 = 'throw e'
		fo.write('%s}\n%scatch (SQLException e)\n%s{\n%s%s;\n%s%s;\n%s}\n'%(tab[2],tab[2],tab[2],tab[3],msg1,tab[3],msg2,tab[2]))
		self.writeFinally(fo, 2, False)
		fo.write('%s}\n\n'%tab[1])

	def writeAccessors(self, fo):
		i = 0
		for f in self.tableFields:
			writeComments(fo,tab[1],'Getter for %s'%f.fieldName,[])
			fo.write('%spublic %s get%s()\n%s{\n%sreturn %s;\n%s}\n\n'%(tab[1],f.javaType,f.fieldName[2:],tab[1],tab[2],f.varName,tab[1]))
			writeComments(fo,tab[1],'Setter for %s'%f.fieldName,[])
			fo.write('%spublic void set%s(%s %s)\n%s{\n'%(tab[1],f.fieldName[2:],f.javaType,f.parmName,tab[1]))
			fo.write('%s%s = %s;\n%ssetreg_[%s] = true;\n%s}\n\n'%(tab[2],f.varName,f.parmName,tab[2],i,tab[1]))
			i += 1
		#clear all update tags
		writeComments(fo,tab[1],'Clear all update tags',[])
		fo.write('%spublic void clearUpdates()\n%s{\n'%(tab[1],tab[1]))
		fo.write('%sfor (int i=0; i<setreg_.length; i++)\n%s{\n'%(tab[2],tab[2]))
		fo.write('%ssetreg_[i] = false;\n%s}\n%s}\n\n'%(tab[3],tab[2],tab[1]))

	def writeQueries(self, fo):
		"""
			Generate a query method.
			return type can be: ArrayList of class or struct, Hashtable of class or struct, self (class or struct), or primitive types
		"""
		for q in self.tableQueries:
			writeComments(fo,tab[1],q.comment,q.makeParamCommentList())
			if q.struct != None:
				cname = q.struct.structName
			else:
				cname = self.className
			returntype = q.validateReturnType(cname)
			methodname = q.makeMethodName()
			paramstr = q.makeParamString()
			fo.write('%spublic %s %s(%s) throws SQLException\n%s{\n'%(tab[1],returntype,methodname,paramstr,tab[1]))
			writeConnectionTest(fo)
			fo.write('%sString sql = "%s";\n'%(tab[2],q.sql))
			if returntype==cname:
				if cname==self.className:
					fo.write('%s%s result = this;\t//populate this instance\n'%(tab[2],returntype))
				else:
					fo.write('%s%s result = new %s();\n'%(tab[2],returntype,returntype))
			elif returntype.endswith('>'):
				fo.write('%s%s result = new %s();\n'%(tab[2],returntype,returntype))
			else:
				fo.write('%s%s result = %s;\n'%(tab[2],returntype,q.defaultValue))

			statementParam = 'sql'
			queryParam = ''
			if q.sqlType=='SQL':
				if q.sql.find('?')>0:
					statementType = 'PreparedStatement'
					statementMethod = 'prepareStatement'
				else:
					statementType = 'java.sql.Statement'
					statementMethod = 'createStatement'
					statementParam = ''
					queryParam = 'sql'
			else:
				statementType = 'CallableStatement'
				statementMethod = 'prepareCall'

			fo.write('%s%s stmt = null;\n'%(tab[2],statementType))
			fo.write('%sResultSet rs = null;\n'%tab[2])
			fo.write('%stry\n%s{\n'%(tab[2],tab[2]))
			fo.write('%sstmt = con_.%s(%s);\n'%(tab[3],statementMethod,statementParam))
			i = 1
			for sm in q.sqlParams:
				fo.write('%sstmt.set%s(%i,%s);\n'%(tab[3],sm[0].capitalize(),i,sm[1]))
				i += 1
			fo.write('%srs = stmt.executeQuery(%s);\n'%(tab[3],queryParam))

			if returntype.endswith('>'):
				#ArrayList<..> or Hashtable<..>
				fo.write('%swhile (rs.next())\n%s{\n'%(tab[3],tab[3]))
				fo.write('%s%s s = new %s();\n'%(tab[4],cname,cname))
			else:
				fo.write('%sif (rs.next())\n%s{\n'%(tab[3],tab[3]))

			if returntype.endswith('>') or returntype==cname:
				if returntype.endswith('>'):
					vname = 's'
				else:
					vname = 'result'
				if returntype.find(self.className)>=0:
					i = 1
					for v in self.tableFields:
						fo.write('%s%s.set%s(rs.get%s(%i));\n'%(tab[4],vname,v.fieldName[2:],v.sqlRsGetType(),i))
						i += 1
				else:
					i = 1
					for v in q.struct.members:
						fo.write('%s%s.set%s(rs.get%s(%i));\n'%(tab[4],vname,v[1].capitalize(),sqlType(v[0]),i))
						i += 1
			else:
				fo.write('%sresult = rs.get%s(1);\n'%(tab[4],returntype.capitalize()))

			if returntype.startswith('ArrayList'):
				fo.write('%sresult.add(s);\n'%tab[4])
			elif returntype.startswith('Hashtable'):
				fo.write('%sresult.put(s.%s, s);\n'%(tab[4],q.hashKey))
			fo.write('%s}\n'%tab[3])

			fo.write('%s}\n%scatch (SQLException e)\n%s{\n%slog_.severe(e.toString());\n%s}\n'%(tab[2],tab[2],tab[2],tab[3],tab[2]))
			#fo.write('%sfinally\n%s{\n%stry {rs.close();stmt.close();} catch (SQLException x) {}\n%s}\n'%(tab[2],tab[2],tab[3],tab[2]))
			self.writeFinally(fo, 2)
			fo.write('%sreturn result;\n%s}\n\n'%(tab[2],tab[1]))

	def writeUpdates(self, fo):
		for u in self.tableUpdates:
			writeComments(fo,tab[1],u.comment,u.makeParamCommentList())
			methodname = u.makeMethodName()
			paramstr = u.makeParamString()
			fo.write('%spublic void %s(%s) throws SQLException\n%s{\n'%(tab[1],methodname,paramstr,tab[1]))
			writeConnectionTest(fo)
			fo.write('%sString sql = "%s";\n'%(tab[2],u.sql))
			if u.sqlType=='SQL':
				statementType = 'PreparedStatement'
				statementMethod = 'prepareStatement'
			else:
				statementType = 'CallableStatement'
				statementMethod = 'prepareCall'

			fo.write('%s%s stmt = null;\n'%(tab[2],statementType))
			fo.write('%stry\n%s{\n'%(tab[2],tab[2]))
			fo.write('%sstmt = con_.%s(sql);\n'%(tab[3],statementMethod))
			i = 1
			for sm in u.sqlParams:
				fo.write('%sstmt.set%s(%i,%s);\n'%(tab[3],sm[0].capitalize(),i,sm[1]))
				i += 1
			fo.write('%sstmt.executeUpdate();\n'%tab[3])
			fo.write('%s}\n%scatch (SQLException e)\n%s{\n%slog_.severe(e.toString());\n%s}\n'%(tab[2],tab[2],tab[2],tab[3],tab[2]))
			#fo.write('%sfinally\n%s{\n%stry {rs.close();stmt.close();} catch (SQLException x) {}\n%s}\n'%(tab[2],tab[2],tab[3],tab[2]))
			self.writeFinally(fo, 2, False)
			fo.write('%s}\n\n'%tab[1])

	def writeFinally(self, fo, idt, hasrs=True):
		fo.write('%sfinally\n%s{\n%stry {\n'%(tab[idt],tab[idt],tab[idt+1]))
		if hasrs:
			fo.write('%sif (rs != null) rs.close();\n'%tab[idt+2])
		fo.write('%sif (stmt != null) stmt.close();\n'%tab[idt+2])
		fo.write('%s}\n%scatch (SQLException x)\n%s{\n%s}\n'%(tab[idt+1],tab[idt+1],tab[idt+1],tab[idt+1]))
		fo.write('%s}\n'%tab[idt])

	def makePrimaryKeySettings(self):
		pks = []
		pksets = []
		i = 1
		for pk in self.tableKeys:
			pks.append(' AND')
			pks.append(' %s=?'%pk.fieldName)
			pksets.append('stmt.set%s(%i,%s);'%(pk.sqlRsGetType(),i,pk.varName))
			i += 1
		return (''.join(pks[1:]),pksets)

	def writeInsertMethod(self, fo):
		writeComments(fo,tab[1],'Insert record in table with current data object.',[])
		fo.write('%spublic void insert(Connection con) throws SQLException\n%s{\n'%(tab[1],tab[1]))
		tf = self.tableFields[0]
		fo.write('%sString sql = "select * from %s where %s=?";\n'%(tab[2],self.tableName,tf.fieldName))
		fo.write('%sPreparedStatement stmt = null;\n%sResultSet rs = null;\n'%(tab[2],tab[2]))
		fo.write('%stry\n%s{\n'%(tab[2],tab[2]))
		st = 'ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE'
		fo.write('%sstmt = con.prepareStatement(sql,%s);\n'%(tab[3],st))
		fo.write('%sstmt.set%s(1, %s);\n'%(tab[3],tf.sqlRsGetType(),tf.varName))
		fo.write('%sstmt.setFetchSize(1);\n'%tab[3])
		fo.write('%srs = stmt.executeQuery();\n'%tab[3])
		fo.write('%srs.moveToInsertRow();\n'%tab[3])
		i = 1
		for v in self.tableFields:
			if v.fieldType!='auto':
				fo.write('%sif (setreg_[%s]) rs.update%s(%i,%s);\n'%(tab[3],i-1,v.sqlRsGetType(),i,v.varName))
			else:
				fo.write('%s//%s is an auto field\n'%(tab[3],v.varName))
			i += 1
		fo.write('%srs.insertRow();\n'%tab[3])
		msg1 = 'log_.severe(e.toString())'
		msg2 = 'throw e'
		fo.write('%s}\n%scatch (SQLException e)\n%s{\n%s%s;\n%s%s;\n%s}\n'%(tab[2],tab[2],tab[2],tab[3],msg1,tab[3],msg2,tab[2]))
		#fo.write('%sfinally\n%s{\n%stry {rs.close();stmt.close();} catch (SQLException x) {}\n%s}\n'%(tab[2],tab[2],tab[3],tab[2]))
		self.writeFinally(fo, 2)
		fo.write('%s}\n\n'%tab[1])
		fo.write('%spublic void insert() throws SQLException\n%s{\n'%(tab[1],tab[1]))
		writeConnectionTest(fo)
		fo.write('%sinsert(this.con_);\n%s}\n\n'%(tab[2],tab[1]))

	def writeUpdateMethod(self, fo):
		cmt = 'Update record in table with current data object.\nAll fields must be set, better query in first.'
		writeComments(fo,tab[1],cmt,[])
		fo.write('%spublic void update(Connection con) throws SQLException\n%s{\n'%(tab[1],tab[1]))
		fo.write('%sString sql = "select * from %s'%(tab[2],self.tableName))
		pkcond,pksets = self.makePrimaryKeySettings()
		fo.write(' where%s";\n'%pkcond)
		fo.write('%sPreparedStatement stmt = null;\n%sResultSet rs = null;\n'%(tab[2],tab[2]))
		fo.write('%stry\n%s{\n'%(tab[2],tab[2]))
		st = 'ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE'
		fo.write('%sstmt = con.prepareStatement(sql,%s);\n'%(tab[3],st))
		tf = self.tableFields[0]
		fo.write('%sstmt.set%s(1, %s);\n'%(tab[3],tf.sqlRsGetType(),tf.varName))
		fo.write('%srs = stmt.executeQuery();\n'%tab[3])
		fo.write('%sif (rs.next())\n%s{\n'%(tab[3],tab[3]))
		i = 1
		for f in self.tableFields:
			if pkcond.find(f.fieldName)<0:
				fo.write('%sif (setreg_[%s]) rs.update%s(%i,%s);\n'%(tab[4],i-1,f.sqlRsGetType(),i,f.varName))
			i += 1
		fo.write('%srs.updateRow();\n'%tab[4])
		fo.write('%s}\n'%tab[3])
		msg1 = 'log_.severe(e.toString())'
		msg2 = 'throw e'
		fo.write('%s}\n%scatch (SQLException e)\n%s{\n%s%s;\n%s%s;\n%s}\n'%(tab[2],tab[2],tab[2],tab[3],msg1,tab[3],msg2,tab[2]))
		#fo.write('%sfinally\n%s{\n%stry {rs.close();stmt.close();} catch (SQLException x) {}\n%s}\n'%(tab[2],tab[2],tab[3],tab[2]))
		self.writeFinally(fo, 2)
		fo.write('%s}\n\n'%tab[1])
		fo.write('%spublic void update() throws SQLException\n%s{\n'%(tab[1],tab[1]))
		writeConnectionTest(fo)
		fo.write('%supdate(this.con_);\n%s}\n\n'%(tab[2],tab[1]))

	def writeMethods(self, fo):
		self.writeConstructors(fo)
		self.writeAccessors(fo)
		self.writeQueryPrimaryKey(fo)
		self.writeQueries(fo)
		self.writeInsertMethod(fo)
		self.writeUpdateMethod(fo)
		self.writeUpdates(fo)
		self.writeDefaultDelete(fo)

	def writeClass(self, fo):
		global serializableuid
		fo.write('public class %s implements java.io.Serializable\n{\n'%self.className)
		fo.write('%sprotected static final long serialVersionUID = %dL;\n'%(tab[1],serializableuid))
		serializableuid += 1
		fo.write('%sprivate Logger log_ = Logger.getLogger(%s.class.getName());\n\n'%(tab[1],self.className))
		fo.write('%sprivate boolean[] setreg_ = new boolean[%s];\n\n'%(tab[1],len(self.tableFields)))
		self.writeProperties(fo)
		self.writeMethods(fo)
		fo.write('}\n')

	def generateJavaCode(self,outdir,package):
		javaFilename = ''.join([outdir,'/',self.className,'.java'])
		fo = open(javaFilename, 'wt')
		fo.write('//Generated code by table2java.py, do not modify.\n')
		fo.write('package %s;\n\n'%package)
		self.writeImports(fo)
		writeComments(fo,'',self.comment,['@version 0.1','@author Ted Wen','@date Jan.2007'])
		self.writeClass(fo)
		fo.close()
		for q in self.tableQueries:
			if q.struct != None:
				q.struct.generateStructFile(outdir,package,self.className)

# main function
outdir = '../com/transmem/data/db'
package = 'com.transmem.data.db'
tableDom = parse('tables.xml') # parse file tables.XML
tables = tableDom.getElementsByTagName('table')
for table in tables:
	tabclass = TableClass(table)
	tabclass.generateJavaCode(outdir,package)

#fout = open(outdir+'\\DataObject.cs','wt')
#fout.write(baseClass);
#fout.close()

#files = os.listdir('.')
#if len(files)<1:
#	print "No files found in this folder"
#	sys.exit(0)
#else:
#	for fname in files:
#		if fname.endswith('.sql'):
#			parsefile(fname)
