<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="text" encoding="UTF-8" />

	<xsl:template match="/database">
		<xsl:apply-templates select="tables" />
	</xsl:template>

	<xsl:template match="tables">
		<xsl:apply-templates select="*" />
	</xsl:template>
	
	<xsl:template match="sequence">
CREATE SEQUENCE <xsl:value-of select="@name" />
	<xsl:if test="@increment"> INCREMENT <xsl:value-of select="@increment"/></xsl:if>
	<xsl:if test="@minvalue"> MINVALUE <xsl:value-of select="@minvalue"/></xsl:if>
	<xsl:if test="@maxvalue"> MAXVALUE <xsl:value-of select="@maxvalue"/></xsl:if>
	<xsl:if test="@start"> START <xsl:value-of select="@start"/></xsl:if>
	<xsl:if test="@cycle"> CYCLE</xsl:if>;<xsl:text>
</xsl:text>
	<xsl:for-each select="users/user">
		<xsl:text>GRANT </xsl:text>
		<xsl:value-of select="@right"/>
		<xsl:text> ON SEQUENCE </xsl:text>
		<xsl:value-of select="../../@name"/>
		<xsl:text> TO </xsl:text>
		<xsl:if test="@type">GROUP "</xsl:if>
		<xsl:value-of select="@name"/>
		<xsl:if test="@type">"</xsl:if>
		<xsl:text>;
</xsl:text>
	</xsl:for-each>
	</xsl:template>

	<xsl:template match="table">
		<xsl:apply-templates select="annotation" />
CREATE TABLE <xsl:value-of select="@name" /><xsl:text>
(
</xsl:text>
		<xsl:apply-templates select="fields" />
		<xsl:apply-templates select="primary-key" />
		<xsl:if test="./foreign-keys"><xsl:text>,
</xsl:text>
    <xsl:apply-templates select="foreign-keys" />
    </xsl:if>
		<xsl:if test="./constraints"><xsl:text>,
</xsl:text>
		<xsl:apply-templates select="constraints" />
		</xsl:if>
		<xsl:text>
);
</xsl:text>
		<xsl:apply-templates select="indices" />
		<xsl:apply-templates select="users" />
		<xsl:apply-templates select="data" />
	</xsl:template>

	<xsl:template match="users">
		<xsl:for-each select="user">
			<xsl:text>GRANT </xsl:text>
			<xsl:value-of select="@right"/>
			<xsl:text> ON TABLE </xsl:text>
			<xsl:value-of select="../../@name"/>
			<xsl:text> TO </xsl:text>
			<xsl:if test="@type">GROUP "</xsl:if>
			<xsl:value-of select="@name"/>
			<xsl:if test="@type">"</xsl:if>
			<xsl:text>;
</xsl:text>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="indices">
		<xsl:for-each select="index">
			<xsl:text>CREATE</xsl:text>
			<xsl:if test="@unique"> UNIQUE</xsl:if>
			<xsl:text> INDEX </xsl:text>
			<xsl:value-of select="@name"/> ON <xsl:value-of select="../../@name"/> (<xsl:value-of select="@field"/><xsl:text>);
</xsl:text>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="queries" />
	<xsl:template match="updates" />
	
	<xsl:template match="row">
		<xsl:text>INSERT INTO </xsl:text>
		<xsl:value-of select="../../@name"/>
		<xsl:text>(</xsl:text>
		<xsl:for-each select="col">
			<xsl:if test="position()>1"><xsl:text>,</xsl:text></xsl:if>
			<xsl:value-of select="@field"/>
		</xsl:for-each>
		<xsl:text>) VALUES(</xsl:text>
		<xsl:for-each select="col">
			<xsl:if test="position()>1"><xsl:text>,</xsl:text></xsl:if>
			<xsl:variable name="fvar"><xsl:value-of select="@field"/></xsl:variable>
			<xsl:choose>
				<xsl:when test="../../../fields/field[@name = $fvar]/@type='char'">
					<xsl:text>'</xsl:text><xsl:value-of select="."/><xsl:text>'</xsl:text>
				</xsl:when>
				<xsl:when test="../../../fields/field[@name = $fvar]/@type='string'">
					<xsl:text>'</xsl:text><xsl:value-of select="."/><xsl:text>'</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="."/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:text>);
</xsl:text>
	</xsl:template>

	<xsl:template match="data">
		<xsl:apply-templates select="row"/>
	</xsl:template>

	<xsl:template name="change-type">
		<xsl:param name="fieldtype" />
		<xsl:choose>
		<xsl:when test="$fieldtype='byte'">int2</xsl:when>
		<xsl:when test="$fieldtype='short'">int2</xsl:when>
		<xsl:when test="$fieldtype='int'">int4</xsl:when>
		<xsl:when test="$fieldtype='long'">int8</xsl:when>
		<xsl:when test="$fieldtype='char'">CHAR</xsl:when>
		<xsl:when test="$fieldtype='string'">VARCHAR</xsl:when>
		<xsl:when test="$fieldtype='date'">TIMESTAMP</xsl:when>
		<xsl:when test="$fieldtype='timestamp'">TIMESTAMP</xsl:when>
		<xsl:when test="$fieldtype='float'">float4</xsl:when>
		<xsl:when test="$fieldtype='double'">float8</xsl:when>
		<xsl:when test="$fieldtype='auto'">SERIAL</xsl:when>
		<xsl:when test="$fieldtype='auto4'">SERIAL</xsl:when>
		<xsl:when test="$fieldtype='auto8'">BIGSERIAL</xsl:when>
		<xsl:when test="$fieldtype='text'">TEXT</xsl:when>
 <!--
		<xsl:when test="$fieldtype='clob8'">TINYTEXT</xsl:when>
		<xsl:when test="$fieldtype='clob16'">TEXT</xsl:when>
		<xsl:when test="$fieldtype='clob24'">MEDIUMTEXT</xsl:when>
		<xsl:when test="$fieldtype='clob32'">LONGTEXT</xsl:when>
		<xsl:when test="$fieldtype='blob8'">TINYBLOB</xsl:when>
		<xsl:when test="$fieldtype='blob16'">BLOB</xsl:when>
		<xsl:when test="$fieldtype='blob24'">MEDIUMBLOB</xsl:when>
		<xsl:when test="$fieldtype='blob32'">LONGBLOB</xsl:when>
 -->
		<xsl:otherwise>TEXT</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="field">
		<xsl:text>	</xsl:text>
		<xsl:value-of select="@name"/>
		<xsl:text>		</xsl:text>
		<xsl:call-template name="change-type">
			<xsl:with-param name="fieldtype"><xsl:value-of select="@type"/></xsl:with-param>
		</xsl:call-template>
		<xsl:if test="@size">(<xsl:value-of select="@size"/>)</xsl:if>
		<xsl:if test="@auto"><xsl:text>	</xsl:text>SERIAL</xsl:if>
		<xsl:if test="@null='false'"><xsl:text>	</xsl:text>NOT NULL</xsl:if>
		<xsl:if test="@unique"><xsl:text>	</xsl:text>UNIQUE</xsl:if>
		<xsl:if test="@default">
      <xsl:text> DEFAULT </xsl:text>
      <xsl:value-of select="@default"/>
    </xsl:if>
		<xsl:text>,
</xsl:text>
	</xsl:template>

	<xsl:template match="fields">
		<xsl:apply-templates select="field" />
	</xsl:template>

	<xsl:template match="annotation">
<!--
		<xsl:text>/*
</xsl:text>
		<xsl:value-of select="." />
		<xsl:text> */
</xsl:text>
-->
	</xsl:template>

	<xsl:template match="primary-key">
		<xsl:text>	PRIMARY KEY (</xsl:text>
		<xsl:for-each select="pk">
			<xsl:if test="position()>1"><xsl:text>,</xsl:text></xsl:if>
			<xsl:value-of select="@field" />
		</xsl:for-each>
		<xsl:text>)</xsl:text>
	</xsl:template>

	<xsl:template match="foreign-keys">
		<xsl:for-each select="fk">
			<xsl:if test="position()>1"><xsl:text>,
</xsl:text></xsl:if>
		<xsl:text>	FOREIGN KEY (</xsl:text>
		<xsl:value-of select="@field"/>
		<xsl:text>) REFERENCES </xsl:text>
		<xsl:value-of select="@ref-table"/>
		<xsl:text> (</xsl:text>
		<xsl:value-of select="@ref-field"/>
		<xsl:text>)</xsl:text>
		<xsl:if test="@ondelete"><xsl:text> ON DELETE CASCADE</xsl:text></xsl:if>
		</xsl:for-each>
	</xsl:template>

  <xsl:template match="constraints">
    <xsl:for-each select="check">
      <xsl:if test="position()>1"><xsl:text>,
</xsl:text></xsl:if>
      <xsl:text>  CONSTRAINT </xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text> CHECK (</xsl:text>
      <xsl:value-of select="."/>
      <xsl:text>)
</xsl:text>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
