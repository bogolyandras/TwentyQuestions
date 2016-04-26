<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="i18n.text" />
<t:genericpage>
    <jsp:body>
    	<c:if test="${not empty message}">
    		<p class="${messageBackground}">${message}</p>
    	</c:if>
		<div class="jumbotron">
            <p id="thingName">${thing.name}</p>
            <form:form action="${pageContext.request.contextPath}/editThing" modelAttribute="thing" id="thingForm">
	           	<div class="form-group">
	           		<form:hidden path="id" />
					<form:input path="name" cssClass="form-control"/>
					<form:errors path="name" cssClass="text-danger"></form:errors>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
					<input type="submit" class="btn btn-success form-control" value="<fmt:message key="app.action.save" />" />
				</div>
            </form:form>
            <form method="post" action="<c:url value="/deleteThing"/>" id="deleteButton">
            	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
	        	<input type="hidden" name="id" value="${thing.id}" />
	        	<input type="submit" class="btn btn-danger" value="<fmt:message key="app.action.delete" />" />
	        </form>
            <c:if test="${empty showThingForm}">
				<button id="showThingForm" type="button" class="btn btn-success"><fmt:message key="app.action.edit" /></button>
				<script>
				$("#thingForm").hide();
				$("#deleteButton").hide();
				$("#showThingForm").click(function() {
					$("#showThingForm").hide();
					$("#thingName").hide();
				  	$("#thingForm").show( "slow" );
				  	$("#deleteButton").show( "slow" );
				});
				</script>
			</c:if>
        </div>
        <table class="table table-hover table-responsive">
			<thead>
				<tr>
					<th><fmt:message key="app.questions.definedquestions" /></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${definedQuestions}" var="q" varStatus="loop">
					<tr>
						<td>${q.text}</td>
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
						<td>
							<form method="post" action="<c:url value="/deleteRelation"/>">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
					        	<input type="hidden" name="question_id" value="${q.id}" />
					        	<input type="hidden" name="thing_id" value="${thing.id}" />
					        	<input type="submit" class="btn btn-danger" value="<fmt:message key="app.action.delete" />" />
					        </form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<table class="table table-hover table-responsive">
			<thead>
				<tr>
					<th><fmt:message key="app.questions.undefinedquestions" /></th>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${undefinedQuestions}" var="q" varStatus="loop">
					<tr>
						<td>${q.text}</td>
						<td>
							<form method="post" action="<c:url value="/addRelation"/>">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
					        	<input type="hidden" name="question_id" value="${q.id}" />
					        	<input type="hidden" name="thing_id" value="${thing.id}" />
					        	<input type="hidden" name="relation" value="yes" />
					        	<input type="submit" class="btn btn-success" value="<fmt:message key="app.answer.yes" />" />
					        </form>
						</td>
						<td>
							<form method="post" action="<c:url value="/addRelation"/>">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
					        	<input type="hidden" name="question_id" value="${q.id}" />
					        	<input type="hidden" name="thing_id" value="${thing.id}" />
					        	<input type="hidden" name="relation" value="irrelevant" />
					        	<input type="submit" class="btn btn-warning" value="<fmt:message key="app.answer.irrelevant" />" />
					        </form>
						</td>
						<td>
							<form method="post" action="<c:url value="/addRelation"/>">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
					        	<input type="hidden" name="question_id" value="${q.id}" />
					        	<input type="hidden" name="thing_id" value="${thing.id}" />
					        	<input type="hidden" name="relation" value="no" />
					        	<input type="submit" class="btn btn-danger" value="<fmt:message key="app.answer.no" />" />
					        </form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
    </jsp:body>
</t:genericpage>