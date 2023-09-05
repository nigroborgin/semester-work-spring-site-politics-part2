<#import "/spring.ftl" as spring/>

<#include "../base.ftlh">
<#macro title>Set Post info</#macro>

<#macro content>

    <h2>Set Post info</h2>

    <#if showNew>
        <div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="First group">
            <a href="/posts/new">
                <button type="button" class="btn btn-outline-primary">
                    Create new post
                </button>
            </a>
        </div>
        <br><br>
    </#if>

    <#if showDelete>
        <div class="btn-group position-absolute start-50 translate-middle-x" role="group" aria-label="Second group">
            <form method="post" action="/posts/${postView.id?string.c}/delete">
                <button type="submit" class="btn btn-outline-danger">Delete</button>
            </form>
        </div>
        <br><br>
    </#if>


    <form method="post">
        <@spring.bind path="postForm"/>

        <div class="form-group">
            <label>Title:
                <br>
                <input class="form-control-sm ${(titleError??)?string('is-invalid', '')}" type="text" name="title" value="${(postView.title)!''}"/>
                <#if titleError??>
                    <div class="invalid-feedback">
                        ${titleError}
                    </div>
                </#if>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label>View Author:
                <br>
                <input class="form-control-sm ${(authorError??)?string('is-invalid', '')}" type="text" name="authorOfPost" readonly value="${(postView.authorOfPost)!''}"/>
                <#if authorError??>
                    <div class="invalid-feedback">
                        ${authorError}
                    </div>
                </#if>
            </label>
        </div>
        <br>

        <div class="form-group">
            <label for="formControlTextarea">Text of post:
                <br>
                <textarea class="form-control-sm ${(textError??)?string('is-invalid', '')}"
                          name="text"
                          id="formControlTextarea"
                          rows="10"
                          cols="60">${postView.text!''}</textarea>
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </label>
        </div>
        <br>

        <div class="btn-group position-absolute start-50 translate-middle-x">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </form>

</#macro>