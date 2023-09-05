<#import "/spring.ftl" as spring/>

<#include "../base.ftlh">
<#macro title>Book-Selection</#macro>

<#macro content>

    <#if selectionBookView??>
        <h2>${selectionBookView.title!''}</h2>

        <#if showNew>
            <div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="First group">
                <a href="/selections-books<#if isMy??>/my</#if>/new">
                    <button type="button" class="btn btn-outline-primary">
                        Create new book-selection
                    </button>
                </a>
            </div>
            <br><br>
        </#if>

        <div class="btn-toolbar position-absolute start-50 translate-middle-x"
             role="toolbar"
             aria-label="Toolbar with button groups">
            <#if showEdit || showDelete>
                <#if showEdit>
                    <div class="btn-group" role="group" aria-label="Second group">
                        <a href="/selections-books<#if isMy??>/my</#if>/${selectionBookView.id?string.c}/edit">
                            <button class="btn btn-outline-secondary">
                                Edit
                            </button>
                        </a>
                    </div>
                    &nbsp;
                </#if>

                <#if showDelete>
                    <div class="btn-group" role="group" aria-label="Third group">
                        <form method="post" action="/selections-books<#if isMy??>/my</#if>/${selectionBookView.id?string.c}/delete">
                            <button class="btn btn-outline-danger" type="submit">
                                Delete
                            </button>
                        </form>
                    </div>
                    &nbsp;
                </#if>
            </#if>

            <div class="btn-group" role="group" aria-label="Fourth group">
                <a href="/selections-books<#if isMy??>/my</#if>/${selectionBookView.id?string.c}/download">
                    <button class="btn btn-outline-success">
                        Download
                    </button>
                </a>
            </div>
        </div>
        <br><br>


        <#if (selectionBookView.bookList)??>
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
                            <th>
                                <center>Download</center>
                            </th>
                        </tr>
                    </thead>

                    <#list selectionBookView.bookList as book>
                        <tbody>
                            <tr>
                                <td>
                                    <center>${book.title!''}</center>
                                </td>
                                <td>
                                    <center><strong>${book.author!''}</strong><br></center>
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
                                <td>
                                    <center>
                                        <#if book.formatsOfBook?? && book.formatsOfBook?size gt 0>
                                            <div class="btn-group" role="group" aria-label="Third group">
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
    </#if>
</#macro>
