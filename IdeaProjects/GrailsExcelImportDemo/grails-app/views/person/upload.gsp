<%--
  Created by IntelliJ IDEA.
  User: ongraph
  Date: 13/09/19
  Time: 11:56 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Upload</title>
    <asset:stylesheet href="bootstrap.css"/>
    <asset:javascript src="bootstrap.js"/>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="nav" role="navigation">
    <ul>
        <li><g:link class="list" action="index">Person List</g:link></li>
    </ul>
</div>
<div id="upload-data" class="content scaffold-create" role="main">
    <div class="content scaffold-create" role="main">
        <h1>Upload Data</h1>
        <g:if test="${flash.message}"><div class="message" role="status">${flash.message}</div></g:if>
        <g:uploadForm action="doUpload">
            <fieldset class="form">
                <input type="file" name="myFile" />
            </fieldset>
            <fieldset class="buttons">
                <g:submitButton name="doUpload" value="Upload" />
            </fieldset>
        </g:uploadForm>
    </div>
</div>
</body>
</html>