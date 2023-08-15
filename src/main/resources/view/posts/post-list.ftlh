<#include "../base.ftlh">
<#macro title>Posts</#macro>

<#macro content>

    <h2>Posts <#if nameByUser??>by <i>${nameByUser}</i></#if></h2>

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


    <#if postList??>
        <div class="card-columns col-md">
            <#list postList as post>

                <#if post.text??>
                    <#assign postLength = post.text?length - 1>
                </#if>
                <#if post.authorOfPost??>
                    <#assign authorLength = post.authorOfPost?length - 1>
                </#if>

                <span class="card" style="width: 18rem;">
                    <#--                    <img class="card-img-top" src=".../100px200/" alt="Card image cap">-->
                    <div class="card-body">
                        <h5 class="card-title">${post.title!}</h5>
                        <p class="card-text">
                            <#if (postLength >= 60)>
                                ${post.text?substring(0,60)}...
                            <#else>
                                ${post.text}
                            </#if>
                        </p>
                        <p class="card-text"><small class="text-muted">${(post.date)!''}</small></p>
                        <div class="btn-toolbar"
                             role="toolbar"
                             aria-label="Toolbar with button groups">

                             <div class="btn-group position-relative start-50 translate-middle-x"
                                  role="group"
                                  aria-label="First group">

                                <a href="/posts/${post.id?string.c}">
                                    <button class="btn btn-outline-success">
                                        Read
                                    </button>
                                </a>

                                <#if showEdit>
                                    &nbsp;
                                    <a href="/posts/${post.id?string.c}/edit">
                                        <button class="btn btn-outline-secondary">
                                            Edit
                                        </button>
                                    </a>
                                </#if>

                                 <#if showDelete>
                                     &nbsp;
                                     <form method="post" action="/posts/${post.id?string.c}/delete">
                                        <button class="btn btn-outline-danger" type="submit">
                                            Delete
                                        </button>
                                     </form>
                                 </#if>
                            </div>
                        </div>
                    </div>

                    <div class="card-footer bg-transparent border-success">
                        Author:
                        <#if (authorLength >= 25)>
                            ${post.authorOfPost?substring(0,25)}...
                        <#else>
                            ${post.authorOfPost}
                        </#if>
                    </div>
                </span>
                <br>
            </#list>
        </div>
    </#if>
</#macro>