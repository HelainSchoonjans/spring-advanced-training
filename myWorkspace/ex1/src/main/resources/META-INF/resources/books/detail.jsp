<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
          integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous"/>
    <title><c:out value="${entity.title}"/></title>
</head>

<body>
	<div style="display: flex; flex: column">
		<div style="flex: 1; padding: 15px">
			Title : <c:out value="${entity.title}"/><br/>
			Author : <c:out value="${entity.author.fullname}"/><br/>
			<hr/>
            Comments :
            <ul>
                <c:forEach var="c" items="${entity.comments}">
                    <li class="list-group-item"><c:out value="${c.text}"/></li>
                </c:forEach>
            </ul>

        </div>

        <div style="flex: 1; padding: 15px">
            <!--hides the form if not authenticated-->
            <security:authorize access="isAuthenticated()">
                <form:form modelAttribute="command" method="post" action="/reservations">
                    <input type="hidden" name="bookId" value="${entity.id}"/>
                    <fieldset>
                        <legend>Borrow this book</legend>
                        <label for="pickupDate">Pickup date :</label>
                        <form:input type="date" path="pickupDate" class="form-control"/>
                        <form:errors path="pickupDate"/>
                        <br/>

                        <label for="returnDate">Return date :</label>
                        <form:input type="date" path="returnDate" class="form-control"/>
                        <form:errors path="returnDate"/>
                        <br/>

                        <button class="btn btn-primary" type="submit" style="margin-top:20px">Borrow</button>
                    </fieldset>
                </form:form>
            </security:authorize>
            <!--show a login link instead-->
            <!--The login will redirect to / however :/ -->
            <!-- So you need to move back to /books to borrow now -->
            <security:authorize access="isAnonymous()">
                <a href="/login">Login</a>
            </security:authorize>
        </div>
    </div>
</body>
</html>
