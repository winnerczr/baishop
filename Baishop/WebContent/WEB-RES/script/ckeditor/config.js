/*
Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function(config) {
	config.language = 'zh-cn'; // 配置语言
	config.width = '100%'; // 宽度
	config.height = '300px'; // 高度

	config.filebrowserBrowseUrl ='WEB-RES/script/ckfinder/ckfinder.html';
    
    config.filebrowserImageBrowseUrl ='WEB-RES/script/ckfinder/ckfinder.html?Type=Images';
    
    config.filebrowserFlashBrowseUrl = 'WEB-RES/script/ckfinder/ckfinder.html?Type=Flash';
    
    //config.filebrowserUploadUrl = 'WEB-RES/script/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Files';
    
    //config.filebrowserImageUploadUrl = 'WEB-RES/script/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images';
    
    //config.filebrowserFlashUploadUrl = 'WEB-RES/script/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Flash';

};
