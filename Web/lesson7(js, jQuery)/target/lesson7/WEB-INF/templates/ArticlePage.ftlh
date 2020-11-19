<#-- @ftlvariable name="text" type="java.lang.String" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="article form-box">
        <div class="header">Article</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="register"/>
                <div class="field">
                    <div class="name">
                        <label for="title">Title</label>
                    </div>
                    <div class="value">
                        <input id="title" name="title" value="${title!}"/>
                    </div>
                </div>
                <div class="text">
                    <div class="name">
                        <label for="text" style="margin-left: 9rem">Text</label>
                    </div>
                    <label>
                        <textarea name="text" style="resize: none; width:15rem; height:10rem; margin-left: 12rem"></textarea>
                    </label>
                </div>
                <div class="error">
                    ${error!}
                </div>
                <div class="button-field">
                    <input type="submit" value="Create">
                </div>
            </form>
        </div>
    </div>

    <script>
        $(function () {
            $(".article form").submit(function () {
                const title = $(this).find("input[name='title']").val();
                const text = $(this).find("textarea[name='text']").val();
                const user = $(this).find("input[name='user']").val();
                const data = {
                    action: "createArticle",
                    title,
                    text,
                    user
                }

                ajax(data, $(this));

                return false;
            });
        })
    </script>
</@c.page>
