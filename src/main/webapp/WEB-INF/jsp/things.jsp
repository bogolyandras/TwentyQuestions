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
        <form:form action="${pageContext.request.contextPath}/addThing" modelAttribute="thing" id="thingForm">
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
		<c:if test="${empty showThingForm}">
			<button id="showThingForm" type="button" class="btn btn-default"><fmt:message key="app.things.addnewthing" /></button>
			<script>
			$("#thingForm").hide();
			$("#showThingForm").click(function() {
				$("#showThingForm").hide();
			  	$("#thingForm").show( "slow" );
			});
			</script>
		</c:if>
		
		<table class="table table-hover table-responsive">
			<thead>
				<tr>
					<th><fmt:message key="app.things.thingname" /></th>
					<th><fmt:message key="app.action.edit" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${things}" var="t" varStatus="loop">        
			    	<tr>
			    		<td><c:out value="${t.name}"/></td>
			    		<td>
			    			<form method="get" action="<c:url value="/thing"/>">
					        	<input type="hidden" name="id" value="${t.id}" />
					        	<input type="submit" class="form-control btn btn-primary" value="<fmt:message key="app.action.open" />" />
					        </form>
			    		</td>
			    	</tr>
			    </c:forEach>
		    </tbody>
		</table>
		
    </jsp:body>
</t:genericpage>