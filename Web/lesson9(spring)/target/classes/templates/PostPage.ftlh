<#-- @ftlvariable name="commentForm" type="ru.itmo.wp.form.CommentForm" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if viewedPost??>
        <@c.post post=viewedPost/>

        <#if user??>
            <div class="add-comment form-box">
                <div class="header">
                    Add Comment:
                </div>

                <form method="post" action="">
                    <input type="hidden" name="action" value="addComment"/>
                    <div class="field">
                        <div class="name">
                            <label for="text">Your comment:</label>
                        </div>

                        <div class="value">
                            <textarea id="text" name="text"></textarea>
                        </div>
                        <@c.error "commentForm.text"/>
                    </div>
                    <div class="button-field">
                        <input type="submit" value="Add Comment"/>
                    </div>

                </form>
            </div>
        </#if>

        <h3>Comments: </h3>
        <#list viewedPost.comments as comment>
            <@c.comment comment=comment/>
        </#list>
    <#else>
        <h1>No such post</h1>
    </#if>
</@c.page>