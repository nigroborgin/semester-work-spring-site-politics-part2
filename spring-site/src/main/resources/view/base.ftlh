<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="/static/style/bootstrap.min.css">
    <script src="/static/js/bootstrap.bundle.min.js"></script>
    <title><@title></@title></title>
</head>

<body>
<#import "/spring.ftl" as spring/>
<#include "./parts_of_pages/security.ftlh">

<div id="header">
    <!--        <h1>POLITICS SITE</h1>-->
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
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Selections
                    </a>
                    <ul class="dropdown-menu">
                        <li class="dropdown-item"><a href="/selections-books">Book selections</a></li>
                        <li class="dropdown-item"><a href="/selections-posts">Post selections</a></li>
                        <#if auth>
                        <li><hr class="dropdown-divider"></li>
                        <li class="dropdown-item"><a href="/selections-books/my">My book selections</a></li>
                        <li class="dropdown-item"><a href="/selections-posts/my">My post selections</a></li>
                        </#if>
                    </ul>
                </li>
            </ul>
            <#if auth>
                <div class="navbar-text"><strong>USER: ${user.user.username}</strong></div>
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link" href="/profile">Profile</a></li>
                    <form method="post" action="/logout">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <li class="nav-item">
                            <button type="submit" class="nav-link">Logout</button>
                        </li>
                    </form>
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
</body>

</html>