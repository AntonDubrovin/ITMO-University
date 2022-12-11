<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Created</th>
            </tr>
            </thead>
            <tbody>
            <#list users as u>
                <tr>
                    <td class="id">${u.id}</td>
                    <td class="login">${u.login}</td>
                    <td class="creationTime">${u.creationTime}</td>
                    <#if user??>
                        <td class="friends">
                            <#if u != user!>
                                <form method="post" action="">
                                    <input type="hidden" name="action" value="friend"/>
                                    <#assign val="Add friend"/>

                                    <input type="hidden" name="currentValue"
                                           value="${val}"/>
                                    <input type="submit" value="${val}">
                                </form>
                            </#if>
                        </td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>
