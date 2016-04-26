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
        <form:form action="${pageContext.request.contextPath}/addQuestion" modelAttribute="question" id="questionForm">
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
		<c:if test="${empty showQuestionForm}">
			<button id="showQuestionForm" type="button" class="btn btn-default"><fmt:message key="app.questions.addnewquestion" /></button>
			<script>
			$("#questionForm").hide();
			$("#showQuestionForm").click(function() {
				$("#showQuestionForm").hide();
			  	$("#questionForm").show( "slow" );
			});
			</script>
		</c:if>
		
		<table class="table table-hover table-responsive">
			<thead>
				<tr>
					<th><fmt:message key="app.questions.text" /></th>
					<th><fmt:message key="app.action.edit" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${questions}" var="q" varStatus="loop">        
			    	<tr>
			    		<td>
			    			<p id="f_text_${q.id}"><c:out value="${q.text}"/></p>
			    			<form method="post" action="<c:url value="/editQuestion"/>" id="f_edit_text_${q.id}">
			    				<div class="form-group">
			    					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
						        	<input type="hidden" name="id" value="${q.id}" />
						        	<input type="text" name="text" value="${q.text}" class="form-control" />
						        	<input type="submit" class="btn btn-success" value="<fmt:message key="app.action.save" />" />
					        	</div>
					        </form>
			    		</td>
			    		<td>
			    			<button id="f_edit_${q.id}" class="form-control btn btn-primary"><fmt:message key="app.action.edit" /></button>
			    			<form method="post" action="<c:url value="/deleteQuestion"/>" id="f_delete_${q.id}">
			    				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
					        	<input type="hidden" name="id" value="${q.id}" />
					        	<input type="submit" class="form-control btn btn-danger" value="<fmt:message key="app.action.delete" />" />
					        </form>
					        <script>
					        	$("#f_delete_${q.id}").hide();
					        	$("#f_edit_text_${q.id}").hide();
					        	
					        	$("#f_edit_${q.id}").click(function() {
									$("#f_text_${q.id}").hide();
					        		$("#f_edit_${q.id}").hide();
					        		$("#f_delete_${q.id}").show("slow");
					        		$("#f_edit_text_${q.id}").show("slow");
								});
					        </script>
			    		</td>
			    	</tr>
			    </c:forEach>
		    </tbody>
		</table>
		
    </jsp:body>
</t:genericpage>