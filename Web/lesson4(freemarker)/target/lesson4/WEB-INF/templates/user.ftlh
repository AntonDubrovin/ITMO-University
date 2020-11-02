<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if handle??>
        <#assign u = c.findBy(users, "handle", handle)!/>
    <#else>
        <#if user_id?has_content && user_id?is_number>
                <#assign u = c.findBy(users, "id", user_id)!/>
        </#if>
    </#if>

    <#if u?has_content>
        <div class="user">
            <div class="curUser">
                <@c.userlink u false/>
            </div>
            <div class="params">
                <li>id: ${u.id}</li>
                <li>handle: ${u.handle}</li>
                <li>name: ${u.name}</li>
                <li><a href="posts?user_id=${u.id}">Записи в блоге(${posts?filter(x -> x.user_id == u.id)?size})</a>
                </li>
            </div>
            <a class="profilePhoto" href="#"><img src="/img/no-title.jpg" alt="title" title="title"></a>
        </div>

        <#if c.findPrevUser(u)?has_content>
            <a class="arrowWithLink" href="user?handle=${c.findPrevUser(u).handle}"
               style="text-decoration: none; color: #3B5998">←</a>
        <#else>
            <div class="arrowWithoutLink">←</div>
        </#if>

        <#if c.findNextUser(u)?has_content>
            <a class="arrowWithLink" href="user?handle=${c.findNextUser(u).handle}"
               style="text-decoration: none; color: #3B5998">→</a>
        <#else>
            <div class="arrowWithoutLink">→</div>
        </#if>
    <#else>
        No such user
    </#if>
</@c.page>