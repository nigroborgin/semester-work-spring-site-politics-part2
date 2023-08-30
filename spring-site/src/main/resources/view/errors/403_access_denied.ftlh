<#include "../base.ftlh">
<#macro title>Access denied</#macro>

<#macro content>
    <h2>Access denied</h2>
    <#if message??>
        <strong>message:</strong> ${message}
        <br>
    </#if>
    <strong>url:</strong> ${url!''}
</#macro>