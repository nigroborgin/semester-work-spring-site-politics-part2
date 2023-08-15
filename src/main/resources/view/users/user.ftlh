<#include "../base.ftlh">
<#macro title>User</#macro>

<#macro content>

    <#if userView??>
        <h2>${userView.username!''}</h2>

        <img width="200" src="${userView.pictureUrl!'/static/picture/default.bmp'}"/>
        <br>
        ID: <strong>${userView.id?string.c!''}</strong>
        <br>
        Email: <strong>${userView.email!''}</strong>
        <br>
        Role: <strong>${(userView.roleName)!''}</strong>
        <br>

        <div class="btn-toolbar position-absolute start-50 translate-middle-x"
             role="toolbar"
             aria-label="Toolbar with button groups">

            <div class="btn-group" role="group" aria-label="First group">
                <a href="/users/${userView.id?string.c!''}/posts">
                    <button class="btn btn-outline-success">
                        Show posts
                    </button>
                </a>
            </div>

            <#if showEdit>
                &nbsp;
                <div class="btn-group" role="group" aria-label="Second group">
                    <a href="/users/${userView.id?string.c!''}/edit">
                        <button class="btn btn-outline-secondary">
                            Edit Profile
                        </button>
                    </a>
                </div>
            </#if>
        </div>
    </#if>

</#macro>