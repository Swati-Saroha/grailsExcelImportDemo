<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-person" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="create-person" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.person}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.person}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.person}" method="POST">
                <fieldset class="form">
                    <div class="fieldcontain required">
                        <label for="firstName">First Name
                            <span class="required-indicator">*</span>
                        </label><input type="text" name="firstName" value="" required="" id="firstName">
                    </div><div class="fieldcontain required">
                    <label for="lastName">Last Name
                        <span class="required-indicator">*</span>
                    </label><input type="text" name="lastName" value="" required="" id="lastName">
                </div><div class="fieldcontain required">
                    <label for="dateOfBirth">Date Of Birth
                        <span class="required-indicator">*</span>
                    </label>
                    <input type="text" name="dateOfBirth" value="04-01-1999" required="" id="dateOfBirth">
                </div><div class="fieldcontain required">
                    <label for="numberOfChildren">Number Of Children
                        <span class="required-indicator">*</span>
                    </label><input type="number" name="numberOfChildren" value="0" required="" id="numberOfChildren">
                </div>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
