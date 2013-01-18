<map version="freeplane 1.2.0">
    <!--To view this file, download free mind mapping software Freeplane from http://freeplane.sourceforge.net -->
    <node TEXT="Rich concent map" ID="ID_1723255651" CREATED="1283093380553" MODIFIED="1354549201880">
        <hook NAME="MapStyle">

            <map_styles>
                <stylenode LOCALIZED_TEXT="styles.root_node">
                    <stylenode LOCALIZED_TEXT="styles.predefined" POSITION="right">
                        <stylenode LOCALIZED_TEXT="default" MAX_WIDTH="600" COLOR="#000000" STYLE="as_parent">
                            <font NAME="SansSerif" SIZE="10" BOLD="false" ITALIC="false" />
                        </stylenode>
                        <stylenode LOCALIZED_TEXT="defaultstyle.details" />
                        <stylenode LOCALIZED_TEXT="defaultstyle.note" />
                        <stylenode LOCALIZED_TEXT="defaultstyle.floating">
                            <edge STYLE="hide_edge" />
                            <cloud COLOR="#f0f0f0" SHAPE="ROUND_RECT" />
                        </stylenode>
                    </stylenode>
                    <stylenode LOCALIZED_TEXT="styles.user-defined" POSITION="right">
                        <stylenode LOCALIZED_TEXT="styles.topic" COLOR="#18898b" STYLE="fork">
                            <font NAME="Liberation Sans" SIZE="10" BOLD="true" />
                        </stylenode>
                        <stylenode LOCALIZED_TEXT="styles.subtopic" COLOR="#cc3300" STYLE="fork">
                            <font NAME="Liberation Sans" SIZE="10" BOLD="true" />
                        </stylenode>
                        <stylenode LOCALIZED_TEXT="styles.subsubtopic" COLOR="#669900">
                            <font NAME="Liberation Sans" SIZE="10" BOLD="true" />
                        </stylenode>
                        <stylenode LOCALIZED_TEXT="styles.important">
                            <icon BUILTIN="yes" />
                        </stylenode>
                    </stylenode>
                    <stylenode LOCALIZED_TEXT="styles.AutomaticLayout" POSITION="right">
                        <stylenode LOCALIZED_TEXT="AutomaticLayout.level.root" COLOR="#000000">
                            <font SIZE="18" />
                        </stylenode>
                        <stylenode LOCALIZED_TEXT="AutomaticLayout.level,1" COLOR="#0033ff">
                            <font SIZE="16" />
                        </stylenode>
                        <stylenode LOCALIZED_TEXT="AutomaticLayout.level,2" COLOR="#00b439">
                            <font SIZE="14" />
                        </stylenode>
                        <stylenode LOCALIZED_TEXT="AutomaticLayout.level,3" COLOR="#990000">
                            <font SIZE="12" />
                        </stylenode>
                        <stylenode LOCALIZED_TEXT="AutomaticLayout.level,4" COLOR="#111111">
                            <font SIZE="10" />
                        </stylenode>
                    </stylenode>
                </stylenode>
            </map_styles>
        </hook>
        <hook NAME="AutomaticEdgeColor" COUNTER="2" />
        <node TEXT="" POSITION="right" ID="ID_616155910" CREATED="1354549222360" MODIFIED="1354549355109">
            <edge COLOR="#ff0000" />
            <richcontent TYPE="DETAILS">

                <html>
                    <head>

                    </head>
                    <body>
                        <p>
                            Rich content with
                            <font size="4">big</font>
                            &#160;
                            <font face="Times New Roman">fonts</font>
                            &#160;and
                            <font color="#cc0033">colors</font>
                            &#160;and
                            <b>bold</b>
                            &#160; and
                            <i>italics</i>
                            &#160;and
                            <u>underline</u>
                            .
                        </p>
                    </body>
                </html>
            </richcontent>
        </node>
        <node TEXT="Stylenode with styling" POSITION="left" ID="ID_1334760614" CREATED="1354550415228" MODIFIED="1354551289136" HGAP="70" VSHIFT="10">
            <edge COLOR="#0000ff" />
            <font NAME="Times New Roman" SIZE="16" BOLD="true" ITALIC="true" />
        </node>
    </node>
</map>
