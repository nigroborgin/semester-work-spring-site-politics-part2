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
                <input
                    class="form-control ${(usernameError??)?string('is-invalid', '')}"
                    name="username"
                    value="<#if regData.username??>${regData.username}</#if>"
                    placeholder="Enter username"/>
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>Email:
                <br>
                <input
                        class="form-control ${(emailError??)?string('is-invalid', '')}"
                        name="email"
                        value="<#if regData.email??>${regData.email}</#if>"
                        placeholder="Enter email"/>
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>Password:
                <br>
                <input
                        class="form-control ${(passwordError??)?string('is-invalid', '')}"
                        name="password"
                        value="<#if regData.password??>${regData.password}</#if>"
                        placeholder="Enter password"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>Password:
                <br>
                <input
                        class="form-control ${(password2Error??)?string('is-invalid', '')}"
                        name="password2"
                        value="<#if regData.password2??>${regData.password2}</#if>"
                        placeholder="Enter password"/>
                <#if password2Error??>
                <div class="invalid-feedback">
                    ${password2Error}
                </div>
            </#if>
            </label>
        </div>
        <br>

        <button type="submit" class="btn btn-primary">Register</button>
    </form>

</#macro>