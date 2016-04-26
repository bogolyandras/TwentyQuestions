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
			    <c:when test="${displayMessage}">
			    	<h1><fmt:message key="${message}" /></h1>
			    </c:when>
			    <c:otherwise>
					<p><c:out value="${question.text}"></c:out></p>
		            <table>
		            	<tbody>
		            		<tr>
		            			<td>
		            				<form method="post" action="<c:url value="/answer"/>">
							        	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
							        	<input type="hidden" name="answer" value="yes" />
							        	<input type="submit" class="btn btn-success" value="<fmt:message key="app.answer.yes" />" />
							        </form>
		            			</td>
		            			<td>
		            				<form method="post" action="<c:url value="/answer"/>">
							        	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
							        	<input type="hidden" name="answer" value="dunno" />
							        	<input type="submit" class="btn btn-warning" value="<fmt:message key="app.answer.dunno" />" />
							        </form>
		            			</td>
		            			<td>
		            				<form method="post" action="<c:url value="/answer"/>">
							        	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
							        	<input type="hidden" name="answer" value="no" />
							        	<input type="submit" class="btn btn-danger" value="<fmt:message key="app.answer.no" />" />
							        </form>
		            			</td>
		            		</tr>
		            	</tbody>
		            </table>
			    </c:otherwise>
			</c:choose>
            <c:choose>
                <c:when test="${displayMessage}">
                    <c:choose>
                        <c:when test="${fn:length(possibleThings) eq 0}">
                            <h2><fmt:message key="app.game.cantfindout" /></h2>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${possibleThings}" var="t" varStatus="loop">
                                <h2><c:out value="${t.name}"/></h2>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:if test="${fn:length(possibleThings) ne 0 and enableDebug}">
                        <div>
                            <table class="table table-hover table-responsive">
                                <thead>
                                <tr>
                                    <th><fmt:message key="app.game.possiblethings" /></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${possibleThings}" var="t" varStatus="loop">
                                    <tr>
                                        <td><c:out value="${t.name}"/></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
            <c:if test="${enableDebug}">
                <c:if test="${fn:length(answeredQuestions) ne 0}">
                    <div>
                        <table class="table table-hover table-responsive">
                            <thead>
                                <tr>
                                    <th><fmt:message key="app.game.answeredquestion" /></th>
                                    <th><fmt:message key="app.game.answer" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${answeredQuestions}" var="q" varStatus="loop">
                                    <tr>
                                        <td><c:out value="${q.text}"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${q.relation == 'Y'}">
                                                    <p class="text-center bg-success"><fmt:message key="app.answer.yes" /></p>
                                                </c:when>
                                                <c:when test="${q.relation == 'M'}">
                                                    <p class="text-center bg-warning"><fmt:message key="app.answer.dunno" /></p>
                                                </c:when>
                                                <c:when test="${q.relation == 'N'}">
                                                    <p class="text-center bg-danger"><fmt:message key="app.answer.no" /></p>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </c:if>
        </div>
    </jsp:body>
</t:genericpage>