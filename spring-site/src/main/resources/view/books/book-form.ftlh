<#import "/spring.ftl" as spring/>

<#include "../base.ftlh">
<#macro title>Set Book info</#macro>

<#macro content>

<h2>Set Book info</h2>

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

<#if showDelete>
<div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="Second group">
    <form method="post" action="/books/${bookView.id?string.c!''}/delete">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-outline-danger">Delete</button>
    </form>
</div>
</#if>
<br><br>

<form method="post" enctype="multipart/form-data">
    <@spring.bind path="bookEdit"/>

    <div class="form-group">
        <label>Title:
            <br>
            <input class="form-control-sm ${(titleError??)?string('is-invalid', '')}"
                   type="text"
                   name="title"
                   value="${bookView.title!''}"/>
            <#if titleError??>
                <div class="invalid-feedback">
                    ${titleError}
                </div>
            </#if>
        </label>
    </div>
    <br>

    <div class="form-group">
        <label>Author:
            <br>
            <input class="form-control-sm ${(authorError??)?string('is-invalid', '')}"
                   type="text"
                   name="author"
                   value="${bookView.author!''}"/>
            <#if authorError??>
                <div class="invalid-feedback">
                    ${authorError}
                </div>
            </#if>
        </label>
    </div>
    <br>

    <div class="form-group">
        <label for="formControlTextarea">Description:
            <textarea class="form-control-sm ${(descriptionError??)?string('is-invalid', '')}"
                      name="description"
                      id="formControlTextarea"
                      rows="10"
                      cols="60">${bookView.description!''}</textarea>
            <#if descriptionError??>
                <div class="invalid-feedback">
                    ${descriptionError}
                </div>
            </#if>
        </label>
    </div>
    <br>

    <label>File:
        <br>
        <input type="file" name="file" value="${bookView.fileUrl!''}"/>
        <#if fileError??>
            <div class="invalid-feedback">
                ${fileError}
            </div>
        </#if>
    </label>
    <br><br>

    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <button type="submit" class="btn btn-primary">Save</button>
</form>
</#macro>