<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="text" encoding="UTF-8"/>
<xsl:template match="/terms">
	<xsl:apply-templates select="term"/>
</xsl:template>
<xsl:template match="term">
INSERT INTO T_Domains VALUES('<xsl:value-of select="@code"/>','<xsl:value-of select="phrase[@lang='EN']"/>',null);
</xsl:template>
</xsl:stylesheet>
