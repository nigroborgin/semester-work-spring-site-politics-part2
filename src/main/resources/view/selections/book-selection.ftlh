<#import "/spring.ftl" as spring/>

<#include "../base.ftlh">
<#macro title>Book-Selection</#macro>

<#macro content>

    <#if selectionBookView??>
        <h2>${selectionBookView.title!''}</h2>

        <#if showNew>
            <div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="First group">
                <a href="/selections-books/new">
                    <button type="button" class="btn btn-outline-primary">
                        Create new book-selection
                    </button>
                </a>
            </div>
            <br><br>
        </#if>

        <#if showEdit || showDelete>
            <div class="btn-toolbar position-absolute start-50 translate-middle-x"
                 role="toolbar"
                 aria-label="Toolbar with button groups">
                <#if showEdit>
                    <div class="btn-group" role="group" aria-label="Second group">
                        <a href="/selections-books/${selectionBookView.id?string.c}/edit">
                            <button class="btn btn-outline-secondary">
                                Edit selection
                            </button>
                        </a>
                    </div>
                </#if>

                <#if showDelete>
                    &nbsp;
                    <div class="btn-group" role="group" aria-label="Third group">
                        <form method="post" action="/selections-books/${selectionBookView.id?string.c}/delete">
                            <button class="btn btn-outline-danger" type="submit">
                                Delete selection
                            </button>
                        </form>
                    </div>
                </#if>
            </div>
            <br><br>
        </#if>


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
    </#if>
</#macro>
