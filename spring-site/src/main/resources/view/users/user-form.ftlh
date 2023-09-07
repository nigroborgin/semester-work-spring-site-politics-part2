<#import "/spring.ftl" as spring/>

<#include "../base.ftlh">
<#macro title>Set User info</#macro>

<#macro content>

    <h2>Set User info</h2>

    <#if userView??>

        <form method="post" enctype="multipart/form-data">
            <@spring.bind path="userEdit"/>

            <img width="100" src="${userView.pictureUrl!'/static/picture/default.bmp'}"/>
            <input type="file" name="picture"/>
            <br>

            ID: ${userView.id?string.c}
            <br>

            <div class="form-group">
                <label>Username:
                    <input class="form-control-sm" type="text" name="username" value="${userView.username}"/>
                </label>
            </div>
            <br>
            <#if showEditPassword>
                <div class="form-group">
                    <label>Password:
                        <input class="form-control-sm" name="password" type="password"/>
                    </label>
                </div>
                <br>
            </#if>

            <div class="form-group">
                <label>Email:
                    <input class="form-control-sm" name="email" type="text" value="${userView.email}"/>
                </label>
            </div>
            <br>
            <#if showEditRole>
                <div class="position-absolute start-50 translate-middle-x">
                    Role:
                    <div class="form-check">
                        <input class="form-check-input"
                               name="roleName"
                               type="radio" id="role1"
                               value="ROLE_ADMIN"
                               <#if userView.roleName == 'ROLE_ADMIN'>checked="checked"</#if>/>
                        <label class="form-check-label" for="role1">Admin</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input"
                               name="roleName"
                               type="radio" id="role2"
                               value="ROLE_USER"
                               <#if userView.roleName == 'ROLE_USER'>checked="checked"</#if> />
                        <label class="form-check-label" for="role2">User</label>
                    </div>
                </div>
                <br><br>
                <br><br>
            </#if>

            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-primary position-absolute start-50 translate-middle-x">Save</button>
        </form>

    </#if>

</#macro>
