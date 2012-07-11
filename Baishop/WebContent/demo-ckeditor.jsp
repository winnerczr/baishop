<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ckeditor demo</title>
<script type="text/javascript" src="/WEB-RES/script/jquery/jquery-1.5.2.js"></script>
<script type="text/javascript" src="/WEB-RES/script/ckeditor/ckeditor.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		CKEDITOR.replace( 'editor1',{
			contentsCss : 'assets/output_xhtml.css'
		});
	});
</script>
</head>
<body>


	<form action="/" method="post">
		<div style="width: 850px">
			<p>
				<label for="editor1"> ckeditor示例</label>
			</p>
			<textarea cols="80" id="editor1" name="editor1" rows="10">
			</textarea>
		</div>
		<p>
			<input type="submit" value="Submit" />
		</p>
	</form>

</body>
</html>