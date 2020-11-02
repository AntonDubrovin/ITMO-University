<#-- @ftlvariable name="text" type="java.lang.String" -->
<#-- @ftlvariable name="uri" type="java.lang.String" -->
<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" type="text/css" href="/css/normalize.css">
        <link rel="stylesheet" type="text/css" href="/css/style.css">
        <link rel="stylesheet" type="text/css" href="/css/r.css+css/g.css+css/b.css">
        <link rel="icon" href="img/favicon.ico"/>
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="/"><img src="/img/logo.png" alt="Codeforces" title="Codeforces"/></a>
        <div class="languages">
            <a href="#"><img src="/img/gb.png" alt="In English" title="In English"/></a>
            <a href="#"><img src="/img/ru.png" alt="In Russian" title="In Russian"/></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user true/>
                |
                <a href="Logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/home", "name": "Home"},
            {"href": "/users", "name": "Users"},
            {"href": "/misc/help", "name": "Help"}
            ]/>
            <ul class="menu">
                <#list items as item>
                    <#if item.href == uri>
                        <li><a class="underlinedItem" href="${item.href}">${item.name}</a></li>
                    <#else>
                        <li><a href="${item.href}">${item.name}</a></li>
                    </#if>
                </#list>
            </ul>
            <form class="search" method="get" action="#">
                <input title="Search">
            </form>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts?reverse as post>
            <#assign text=post.text?truncate(100, "...")/>
            <section>
                <div class="header">Post#${post.id}</div>
                <div class="body">${text}</div>
                <div class="footer">
                    <a href="post?post_id=${post.id}">View all »</a>
                </div>
            </section>
        </#list>
    </aside>
</#macro>


<#macro footer>
    <footer>
        <a href="#">Codeforces</a> &copy; 2010-2020 by Mike Mirzayanov
    </footer>
</#macro>

<#macro userlink user nameOnly=true>
    <#if !nameOnly>
        <a href="/user?handle=${user.handle}" style="text-decoration: none; color: ${user.color}">${user.handle}</a>
    <#else>
        <a href="/user?handle=${user.handle}">${user.handle}</a>
    </#if>
</#macro>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function findPrevUser user>
    <#list users?reverse as u>
        <#if (u.id < user.id)>
            <#return u/>
        </#if>
    </#list>
</#function>

<#function findNextUser user>
    <#list users as u>
        <#if (u.id > user.id)>
            <#return u/>
        </#if>
    </#list>
</#function>

<#macro makePost post isReduce>
    <#if post?has_content>
        <#assign user=findBy(users, "id", post.user_id)/>
        <#assign text=post.text/>
        <#if isReduce>
            <#assign text=text?truncate(300, "...")/>
        </#if>
        <article>
            <div class="title"><a href="post?post_id=${post.id}"
                                  style="text-decoration: none; color: #3B5998">${post.title}</a></div>
            <div class="information">
                By
                <a class="author"><@userlink user false/></a>
            </div>
            <div class="body">
                ${text}
                <br>
                <br><a href="post?post_id=${post.id}" class="deploy" style="color: blue">View all »</a>
            </div>
            <ul class="attachment">
                <li>Announcement of <a href="#" style="text-decoration: none; color: grey">Attachment</a></li>
            </ul>
            <div class="footer">
                <div class="left">
                    <a href="#"><img src="/img/voteup.png" title="Vote Up" alt="Vote Up"/></a>
                    <span class="positive-score">+${post.likes}</span>
                    <a href="#"><img src="/img/votedown.png" title="Vote Down" alt="Vote Down"/></a>
                </div>
                <div class="right">
                    <img src="/img/user_16x16.png" alt="imageUser"/>
                    <a href="user?handle=${user.handle}" class="linkUser">${user.handle}</a>
                    <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                    <a class="time">${post.time} days ago</a>
                    <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                    <a href="#" class="comments">${post.comments}</a>
                </div>
            </div>
        </article>
    <#else>
        <h1>No such post</h1>
    </#if>
</#macro>