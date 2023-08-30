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
                <input class="form-control-sm" type="text" name="title" value="${bookView.title!''}"/>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>Author:
                <br>
                <input class="form-control-sm" type="text" name="author" value="${bookView.author!''}"/>
            </label>
        </div>
        <br>

        <label>Description:
            <br>
            <textarea rows="10" cols="50" name="description">${bookView.description!''}</textarea>
        </label>
        <br>

        <label>File:
            <br>
            <input type="file" name="file" value="${bookView.fileUrl!''}"/>
        </label>
        <br><br>

        <button type="submit" class="btn btn-primary">Save</button>
    </form>
</#macro>