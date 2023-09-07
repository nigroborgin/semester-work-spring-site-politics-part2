<#import "/spring.ftl" as spring/>

<#include "../base.ftlh">
<#macro title>Login</#macro>

<#macro content>

    <h2>Login</h2>

    <#if loginError??>
        <div style="display:inline-block" class="alert alert-danger" role="alert">
            ${loginError}
        </div>
    </#if>

    <form method="post" action="/login">
        <@spring.bind path="loginData"/>
        <div class="form-group">
            <label>Username:
                <br>
                <@spring.formInput path="loginData.username" attributes='class=\"form-control\" placeholder=\"Enter username\"'/>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>Password:
                <br>
                <@spring.formPasswordInput path="loginData.password" attributes='class=\"form-control\" placeholder=\"Enter password\"'/>
            </label>
        </div>
        <br>
        <div class="form-group">
        <label>
            Save user <@spring.formCheckbox path="loginData.saveuser" attributes='class=\"form-check-input\"'/>
        </label>
        </div>
        <br>

        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary">Login</button>
    </form>

</#macro>