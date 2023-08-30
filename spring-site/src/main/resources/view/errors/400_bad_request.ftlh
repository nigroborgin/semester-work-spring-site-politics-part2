<#include "../base.ftlh">
<#macro title>Bad request</#macro>

<#macro content>
    <h2>Bad request</h2>
    <#if message??>
        <strong>message:</strong> ${message}
        <br>
    </#if>
    <strong>url:</strong> ${url!''}
</#macro>