<#include "../base.ftlh">
<#macro title>Payload too large</#macro>

<#macro content>
    <h2>Payload too large</h2>
    <#if message??>
        <strong>message:</strong> ${message}
        <br>
    </#if>
    <strong>url:</strong> ${url!''}
</#macro>