<#import "/spring.ftl" as spring/>

<#include "../base.ftlh">
<#macro title>Set <#if isMy??>My</#if> Book-Selection info</#macro>

<#macro content>

    <h2>Set <#if isMy??>My</#if> Book-Selection info</h2>

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

    <#if showDelete>
        <div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="Second group">
            <form method="post" action="/selections-books<#if isMy??>/my</#if>/${selectionBookView.id?string.c!''}/delete">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="btn btn-outline-danger">
                    Delete
                </button>
            </form>
        </div>
    </#if>
    <br><br>

    <form method="post">
        <@spring.bind path="selectionBookEdit"/>

        <div class="form-group">
            <label>Title of book-selection:
                <br>
                <input class="form-control-sm ${(titleError??)?string('is-invalid', '')}"
                       type="text"
                       name="title"
                       value="${selectionBookView.title!''}"/>
                <#if titleError??>
                    <div class="invalid-feedback">
                        ${titleError}
                    </div>
                </#if>
            </label>
        </div>
        <br>

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
                                <center>Select</center>
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
                                    <center>
                                        <strong>${book.author!''}</strong><br>
                                    </center>
                                </td>
                                <td>
                                    <center>
                                        <input type="checkbox" name="bookIdList" value="${book.id!''}" <#if book.isSelected> checked </#if>>
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

        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary">Save</button>
    </form>
</#macro>
