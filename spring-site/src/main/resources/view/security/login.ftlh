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
            <label>Email:
                <br>
                <@spring.formInput path="loginData.username" attributes='class=\"form-control\" placeholder=\"Enter email\"'/>
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
        <br><br>
        <small>login with:</small>
        <br>
        <!--<button type="button" class="btn btn-outline-light">-->
            <a href="${vkCodeUrl}">
                <svg width="25" height="25" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M0 12.504c0-5.43 0-8.145 1.687-9.833C3.374.984 6.09.984 11.52.984h.96c5.43 0 8.146 0 9.833 1.687C24 4.36 24 7.074 24 12.504v.96c0 5.43 0 8.146-1.687 9.833-1.687 1.687-4.402 1.687-9.833 1.687h-.96c-5.43 0-8.146 0-9.833-1.687C0 21.61 0 18.895 0 13.464v-.96Z" fill="#07F"></path>
                    <path d="M12.77 18.274c-5.47 0-8.59-3.75-8.72-9.99h2.74c.09 4.58 2.11 6.52 3.71 6.92v-6.92h2.58v3.95c1.58-.17 3.24-1.97 3.8-3.95h2.58c-.43 2.44-2.23 4.24-3.51 4.98 1.28.6 3.33 2.17 4.11 5.01h-2.84c-.61-1.9-2.13-3.37-4.14-3.57v3.57h-.31Z" fill="#fff"></path>
                </svg>
            </a>
        <!--</button>-->
    </form>

</#macro>