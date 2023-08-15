<#include "../base.ftlh">
<#macro title>Users</#macro>

<#macro content>

    <h2>Users</h2>
    <div class="container-fluid">
        <table class="table table-bordered w-auto" border='1'>
            <thead>
            <tr>
                <th>
                    <center>Picture</center>
                </th>
                <th>
                    <center>Username</center>
                </th>
                <th>
                    <center>Role</center>
                </th>
                <th>
                    <center>Posts</center>
                </th>
            </tr>
            </thead>
            <#if userList??>
                <tbody>
                <#list userList as u>
                    <tr>
                        <td>
                            <center>
                                <img height="60" src="${u.pictureUrl!'/static/picture/default.bmp'}"/>
                            </center>
                        </td>
                        <td>
                            <center>
                                <a href="/users/${u.id?string.c}">
                                    <#if u.username??>
                                        <#assign usernameLength = u.username?length - 1>
                                    </#if>
                                    <#if (usernameLength >= 25)>
                                        ${u.username?substring(0,25)}...
                                    <#else>
                                        ${u.username}
                                    </#if>
                                </a>
                            </center>
                        </td>
                        <td>
                            <center>
                                ${u.roleName}
                            </center>
                        </td>
                        <td>
                            <center>
                                <div class="btn-group" role="group" aria-label="Second group">
                                    <a href="/users/${u.id?string.c}/posts">
                                        <button class="btn btn-outline-success btn-sm">
                                            Show posts
                                        </button>
                                    </a>
                                </div>
                            </center>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </#if>
        </table>
    </div>
</#macro>