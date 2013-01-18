<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" 
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:java="http://acme.com/java/com.acme.fppdf.transformer.XsltMethods"
    xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions">

    <xsl:variable name="lastModified">
        <xsl:for-each select="//node/@MODIFIED">
            <xsl:sort data-type="number" order="descending" />
            <xsl:if test="position()=1">
                <xsl:value-of select="." />
            </xsl:if>
        </xsl:for-each>
    </xsl:variable>

	<xsl:variable name="page-height">
		<xsl:choose>
			<xsl:when test="$orientation = 'LANDSCAPE'">210mm</xsl:when>
			<xsl:otherwise>297mm</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="page-width">
		<xsl:choose>
			<xsl:when test="$orientation = 'LANDSCAPE'">297mm</xsl:when>
			<xsl:otherwise>210mm</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

    <xsl:template name="page-layout">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="PageMaster"
                        margin-left="25mm" 
                        margin-right="25mm" 
                        margin-top="15mm"
                        margin-bottom="25mm">
                    <xsl:attribute name="page-height"><xsl:value-of select="$page-height" /></xsl:attribute>
                    <xsl:attribute name="page-width"><xsl:value-of select="$page-width" /></xsl:attribute>
                    <fo:region-body margin-top="10mm" margin-bottom="10mm" />
                    <fo:region-before extent="10mm" display-align="before" />
                    <fo:region-after extent="10mm" display-align="after" />
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="PageMaster">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block font-size="6pt" text-align-last="justify" font-family="Arial">
                        <xsl:text>Generated from file: </xsl:text>
                        <xsl:value-of select="$fileName" />
                        <fo:leader leader-pattern="space" />
                        <xsl:text>Last modified: </xsl:text>
                        <xsl:value-of select='java:formatTimestamp($lastModified)' />
                    </fo:block>
                </fo:static-content>


                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="8pt" text-align-last="center" font-family="Arial">
                        <fo:page-number/>
                    </fo:block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body">
                    <xsl:attribute name="font-family"> Arial</xsl:attribute>
                    <xsl:attribute name="font-size"><xsl:value-of select="12*$sizeFactor" /></xsl:attribute>
                    <xsl:apply-templates select="map/node" />
                </fo:flow>
                 

            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
