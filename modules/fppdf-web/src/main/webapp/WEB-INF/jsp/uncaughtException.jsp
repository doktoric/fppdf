<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link href="styles/bootstrap.css" rel="stylesheet" type="text/css">
<link href="styles/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="styles/prettify.css" rel="stylesheet" type="text/css">
<link href="styles/docs.css" rel="stylesheet" type="text/css">
<body data-spy="scroll" data-target=".bs-docs-sidebar" data-twttr-rendered="true">
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                </button>
                <a class="brand" href="index.html">FpPdf</a>
                <div class="nav-collapse collapse"></div>
            </div>
        </div>
    </div>

    <header class="jumbotron subhead" id="overview">
        <div class="container">
            <h1>FpPdf</h1>
            <p class="lead">Convert Freeplane mind map to PDF</p>
        </div>
    </header>
    <div class="container">
        <h3>
        	${exception}
        </h3>
        
        <c:forEach items="${exception.stackTrace}" var="element">
            <div>${element}</div>
        </c:forEach>
    </div>
</body>
</html>