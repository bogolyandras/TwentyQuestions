<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="i18n.text" />
<t:genericpage>
    <jsp:body>
        <div class="jumbotron">
            <c:choose>
                <c:when test="${empty thingName}">
                    <form method="post" action="<c:url value="/teachThing"/>">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="text" name="name" />
                        <input type="submit" class="btn btn-primary" value="<fmt:message key="app.things.addthing" />" />
                    </form>
                </c:when>
                <c:when test="${empty questionText}">
                    <c:out value="${thingName}" />
                    <form method="post" action="<c:url value="/teachQuestion"/>">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="text" name="text" />
                        <input type="submit" class="btn btn-primary" value="<fmt:message key="app.questions.addquestion" />" />
                    </form>
                </c:when>
                <c:when test="${fn:length(remainigQuestions) gt 0}">
                    Thirds
                </c:when>
                <c:when test="${fn:length(remainingThings) gt 0}">
                    Fourths
                </c:when>
                <c:otherwise>
                    Time to finish
                </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</t:genericpage>