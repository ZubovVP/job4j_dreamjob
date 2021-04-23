<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.store.Store" %>
<%@ page import="ru.job4j.dream.model.Post" %>
<%@ page import="java.util.Collection" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <style>
        .btn-group button {
            background-color: #4CAF50; /* Green background */
            border: 1px solid green; /* Green border */
            color: white; /* White text */
            padding: 10px 24px; /* Some padding */
            cursor: pointer; /* Pointer/hand icon */
            float: left; /* Float the buttons side by side */
        }

        .btn-group button:not(:last-child) {
            border-right: none; /* Prevent double borders */
        }

        /* Clear floats (clearfix hack) */
        .btn-group:after {
            content: "";
            clear: both;
            display: table;
        }

        /* Add a background color on hover */
        .btn-group button:hover {
            background-color: #3e8e41;
        }
    </style>
    <title>Работа мечты</title>
</head>
<body>
<div class="container pt-3">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/posts.do">Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidates.do">Кандидаты</a>
            </li>
            <%if (session.getAttribute("user") != null) {%>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/post/edit.jsp">Добавить вакансию</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidate/edit.jsp">Добавить кандидата</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/logout">  <c:out value="${user}"/> |
                    Выйти</a>
            </li>
            <%} else {%>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">Войти</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/reg.jsp">Регистрация</a>
            </li>
            <%
                } %>
        </ul>
        <div class="card" style="width: 100%">
            <div class="card-header">
                Кандидаты
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Названия</th>
                        <th scope="col">Фото</th>
                        <th scope="col">Кнопки</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${candidates}" var="can">
                        <tr>
                            <td>
                                <a href='<c:url value="/candidate/edit.jsp?id=${can.id}"/>'>
                                    <i class="fa fa-edit mr-3"></i>
                                </a>
                                <c:out value="${can.name}"/>
                            </td>
                            <c:if test="${not empty can.photo}">
                            <td>

                                <img src="<c:url value='/download?name=${can.photo}'/>" width="100px"
                                     height="100px"/>
                            </td>
                            <div class="btn-group">
                                <td>
                                    <form action="<c:url value='/photo'/>" method="post">
                                        <input type="hidden" class="form-control" name="id"
                                               value="<c:out value="${can.id}"/>">
                                        <input type="hidden" class="form-control" name="name"
                                               value="<c:out value="${can.name}"/>">
                                        <input type="hidden" class="form-control" name="photo"
                                               value="<c:out value="${can.photo}"/>">
                                        <button type="submit" class="btn btn-primary">Удалить фото</button>
                                    </form>
                                    </c:if>
                                    <c:if test="${empty can.photo}">
                                <td></td>
                                <div class="btn-group">
                                    <td>
                                        <form action="<c:url value='/photo'/>" method="get"
                                              enctype="multipart/form-data">
                                            <input type="hidden" class="form-control" name="id"
                                                   value="<c:out value="${can.id}"/>">
                                            <button type="submit" class="btn btn-primary">Добавить фото</button>
                                        </form>
                                        </c:if>
                                        <form action="<c:url value='/candidates.do'/>" method="post">
                                            <input type="hidden" class="form-control" name="id"
                                                   value="<c:out value="${can.id}"/>">
                                            <button type="submit" class="btn btn-primary">Удалить кандидата</button>
                                        </form>
                                    </td>
                                </div>
                            </div>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>