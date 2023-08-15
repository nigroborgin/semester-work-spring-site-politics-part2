<#include "../base.ftlh">
<#macro title>Internal server error</#macro>

<#macro content>
    <h2>Internal server error</h2>
    <#if message??>
        <strong>message:</strong> ${message}
        <br>
    </#if>
    <strong>url:</strong> ${url!''}
</#macro>