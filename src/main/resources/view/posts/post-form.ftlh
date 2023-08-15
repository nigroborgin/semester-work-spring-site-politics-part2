<#import "/spring.ftl" as spring/>

<#include "../base.ftlh">
<#macro title>Set Post info</#macro>

<#macro content>

    <h2>Set Post info</h2>

    <#if showNew>
        <div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="First group">
            <a href="/posts/new">
                <button type="button" class="btn btn-outline-primary">
                    Create new post
                </button>
            </a>
        </div>
        <br><br>
    </#if>

    <#if showDelete>
        <div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="Second group">
            <form method="post" action="/posts/${postView.id?string.c}/delete">
                <button type="submit" class="btn btn-outline-danger">Delete</button>
            </form>
        </div>
        <br><br>
    </#if>


    <form method="post">
        <@spring.bind path="postForm"/>

        <div class="form-group">
            <label>Title:
                <br>
                <input class="form-control" type="text" name="title" value="${(postView.title)!''}"/>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>View Author:
                <br>
                <input class="form-control" type="text" name="authorOfPost" readonly value="${(postView.authorOfPost)!''}"/>
            </label>
        </div>
        <br>

<#--        <div class="form-group">-->
<#--            <label>Date and time:-->
<#--                <br>-->
<#--                <input type="datetime-local" name="date" value="${(post.date)!}"/>-->
<#--            </label>-->
<#--        </div>-->
<#--        <br>-->

        <div class="form-group">
            <label>Text of post:
                <br>
                <textarea rows="10" cols="30" name="text">${(postView.text)!''}</textarea>
            </label>
        </div>
        <br>

        <div class="btn-group position-absolute start-50 translate-middle-x">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </form>

</#macro>