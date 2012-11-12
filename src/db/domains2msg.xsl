<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="text" encoding="GBK"/>
<xsl:template match="/terms">
	<xsl:apply-templates select="term"/>
</xsl:template>
<xsl:template match="term">
<xsl:text>domain.</xsl:text><xsl:value-of select="@code"/>
<xsl:text>=</xsl:text><xsl:value-of select="phrase[@lang='ZH']"/><xsl:text>
</xsl:text>
</xsl:template>
</xsl:stylesheet>
