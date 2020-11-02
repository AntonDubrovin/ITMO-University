<#import "./common.ftlh" as c>

<@c.page>
    <#list posts?reverse as post>
        <#assign curUser = c.findBy(users, "id", post.user_id)/>
        <@c.makePost post true/>
    </#list>
</@c.page>