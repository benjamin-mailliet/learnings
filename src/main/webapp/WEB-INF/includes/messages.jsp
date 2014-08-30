<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
			<c:if test="${messagesErreur != null && fn:length(messagesErreur) > 0}">				
				<div class="alert alert-danger" role="alert">
					<c:if test="${fn:length(messagesErreur) > 1}">
					<ul>
						<c:forEach var="message" items="${messagesErreur}">
							<li>${message}</li>
						</c:forEach>
					</ul>
					</c:if>
					<c:if test="${fn:length(messagesErreur) == 1}">
						<p>${messagesErreur[0]}</p>
					</c:if>
				</div>
			</c:if>
			<c:if test="${messagesWarn != null && fn:length(messagesWarn) > 0}">				
				<div class="alert alert-warning" role="alert">
					<c:if test="${fn:length(messagesWarn) > 1}">
					<ul>
						<c:forEach var="message" items="${messagesWarn}">
							<li>${message}</li>
						</c:forEach>
					</ul>
					</c:if>
					<c:if test="${fn:length(messagesWarn) == 1}">
						<p>${messagesWarn[0]}</p>
					</c:if>
				</div>
			</c:if>
			<c:if test="${messagesInfo != null && fn:length(messagesInfo) > 0}">				
				<div class="alert alert-info" role="alert">
					<c:if test="${fn:length(messagesInfo) > 1}">
					<ul>
						<c:forEach var="message" items="${messagesInfo}">
							<li>${message}</li>
						</c:forEach>
					</ul>
					</c:if>
					<c:if test="${fn:length(messagesInfo) == 1}">
						<p>${messagesInfo[0]}</p>
					</c:if>
				</div>
			</c:if>
			<c:if test="${messagesSucces != null && fn:length(messagesSucces) > 0}">				
				<div class="alert alert-success" role="alert">
					<c:if test="${fn:length(messagesSucces) > 1}">
					<ul>
						<c:forEach var="message" items="${messagesSucces}">
							<li>${message}</li>
						</c:forEach>
					</ul>
					</c:if>
					<c:if test="${fn:length(messagesSucces) == 1}">
						<p>${messagesSucces[0]}</p>
					</c:if>
				</div>
			</c:if>
			