<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.store.Store" %>
<%@ page import="ru.job4j.dream.model.Post" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.CsqlStore" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>

    <title>Работа мечты</title>
</head>
<script>
    function validate() {
        var result = true;
        if ($('#name').val() == '') {
            alert("fill name");
            result = false;
        }
        return result;
    }

    function loadCities() {
        $.ajax("http://localhost:8080/dreamjob/city", {
                method: 'GET',
                dataType: 'application/json',
                complete: function (data) {
                    console.log(data);
                    var city = JSON.parse(data.responseText);
                    for ( var i = 0; i < city.length; i++ ) {
                        $( '#mySelectId' ).append( '<option value="' + city[i]['id'] + '">' + city[i]['name'] + '</option>' );
                    }
                    $( '#mySelectId' ).prop( 'disabled', false ); // Включаем поле
                    console.log(data);
                }
            }
        );
    }
</script>
<body onload="loadCities()">
<%
    String id = request.getParameter("id");
    Candidate can = new Candidate(0, "", 0);
    if (id != null) {
        can = (Candidate) CsqlStore.instOf().findById(Integer.valueOf(id));
    }
%>
<div class="container pt-3">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/posts.do">Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidates.do">Кандидаты</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/post/edit.jsp">Добавить вакансию</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidate/edit.jsp">Добавить кандидата</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp"> <c:out value="${user.name}"/> |
                    Выйти</a>
            </li>
        </ul>
    </div>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат.
                <% } else { %>
                Редактирование кандидата.
                <% } %>
            </div>
            <div class="card-body">
                <form name="myForm" action="<%=request.getContextPath()%>/candidates.do?id=<%=can.getId()%>" method="post"
                      onsubmit="return validate()">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" id="name" name="name" value="<%=can.getName()%>">
                    </div>
                    <div class="form-group" id="city1">
                        <p>
                            <label for="mySelectId">Выберите город:</label>
                            <select id="mySelectId" name="city">
                            </select>
                        </p>
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>