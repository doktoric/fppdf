<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions"
    xmlns:date="http://exslt.org/dates-and-times" xmlns:xs="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:java="http://acme.com/java/com.acme.fppdf.transformer.XsltMethods"
    xs:schemaLocation="http://www.w3.org/1999/XSL/Format http://svn.apache.org/viewvc/xmlgraphics/fop/trunk/src/foschema/fop.xsd?view=co">

    <xsl:attribute-set name="list.item">
        <xsl:attribute name="space-before">0.4em</xsl:attribute>
        <xsl:attribute name="space-after">0.4em</xsl:attribute>
        <xsl:attribute name="relative-align">inherit</xsl:attribute>
    </xsl:attribute-set>

    <xsl:param name="fileName" />
    <xsl:param name="orientation" />
    <xsl:param name="sizeFactor" />

    <xsl:include href="page-layout.xsl" />
    <xsl:include href="richcontent.xsl" />

    <xsl:template match="/">
        <xsl:call-template name="page-layout" />
    </xsl:template>

    <xsl:template match="node">
        <fo:list-block provisional-distance-between-starts="2em">
            <fo:list-item xsl:use-attribute-sets="list.item">
                <xsl:call-template name="bullet" />
                <fo:list-item-body start-indent="body-start()" text-align="justify">

                    <xsl:choose>
                        <xsl:when test="@TEXT!='' or @LOCALIZED_TEXT!=''">
                            <fo:block>
                                <xsl:apply-templates select="font" mode="nodestyle" />
                                <xsl:call-template name="isIcon">
                                    <xsl:with-param name="path">
                                        <xsl:value-of select="icon/@BUILTIN" />
                                    </xsl:with-param>
                                </xsl:call-template>
                                <xsl:value-of select="@TEXT" />
                                <xsl:value-of select="@LOCALIZED_TEXT" />
                            </fo:block>
                            <xsl:if test="count(child::richcontent) &gt; 0">
                                <fo:block>
                                    <fo:leader />
                                </fo:block>
                            </xsl:if>
                        </xsl:when>
                    </xsl:choose>
                    <fo:block>
                        <xsl:call-template name="linker">
                            <xsl:with-param name="link" select="@LINK" />
                        </xsl:call-template>
                        <xsl:apply-templates select="hook[@NAME='ExternalObject']" />
                    </fo:block>
                    <xsl:call-template name="process-richcontent" />
                    <xsl:apply-templates select="node" />
                </fo:list-item-body>
            </fo:list-item>
        </fo:list-block>
    </xsl:template>

    <xsl:template name="linker">
        <xsl:param name="link" />
        <xsl:choose>
            <xsl:when test="java:isImage($link)">
                <fo:block>
                    <xsl:element name="fo:external-graphic">
                        <xsl:attribute name="src">
				        	<xsl:value-of select="$link" />
				        </xsl:attribute>
                        <xsl:attribute name="width">100%</xsl:attribute>
                        <xsl:attribute name="height">100%</xsl:attribute>
                        <xsl:attribute name="inline-progression-dimension.maximum">100%</xsl:attribute>
                        <xsl:attribute name="content-width">scale-down-to-fit</xsl:attribute>
                        <xsl:attribute name="content-height">
                            <xsl:value-of select="100*$sizeFactor" />%
                        </xsl:attribute>
                    </xsl:element>
                </fo:block>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="bullet">
        <fo:list-item-label end-indent="label-end()">
            <xsl:variable name="level">
                <xsl:number value="count(ancestor::*)" format="1" />
            </xsl:variable>
            <fo:block text-align="end">
                <xsl:choose>
                    <xsl:when test="$level='1'">
                        <xsl:text>&#x200b;</xsl:text> <!-- Zero width space -->
                    </xsl:when>
                    <xsl:when test="$level='2'">
                        <xsl:text>&#x25CF;</xsl:text> <!-- black circle -->
                    </xsl:when>
                    <xsl:when test="$level='3'">
                        <xsl:text>&#x25CB;</xsl:text> <!-- white circle -->
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>&#x25A0;</xsl:text> <!-- black square -->
                    </xsl:otherwise>
                </xsl:choose>
            </fo:block>
        </fo:list-item-label>
    </xsl:template>

    <xsl:template name="show-icon">
        <xsl:param name="pic" />
        <fo:inline>
            <xsl:variable name="icon-filename" select="@BUILTIN" />
            <xsl:element name="fo:external-graphic">
                <xsl:attribute name="src">
                    <xsl:value-of select="concat('../icons/',java:urlencode($pic),'.png')" />
                </xsl:attribute>
                <xsl:attribute name="content-width">
                    <xsl:value-of select="8*$sizeFactor" />pt
                </xsl:attribute>
                <xsl:attribute name="content-height">
                    <xsl:value-of select="8*$sizeFactor" />pt
                </xsl:attribute>
            </xsl:element>
            <xsl:text>&#x00A0;</xsl:text> <!-- Non breaking space -->
        </fo:inline>
    </xsl:template>

    <xsl:template name="process-richcontent">
        <xsl:if test="richcontent/@TYPE='NODE'">
            <xsl:call-template name="process_node" />
            <fo:block>
                <fo:leader />
            </fo:block>
        </xsl:if>
        <xsl:if test="richcontent/@TYPE='DETAILS'">
            <xsl:call-template name="process_detail" />
            <fo:block>
                <fo:leader />
            </fo:block>
        </xsl:if>
        <xsl:if test="richcontent/@TYPE='NOTE'">
            <xsl:call-template name="process_note" />
            <fo:block>
                <fo:leader />
            </fo:block>
        </xsl:if>
    </xsl:template>

    <xsl:template name="process_note">
        <fo:block>
            <xsl:call-template name="block_note" />
            <xsl:call-template name="show-icon">
                <xsl:with-param name="pic" select="'note_black_and_transp'" />
            </xsl:call-template>
            <xsl:apply-templates select="richcontent[@TYPE='NOTE']" mode="richtext" />
        </fo:block>
    </xsl:template>

    <xsl:template name="process_detail">
        <fo:block>
            <xsl:call-template name="block_details" />
            <xsl:call-template name="show-icon">
                <xsl:with-param name="pic" select="'edit_details'" />
            </xsl:call-template>
            <xsl:apply-templates select="richcontent[@TYPE='DETAILS']" mode="richtext" />
        </fo:block>
    </xsl:template>

    <xsl:template name="process_node">
        <fo:block>
            <xsl:if test="../icon/@BUILTIN!=''">
                <xsl:variable name="icon-filename" select="../icon/@BUILTIN" />
                <xsl:call-template name="show-icon">
                    <xsl:with-param name="pic">
                        <xsl:value-of select="concat('builtin/',$icon-filename)" />
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:if>
            <xsl:apply-templates select="richcontent[@TYPE='NODE']" mode="richtext" />
        </fo:block>
    </xsl:template>

    <xsl:template name="block_note">
        <xsl:attribute name="padding">0.5em</xsl:attribute>
        <xsl:attribute name="background-color">rgb(255,255,200)</xsl:attribute>
    </xsl:template>

    <xsl:template name="block_details">
        <xsl:attribute name="padding-left">0.5em</xsl:attribute>
        <xsl:attribute name="border-left-width">thin</xsl:attribute>
        <xsl:attribute name="border-left-style">dotted</xsl:attribute>
        <xsl:attribute name="border-left-color">gray</xsl:attribute>
    </xsl:template>

    <xsl:template name="isIcon">
        <xsl:param name="path" />
        <xsl:if test="$path!=''">
            <xsl:call-template name="show-icon">
                <xsl:with-param name="pic">
                    <xsl:value-of select="concat('builtin/',$path)" />
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
