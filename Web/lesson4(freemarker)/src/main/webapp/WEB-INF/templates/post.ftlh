<#-- @ftlvariable name="post_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if post_id?is_number && post_id?has_content>
        <#assign post = c.findBy(posts, "id", post_id)!/>
        <#if post?has_content>
            <@c.makePost post true/>
        <#else>
            <h1>No such post</h1>
        </#if>
    <#else>
        <h1>No such post</h1>
    </#if>
</@c.page>