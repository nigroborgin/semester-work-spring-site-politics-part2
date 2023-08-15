<#include "../base.ftlh">
<#macro title>Method not allowed</#macro>

<#macro content>
    <h2>Method not allowed</h2>
    <#if message??>
        <strong>message:</strong> ${message}
        <br>
    </#if>
    <strong>url:</strong> ${url!''}
</#macro>