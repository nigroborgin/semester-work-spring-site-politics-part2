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

    <#if showEdit || showDelete || bookView.fileUrl??>
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
                        <button type="submit" class="btn btn-outline-danger">Delete</button>
                    </form>
                </div>
            </#if>

            <#if bookView.fileUrl??>
                &nbsp;
                <div class="btn-group" role="group" aria-label="Fiftieth group">
                    <a href="${bookView.fileUrl}" download="">
                        <button type="button" class="btn btn-outline-success">
                            Download
                        </button>
                    </a>
                </div>
                <br>
            </#if>

        </div>
        <br><br>
    </#if>

    Author(s): ${bookView.author!'no info'}

    <h4>Description:</h4>
    <#noautoesc>
        ${bookView.description!''}
    </#noautoesc>

</#macro>