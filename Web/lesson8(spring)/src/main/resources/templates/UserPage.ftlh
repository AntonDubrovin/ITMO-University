<#import "/spring.ftl" as spring>
<#import "commons.ftlh" as c>

<@c.page>
    <div class="userPage">
        <#if user??>
            Id: ${user.id}
            <br>Login: ${user.login}
            <br>CreationTime: ${user.creationTime}
        <#else>
            No such user
        </#if>
    </div>
</@c.page>
