<#include "../base.ftlh">
<#macro title>Registration</#macro>

<#macro content>

    <h2>Registration</h2>

    ${message!}

    <form action="/reg" method="post">
        <@spring.bind path="regData"/>

        <div class="form-group">
            <label>Username:
                <br>
                <@spring.formInput path="regData.username" attributes='class=\"form-control\" placeholder=\"Enter username\"'/>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>Email:
                <br>
                <@spring.formInput path="regData.email" attributes='class=\"form-control\" placeholder=\"Enter email\"'/>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>Password:
                <br>
                <@spring.formPasswordInput path="regData.password" attributes='class=\"form-control\" placeholder=\"Enter password\"'/>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>Password:
                <br>
                <@spring.formPasswordInput path="regData.password2" attributes='class=\"form-control\" placeholder=\"Repeat password\"'/>
            </label>
        </div>
        <br>

        <button type="submit" class="btn btn-primary">Register</button>
    </form>

</#macro>