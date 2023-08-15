<#include "../base.ftlh">
<#macro title>Books</#macro>

<#macro content>

    <h2>Books</h2>
    <#if showNew>
        <div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="First group">
            <a href="/books/new">
                <button type="button" class="btn btn-outline-primary">
                    Add new book
                </button>
            </a>
        </div>
    </#if>
    <br><br>

    <#if bookList??>
        <div class="container-fluid">
            <table class="table table-bordered w-auto" border='1'>
                <thead>
                <tr>
                    <th>
                        <center>Title</center>
                    </th>
                    <th>
                        <center>Author</center>
                    </th>
                    <th>
                        <center>More Info</center>
                    </th>
                    <#if showEdit>
                        <th>
                            <center>Edit</center>
                        </th>
                    </#if>
                    <#if showDelete>
                        <th>
                            <center>Delete</center>
                        </th>
                    </#if>
                    <th>
                        <center>Download</center>
                    </th>
                </tr>
                </thead>

                <#list bookList as book>
                    <tbody>
                    <tr>
                        <td>
                            <center>${book.title!''}</center>
                        </td>
                        <td>
                            <center>
                                <strong>${book.author!''}</strong><br>
                            </center>
                        </td>
                        <td>
                            <center>
                                <div class="btn-group" role="group" aria-label="Second group">
                                    <a href="/books/${book.id?string.c}">
                                        <button class="btn btn-outline-success btn-sm">
                                            More
                                        </button>
                                    </a>
                                </div>
                            </center>
                        </td>
                        <#if showEdit>
                            <td>
                                <center>
                                    <div class="btn-group" role="group" aria-label="Third group">
                                        <a href="/books/${book.id?string.c}/edit">
                                            <button class="btn btn-outline-secondary btn-sm">
                                                Edit
                                            </button>
                                        </a>
                                    </div>
                                </center>
                            </td>
                        </#if>
                        <#if showDelete>
                            <td>
                                <center>
                                    <div class="btn-group" role="group" aria-label="Fourth group">
                                        <form method="post" action="/books/${book.id?string.c}/delete">
                                            <button class="btn btn-outline-danger btn-sm" type="submit">
                                                Delete
                                            </button>
                                        </form>
                                    </div>
                                </center>
                            </td>
                        </#if>
                        <td>
                            <center>
                                <#if book.fileUrl??>
                                    <a href="${book.fileUrl}" download="">
                                        <button class="btn btn-outline-dark btn-sm">
                                            <center>
                                                <img width="24" src="/static/picture/download-file.png">
                                            </center>
                                        </button>
                                    </a>
                                <#else>
                                    <center>
                                        <img width="24" src="/static/picture/prohibition.png">
                                    </center>
                                </#if>
                            </center>
                        </td>
                    </tr>
                    </tbody>
                </#list>
            </table>
        </div>
    <#else>
        <strong>No Books.</strong>
    </#if>

</#macro>