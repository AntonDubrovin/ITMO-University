<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#list posts as post>
        <#if post.user_id == user_id>
            <@c.makePost post true/>
        </#if>
    </#list>
</@c.page>