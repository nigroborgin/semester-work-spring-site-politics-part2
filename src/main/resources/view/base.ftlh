<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="/static/style/bootstrap.min.css">
    <link rel="script" href="/static/js/bootstrap.min.js">
    <title><@title></@title></title>
</head>

<body>
<#import "/spring.ftl" as spring/>
<#include "./parts_of_pages/security.ftlh">

<div id="header">
    <#--        <h1>POLITICS SITE</h1>-->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand"
           href="/main">
            Politics Site
        </a>
        <button class="navbar-toggler"
                type="button"
                data-toggle="collapse"
                data-target="#navbarNav"
                aria-controls="navbarNav"
                aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item"><a class="nav-link" href="/main">Main page</a></li>
                <li class="nav-item"><a class="nav-link" href="/users">Users</a></li>
                <li class="nav-item"><a class="nav-link" href="/posts">Posts</a></li>
                <li class="nav-item"><a class="nav-link" href="/books">Books</a></li>
            </ul>
            <#if auth>
                <div class="navbar-text"><strong>USER: ${user.username}</strong></div>
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link" href="/profile">Profile</a></li>
                    <li class="nav-item"><a class="nav-link" href="/logout">Logout</a></li>
                </ul>
            <#else>
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link" href="/login">Login</a></li>
                    <li class="nav-item"><a class="nav-link" href="/reg">Registration</a></li>
                </ul>
            </#if>
        </div>
    </nav>
</div>


<div class="content">
    <center>
        <@content></@content>
    </center>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js"
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
        crossorigin="anonymous"></script>
</body>

</html>