<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://acme.com/java/com.acme.fppdf.transformer.XsltMethods">

    <xsl:template match="font" mode="richtext">
        <xsl:if test="@face">
            <fo:inline>
                <xsl:attribute name="font-family">
					<xsl:value-of select="@face" />
				</xsl:attribute>
                <xsl:apply-templates mode="richtext" />
            </fo:inline>
        </xsl:if>
        <xsl:if test="@color">
            <fo:inline>
                <xsl:attribute name="color">
					<xsl:value-of select="@color" />
				</xsl:attribute>
                <xsl:apply-templates mode="richtext" />
            </fo:inline>
        </xsl:if>
        <xsl:if test="@size">
            <fo:inline>
                <xsl:attribute name="font-size">
					<xsl:value-of select="@size * $sizeFactor" />
				</xsl:attribute>
                <xsl:apply-templates mode="richtext" />
            </fo:inline>
        </xsl:if>
    </xsl:template>

    <!-- <font NAME="Times New Roman" SIZE="20" BOLD="true" ITALIC="true"/> -->
    <xsl:template match="font" mode="nodestyle">

        <xsl:if test="@NAME">
            <xsl:attribute name="font-family">
                <xsl:value-of select="@NAME" />
              </xsl:attribute>
        </xsl:if>
        <xsl:if test="@SIZE">
            <xsl:attribute name="font-size">
        <xsl:value-of select="@SIZE * $sizeFactor" />
              </xsl:attribute>
        </xsl:if>
        <xsl:if test="@BOLD = 'true'">
            <xsl:attribute name="font-weight">
                <xsl:text>bold</xsl:text>
            </xsl:attribute>
        </xsl:if>
        <xsl:if test="@ITALIC = 'true'">
            <xsl:attribute name="font-style">
                <xsl:text>italic</xsl:text>
            </xsl:attribute>
        </xsl:if>
    </xsl:template>

    <xsl:template match="hook">
        <xsl:choose>
            <xsl:when test="java:isImage(@URI)">
                <fo:block>
                    <xsl:element name="fo:external-graphic">
                        <xsl:attribute name="src">
                     		<xsl:value-of select="@URI" />
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

    <xsl:template match="img" mode="richtext">
        <xsl:choose>
            <xsl:when test="@src!=''">
                <fo:block>
                    <xsl:element name="fo:external-graphic">
                        <xsl:attribute name="src">
                     		<xsl:value-of select="@src" />
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

    <xsl:template match="text()[.!='']" mode="richtext">
        <xsl:value-of select="." />
    </xsl:template>

    <xsl:template match="b" mode="richtext">
        <fo:inline font-weight="bold">
            <xsl:apply-templates mode="richtext" />
        </fo:inline>
    </xsl:template>

    <xsl:template match="i" mode="richtext">
        <fo:inline font-style="italic">
            <xsl:apply-templates mode="richtext" />
        </fo:inline>
    </xsl:template>

    <xsl:template match="u" mode="richtext">
        <fo:inline text-decoration="underline">
            <xsl:apply-templates mode="richtext" />
        </fo:inline>
    </xsl:template>

    <xsl:template match="p" mode="richtext">
        <xsl:apply-templates mode="richtext" />
        <fo:block />
    </xsl:template>

    <xsl:template match="br" mode="richtext">
        <fo:block />
        <xsl:apply-templates mode="richtext" />
    </xsl:template>
</xsl:stylesheet>
