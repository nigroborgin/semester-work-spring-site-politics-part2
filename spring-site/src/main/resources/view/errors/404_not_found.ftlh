<#include "../base.ftlh">
<#macro title>Not found ${entity!''}</#macro>

<#macro content>
    <h2>Not found ${entity!''}</h2>
    <#if message??>
        <strong>message:</strong> ${message}
        <br>
    </#if>
    <strong>url:</strong> ${url!''}
</#macro>