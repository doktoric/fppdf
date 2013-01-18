<%@page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>

<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">


<link href="styles/DT_bootstrap.css" rel="stylesheet" type="text/css">
<link href="styles/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="styles/bootstrap-responsive.css" rel="stylesheet" type="text/css">
<link href="styles/docs.css" rel="stylesheet" type="text/css">
<link href="styles/prettify.css" rel="stylesheet" type="text/css">

<script src="scripts/jquery.js"></script>
<script src="scripts/jquery.dataTables.js"></script>
<script src="scripts/DT_bootstrap.js"></script>
<script src="scripts/prettify.js"></script>
<script src="scripts/bootstrap.js"></script>
<script src="scripts/bootstrap.min.js"></script>
<script src="scripts/bootstrap-transition.js"></script>
<script src="scripts/bootstrap-alert.js"></script>
<script src="scripts/bootstrap-modal.js"></script>
<script src="scripts/bootstrap-dropdown.js"></script>
<script src="scripts/bootstrap-scrollspy.js"></script>
<script src="scripts/bootstrap-tab.js"></script>
<script src="scripts/bootstrap-tooltip.js"></script>
<script src="scripts/bootstrap-popover.js"></script>
<script src="scripts/bootstrap-button.js"></script>
<script src="scripts/bootstrap-collapse.js"></script>
<script src="scripts/bootstrap-carousel.js"></script>
<script src="scripts/bootstrap-typeahead.js"></script>
<script src="scripts/bootstrap-affix.js"></script>
<script src="scripts/bootstrap-inputmask.js"></script>
<script src="scripts/bootstrap-rowlink.js"></script>
<script src="scripts/bootstrap-fileupload.js"></script>

<title>FpPdf</title>
</head>

<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true">
		
	<div id="upload">
		<form:form class="form-horizontal" id="uploadForm" name="uploadForm" action="convert" method="POST" enctype="multipart/form-data"	commandName="uploadForm">
			<fieldset>
				<legend style="margin-left: 5px;">Upload mind map</legend>
				<div id="errorHolder">
					<form:errors path="*" cssClass="alert" element="div" />
				</div>
				<div class="control-group">
					<label class="control-label" for="inputIcon">File browse:</label>
					<div class="controls">
<!-- 						<input id="file" name="file" type="file"  onchange="document.getElementById('info').innerHTML=this.value"> -->
						<div class="input-append">
							<input class="span3" type="text" id="filePath" name="filePath" data-dismiss="fileupload" />
							<a class="btn" onclick="$('#file').click()">Browse</a>
							<input id="file" name="file" type="file" style="left: 0px;position: absolute;width: 85%;left: 40px;opacity:0">
							
						</div>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="sizeFactor">Scaling:</label>
					<div class="controls">
						<div class="input-append">
							<span class="select"> <select name="sizeFactor">
									<option value="0.25">25%</option>
									<option value="0.5">50%</option>
									<option value="0.75">75%</option>
									<option value="1" selected="selected">100%</option>
									<option value="1.25">125%</option>
									<option value="1.5">150%</option>
									<option value="1.75">175%</option>
									<option value="2">200%</option>
									<option value="2.5">250%</option>
									<option value="3">300%</option>
									<option value="4">400%</option>
									<option value="5">500%</option>
							</select>
							</span>
						</div>
					</div>
				</div>
	
				<div class="control-group">
					<div class="controls row">
						<label class="radio" for="portrait">Portrait <input
							type="radio" name="orientationType" value="PORTRAIT"
							id="portrait" checked="checked">
						</label> <label class="radio" for="landscape">Landscape <input
							type="radio" name="orientationType" value="LANDSCAPE"
							id="landscape">
						</label>
					</div>
				</div>
	
				<input name="toType" type="hidden" value="PDF" class="hidden" />
	
				<div class="control-group">
					<div class="controls">
						<input type="submit"  id="submitId" class="btn" value="Upload"/>
						<button value="cancel" type="reset" class="btn" onclick="$('#upload').hide();">Cancel</button>
					</div>
				</div>
			</fieldset>
		</form:form>
	</div>

	<header class="jumbotron subhead" id="overview">
		<div class="container">
            <a id="uploadButton" title="Upload">
                <img src="img/upload.png" /><br/>
                <span class="pagination-left" style="font-size: 12pt">Click here to upload mind map</span>
            </a>
			<h2>FpPdf</h2>
			<p class="lead">Convert Freeplane mind map to PDF</p>
		</div>
	</header>
	
	<div class="row" style="height: 20px !important"></div>
	<div class="container" style="width: 80% !important">
		<div class="container" style="width: 100%">
			<table class="table table-hover" id="convertedTable">
				<thead>
					<tr>
						<th style="text-align: center !important;">Id</th>
						<th style="text-align: center !important;">Name</th>
						<th style="text-align: center !important;">CreationDate</th>
						<th style="text-align: center !important;">ToType</th>
						<th style="text-align: center !important;">Orientation</th>
						<th style="text-align: center !important;">Mm File</th>
						<th style="text-align: center !important;">Fo File</th>
						<th style="text-align: center !important;">Converted File</th>
						<th style="text-align: center !important;">Delete</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${converterList}">

						<tr id="item_${item.id}">
							<td
								style="text-align: center !important; padding-bottom: 0px !important;">${item.id}</td>
							<td
								style="text-align: center !important; padding-bottom: 0px !important;">${item.name}</td>
							<td
								style="text-align: center !important; padding-bottom: 0px !important;">${item.creationDate}</td>
							<td
								style="text-align: center !important; padding-bottom: 0px !important;">${item.toType}</td>
							<td
								style="text-align: center !important; padding-bottom: 0px !important;">${item.orientation}</td>
							<td
								style="text-align: center !important; padding-bottom: 0px !important;">
								<form action='<c:url value="/download/mm/${item.id}"/>'
									method="post">
									<button type="submit" class="btn">
										<i class="icon-file"></i>
									</button>
								</form>
							</td>
							<td
								style="text-align: center !important; padding-bottom: 0px !important;">
								<form action='<c:url value="/download/fo/${item.id}"/>'
									method="post">
									<button type="submit" class="btn">
										<i class="icon-print"></i>
									</button>
								</form>
							</td>
							<td
								style="text-align: center !important; padding-bottom: 0px !important;">
								<form action='<c:url value="/download/pdf/${item.id}"/>'
									method="post">
									<button type="submit" class="btn">
										<i class="icon-book"></i>
									</button>
								</form>
							</td>
							<td style="text-align: center !important;">
								<form action='<c:url value="/remove/${item.id}"/>' method="post">
									<button type="submit" class="btn">
										<i class="icon-trash"></i>
									</button>
								</form>
							</td>

						</tr>
					</c:forEach>
				</tbody>


			</table>
		</div>


	</div>
	<footer class="footer">
		<div class="container">
			<spring:message code="git.commit.id" var="gitVersionId" text="" />
			<b>Git version:</b> <a
				href=""
				title="${gitVersionId}"><spring:message
					code="git.commit.id.abbrev" text="Works only with maven build" /></a>
			<b>committed by:</b>
			<spring:message code="git.commit.user.name" text="Unknown" />
			<b>on:</b>
			<spring:message code="git.commit.time" text="Unknown" />
			<b>with message:</b>
			<spring:message code="git.commit.message.short" text="Unknown" />
		</div>
	</footer>
	
	<script type="text/javascript">
	
	$(document).ready(function () {
		
		// upload icon	
        $("#uploadButton").click(function () {
        	$("#upload").toggle();
        });
        
        // file browse button
		$('#file').change(function() {
			
			$('#filePath').val($(this).val());
			if ($('#filePath').val() != "") {
				$('#errorHolder').hide();
			}
		});
        
	    $('form[id=uploadForm]').submit(function() {
	        if ($('#filePath').val() == "") {
		        $('#errorHolder').html("<div class='alert'>Please select a file</div>");
	            return false;
	        }
	        return true;
	    });        
        
        // highlight item if just uploaded
        if(location.hash) {
        	$('#item_' + location.hash.substring(1)).addClass("uploaded");
        }
	});
	
	</script>
</body>
</html>
