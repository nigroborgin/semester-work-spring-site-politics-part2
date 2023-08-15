<#include "../base.ftlh">
<#macro title>Not found ${(exception.entity)!''}</#macro>

<#macro content>
    <h2>Not found ${(exception.entity)!''}</h2>
    <#if message??>
        <strong>message:</strong> ${message}
        <br>
    </#if>
    <strong>url:</strong> ${url!''}
</#macro>