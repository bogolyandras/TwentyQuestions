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
                <c:when test="${empty thing.name}">
                    <form:form action="${pageContext.request.contextPath}/teachP1" modelAttribute="thing">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="form-group">
                            <form:label path="name"><fmt:message key="app.things.thingname" />:</form:label>
                            <form:input path="name" cssClass="form-control"/>
                        </div>
                        <div class="form-group">
                            <form:errors path="name" cssClass="text-danger"></form:errors>
                        </div>
                        <div class="form-group">
                            <input type="submit" class="btn btn-primary form-control" value="<fmt:message key="app.things.addthing" />" />
                        </div>
                    </form:form>
                </c:when>
                <c:when test="${fn:length(remainigQuestions) gt 0}">
                    <h3><c:out value="${thing.name}"/></h3>
                    <p><c:out value="${currentQuestion.text}" /></p>
                    <table>
                        <tbody>
                        <tr>
                            <td>
                                <form method="post" action="<c:url value="/teachP2"/>">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="hidden" name="answer" value="yes" />
                                    <input type="submit" class="btn btn-success" value="<fmt:message key="app.answer.yes" />" />
                                </form>
                            </td>
                            <td>
                                <form method="post" action="<c:url value="/teachP2"/>">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="hidden" name="answer" value="irrelevant" />
                                    <input type="submit" class="btn btn-warning" value="<fmt:message key="app.answer.irrelevant" />" />
                                </form>
                            </td>
                            <td>
                                <form method="post" action="<c:url value="/teachP2"/>">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="hidden" name="answer" value="no" />
                                    <input type="submit" class="btn btn-danger" value="<fmt:message key="app.answer.no" />" />
                                </form>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </c:when>
                <c:when test="${empty question.text}">
                    <form:form action="${pageContext.request.contextPath}/teachP3" modelAttribute="question">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="form-group">
                            <form:label path="text"><fmt:message key="app.questions.text" />:</form:label>
                            <form:input path="text" cssClass="form-control"/>
                        </div>
                        <div class="form-group">
                            <form:errors path="text" cssClass="text-danger"></form:errors>
                        </div>
                        <div class="form-group">
                            <input type="submit" class="btn btn-primary form-control" value="<fmt:message key="app.questions.addquestion" />" />
                        </div>
                    </form:form>
                </c:when>
                <c:when test="${fn:length(remainingThings) gt 0}">
                    <h3><c:out value="${currentThing.name}" /></h3>
                    <p><c:out value="${question.text}"/></p>
                    <table>
                        <tbody>
                        <tr>
                            <td>
                                <form method="post" action="<c:url value="/teachP4"/>">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="hidden" name="answer" value="yes" />
                                    <input type="submit" class="btn btn-success" value="<fmt:message key="app.answer.yes" />" />
                                </form>
                            </td>
                            <td>
                                <form method="post" action="<c:url value="/teachP4"/>">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="hidden" name="answer" value="irrelevant" />
                                    <input type="submit" class="btn btn-warning" value="<fmt:message key="app.answer.irrelevant" />" />
                                </form>
                            </td>
                            <td>
                                <form method="post" action="<c:url value="/teachP4"/>">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="hidden" name="answer" value="no" />
                                    <input type="submit" class="btn btn-danger" value="<fmt:message key="app.answer.no" />" />
                                </form>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <h2><fmt:message key="app.teach.pleasereview" /></h2>
                    <c:if test="${fn:length(answeredQuestions) ne 0}">
                        <div>
                            <table class="table table-hover table-responsive">
                                <thead>
                                <tr>
                                    <th><fmt:message key="app.things.addnewthing" /></th>
                                    <th><fmt:message key="app.game.answeredquestion" /></th>
                                    <th><fmt:message key="app.game.answer" /></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${answeredQuestions}" var="q" varStatus="loop">
                                    <tr>
                                        <td><c:out value="${thing.name}"/></td>
                                        <td><c:out value="${q.text}"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${q.relation == 'Y'}">
                                                    <p class="text-center bg-success"><fmt:message key="app.answer.yes" /></p>
                                                </c:when>
                                                <c:when test="${q.relation == 'M'}">
                                                    <p class="text-center bg-warning"><fmt:message key="app.answer.irrelevant" /></p>
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
                    <c:if test="${fn:length(answeredThings) ne 0}">
                        <div>
                            <table class="table table-hover table-responsive">
                                <thead>
                                <tr>
                                    <th><fmt:message key="app.menu.admin.things" /></th>
                                    <th><fmt:message key="app.questions.addnewquestion" /></th>
                                    <th><fmt:message key="app.game.answer" /></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${answeredThings}" var="t" varStatus="loop">
                                    <tr>
                                        <td><c:out value="${t.name}"/></td>
                                        <td><c:out value="${question.text}"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${t.relation == 'Y'}">
                                                    <p class="text-center bg-success"><fmt:message key="app.answer.yes" /></p>
                                                </c:when>
                                                <c:when test="${t.relation == 'M'}">
                                                    <p class="text-center bg-warning"><fmt:message key="app.answer.irrelevant" /></p>
                                                </c:when>
                                                <c:when test="${t.relation == 'N'}">
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
                    <form method="post" action="<c:url value="/teachP5"/>">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" class="btn btn-primary form-control" value="<fmt:message key="app.teach.okay" />" />
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</t:genericpage>