<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        auth = true
    >
<#--    <#if user??>-->
<#--        <#if user.role.name == "ROLE_ADMIN">-->
<#--            <#assign isAdmin = true>-->
<#--        <#elseif user.role.name == "ROLE_USER">-->
<#--            <#assign isUser = true>-->
<#--        </#if>-->
<#--    </#if>-->
<#else>
    <#assign
        name = "unknown"
        auth = false
        isAdmin = false
        isUser = false
    >
</#if>