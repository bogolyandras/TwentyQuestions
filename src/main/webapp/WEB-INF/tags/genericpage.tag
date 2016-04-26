<%@ tag description="Overall Page template" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="i18n.text" />
<!DOCTYPE html>
<html lang="${language}">
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <title><fmt:message key="app.menu.title" /></title>
	    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css" rel="stylesheet">
	    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    	<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    </head>
  	<body>
  		<a href="#content" class="sr-only sr-only-focusable">Skip to main content</a>
  		<div class="container">
	        <nav class="navbar navbar-default" role="navigation">
	            <div class="container">
	                <div class="navbar-header">
	                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
	                        <span class="sr-only">Toggle navigation</span>
	                        <span class="icon-bar"></span>
	                        <span class="icon-bar"></span>
	                        <span class="icon-bar"></span>
	                    </button>
	                    <a class="navbar-brand" href="<c:url value="/"/>"><fmt:message key="app.menu.title" /></a>
	                </div>
	
	                <div class="collapse navbar-collapse navbar-right" id="bs-example-navbar-collapse-1">
	                    <ul class="nav navbar-nav">
	                        <li><a href="<c:url value="/newgame"/>"><fmt:message key="app.menu.newgame" /></a></li>
                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#"><fmt:message key="app.menu.language" /><span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="<c:url value="/?language=en"/>">English</a></li>
                                    <li><a href="<c:url value="/?language=hu"/>">Magyar</a></li>
                                </ul>
                            </li>
	                        <li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#"><fmt:message key="app.menu.admin.title" /><span class="caret"></span></a>
								<ul class="dropdown-menu">
									<li><a href="<c:url value="/questions"/>"><fmt:message key="app.menu.admin.questions" /></a></li>
									<li><a href="<c:url value="/things"/>"><fmt:message key="app.menu.admin.things" /></a></li>
									<li><a href="<c:url value="/teach"/>"><fmt:message key="app.menu.admin.teach" /></a></li>
                                    <c:if test="${not empty enableDebug}">
                                        <c:choose>
                                            <c:when test="${enableDebug}">
                                                <li><a href="<c:url value="/disabledebug"/>"><fmt:message key="app.menu.admin.disabledebug" /></a></li>
                                            </c:when>
                                            <c:otherwise>
                                                <li><a href="<c:url value="/enabledebug"/>"><fmt:message key="app.menu.admin.enabledebug" /></a></li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
								</ul>
							</li>
	                    </ul>
	                </div>
	            </div>
	        </nav>
	    </div>
  		<div class="container">
			<jsp:doBody/>
  		</div>
    </body>
</html>