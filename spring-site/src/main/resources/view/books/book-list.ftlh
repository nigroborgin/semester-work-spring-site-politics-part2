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
        <br><br>
    </#if>

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
                                <#if book.formatsOfBook?? && book.formatsOfBook?size gt 0>
                                    <div class="btn-group" role="group" aria-label="Fifth group">
                                        <button type="button" class="btn btn-outline-dark">
                                            <img width="24" src="/static/picture/download-file.png">
                                        </button>
                                        <button type="button" class="btn btn-outline-dark dropdown-toggle dropdown-toggle-split" data-bs-toggle="dropdown" aria-expanded="false">
                                            <span class="visually-hidden">dropdown switcher</span>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <#list book.formatsOfBook as f>
                                                <li><a class="dropdown-item" href="${f.url}">${f.formatName!''}</a></li>
                                            </#list>
                                        </ul>
                                    </div>
                                <#else>
                                    <img width="24" src="/static/picture/prohibition.png">
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