<#include "../base.ftlh">
<#macro title>Post</#macro>

<#macro content>

    <#if postView??>
        <h2>${(postView.title)!''}</h2>

        <#--<div class="btn-toolbar " role="toolbar" aria-label="Toolbar with button groups">-->
        <#if showNew>
            <div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="First group">
                <a href="/posts/new">
                    <button type="button" class="btn btn-outline-primary">
                        Create new post
                    </button>
                </a>
            </div>
        </#if>
        <#--</div>-->

        <#if showEdit || showDelete>
            <br><br>
            <div class="btn-toolbar position-absolute start-50 translate-middle-x"
                 role="toolbar"
                 aria-label="Toolbar with button groups">
                <#if showEdit>
                    <div class="btn-group" role="group" aria-label="Second group">
                        <a href="/posts/${postView.id?string.c}/edit">
                            <button type="button" class="btn btn-outline-secondary">
                                Edit
                            </button>
                        </a>
                    </div>
                </#if>
                &nbsp;
                <#if showDelete>
                    <div class="btn-group" role="group" aria-label="Third group">
                        <form method="post" action="/posts/${postView.id?string.c}/delete">
                            <button type="submit" class="btn btn-outline-danger">Delete</button>
                        </form>
                    </div>
                </#if>
            </div>
        </#if>
        <br><br>

        Author: <strong>${(postView.authorOfPost)!''}</strong>
        <br>
        ${(postView.date)!''}
        <br>
        <#--<a href="/users/${postView.user.id}/postViews">View postViews from <strong>${postView.authorOfPost!}</strong></a>-->
        <#--<br>-->
        <p><br>${(postView.text)!''}</p>
    </#if>

</#macro>