<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="i18n.text" />
<t:genericpage>
    <jsp:body>
    	<c:if test="${not empty message}">
    		<p class="${messageBackground}">${message}</p>
    	</c:if>
    	<c:if test="${fn:length(thingsToTeach) eq 0}">
   			<h1><fmt:message key="app.teach.nothingtoteach" /></h1>
		</c:if>
		<c:forEach items="${thingsToTeach}" var="tq" varStatus="loop">
			<table class="table table-hover table-responsive">
				<thead>
					<tr>
						<th><c:out value="${tq.thing.name}"></c:out></th>
						<th></th>
						<th></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${tq.questions}" var="q" varStatus="loop">
						<tr>
							<td>${q.text}</td>
							<td>
								<form method="post" action="<c:url value="/addRelation"/>">
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
									<input type="hidden" name="teaching" value="yes"/> 
						        	<input type="hidden" name="question_id" value="${q.id}" />
						        	<input type="hidden" name="thing_id" value="${tq.thing.id}" />
						        	<input type="hidden" name="relation" value="yes" />
						        	<input type="submit" class="btn btn-success" value="<fmt:message key="app.answer.yes" />" />
						        </form>
							</td>
							<td>
								<form method="post" action="<c:url value="/addRelation"/>">
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
									<input type="hidden" name="teaching" value="yes"/> 
						        	<input type="hidden" name="question_id" value="${q.id}" />
						        	<input type="hidden" name="thing_id" value="${tq.thing.id}" />
						        	<input type="hidden" name="relation" value="irrelevant" />
						        	<input type="submit" class="btn btn-warning" value="<fmt:message key="app.answer.irrelevant" />" />
						        </form>
							</td>
							<td>
								<form method="post" action="<c:url value="/addRelation"/>">
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
									<input type="hidden" name="teaching" value="yes"/> 
						        	<input type="hidden" name="question_id" value="${q.id}" />
						        	<input type="hidden" name="thing_id" value="${tq.thing.id}" />
						        	<input type="hidden" name="relation" value="no" />
						        	<input type="submit" class="btn btn-danger" value="<fmt:message key="app.answer.no" />" />
						        </form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:forEach>
    </jsp:body>
</t:genericpage>