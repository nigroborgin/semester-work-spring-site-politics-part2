<#include "../base.ftlh">
<#macro title>Book</#macro>

<#macro content>

    <h2>${(bookView.title)!''}</h2>

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

    <#if showEdit || showDelete || bookView.formatsOfBook??>
        <div class="btn-toolbar position-absolute start-50 translate-middle-x"
             role="toolbar"
             aria-label="Toolbar with button groups">

            <#if showEdit>
                <div class="btn-group" role="group" aria-label="Second group">
                    <a href="/books/${bookView.id?string.c}/edit">
                        <button type="button" class="btn btn-outline-secondary">
                            Edit
                        </button>
                    </a>
                </div>
            </#if>

            <#if showDelete>
                &nbsp;
                <div class="btn-group" role="group" aria-label="Third group">
                    <form method="post" action="/books/${bookView.id?string.c}/delete">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <button type="submit" class="btn btn-outline-danger">Delete</button>
                    </form>
                </div>
            </#if>

            <#if bookView.formatsOfBook??>
                &nbsp;
                <div class="btn-group" role="group" aria-label="Fourth group">
                    <button type="button" class="btn btn-outline-success">
                        Download
                    </button>
                    <button type="button" class="btn btn-outline-success dropdown-toggle dropdown-toggle-split" data-bs-toggle="dropdown" aria-expanded="false">
                        <span class="visually-hidden">dropdown switcher</span>
                    </button>
                    <ul class="dropdown-menu">
                        <#list bookView.formatsOfBook as f>
                            <li><a class="dropdown-item" href="${f.url}">${f.formatName!''}</a></li>
                        </#list>
                    </ul>
                </div>
            <#else>
                <img width="24" src="/static/picture/prohibition.png">
            </#if>
            <br>

        </div>
        <br><br>
    </#if>

    Author(s): ${bookView.author!'no info'}

    <h4>Description:</h4>
    <#noautoesc>
        ${bookView.description!''}
    </#noautoesc>

</#macro>