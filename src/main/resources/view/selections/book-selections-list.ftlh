<#include "../base.ftlh">
<#macro title>Book-Selections</#macro>

<#macro content>

    <h2>Book-Selections<#-- <#if nameByUser??>by <i>${nameByUser}</i></#if>--></h2>

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


    <#if bookSelections??>
        <div class="card-columns col-md">
            <#list bookSelections as selection>

                <span class="card" style="width: 18rem;">
<#--                    <img class="card-img-top" src=".../100px200/" alt="Card image cap">-->
                    <div class="card-body">
                        <h5 class="card-title">${selection.title!}</h5>

                        <div class="btn-toolbar"
                             role="toolbar"
                             aria-label="Toolbar with button groups">

                             <div class="btn-group position-relative start-50 translate-middle-x"
                                  role="group"
                                  aria-label="First group">

                                <a href="/selections-books/${selection.id?string.c}">
                                    <button class="btn btn-outline-success">
                                        Open
                                    </button>
                                </a>

                                <#if showEdit>
                                    &nbsp;
                                    <a href="/selections-books/${selection.id?string.c}/edit">
                                        <button class="btn btn-outline-secondary">
                                            Edit
                                        </button>
                                    </a>
                                </#if>

                                 <#if showDelete>
                                     &nbsp;
                                     <form method="post" action="/selections-books/${selection.id?string.c}/delete">
                                        <button class="btn btn-outline-danger" type="submit">
                                            Delete
                                        </button>
                                     </form>
                                 </#if>
                            </div>
                        </div>
                    </div>
                </span>
                <br>
            </#list>
        </div>
    </#if>

</#macro>