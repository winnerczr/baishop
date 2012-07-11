/**
 * 正则表达式枚举
 * 参考：http://www.ccvita.com/61.html，http://www.oicto.com/html/regex.htm
 */

var RegexpEnum = {
		
	//匹配Email地址的正则表达式
	REG_EMAIL : /^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/,
	
	//匹配用户昵称
	REG_USERNAME : /^[\u4e00-\u9fa5a-zA-Z0-9_]+$/,
	
	//验证密码 (字母或数字组成，大于6位，小于16位)
	REG_PASSWORD : /^[a-zA-Z0-9]{5,16}$/,
	
	
	//只能输入数字
	REG_NUMBER : /^[0-9]*$/,
	
	//匹配由26个英文字母组成的字符串
	REG_CHAR : /^[A-Za-z]+$/,
	
	//匹配中文字符的正则表达式
	REG_CN : /[u4e00-u9fa5]/,
	
	//匹配空白行的正则表达式,可以用来删除空白行
	REG_NULL : /^$/,
	
	REG_NOTNULL : /\S/,
	
	//匹配首尾空白字符的正则表达式,可以用来删除行首行尾的空白字符(包括空格、制表符、换页符等等)，非常有用的表达式
	REG_TRIMNULL : /^s*|s*$/,

	
	//匹配网址URL的正则表达式
	//REG_URL : /^[a-zA-z]+:/[^s]*$/,
	
	
	
	
	
	//匹配国内电话号码,匹配形式如 0511-4405222 或 021-87888822
	REG_PHONE : /^0?[0-9]{2,3}-[0-9]{7,9}$/,
	
	//匹配腾讯QQ号,腾讯QQ号从10000开始
	REG_QQ : /[1-9][0-9]{4,}/,
	
	//匹配中国邮政编码,中国邮政编码为6位数字
	REG_CHINAPOST : /^[1-9]\d{5}$/,
	
	//匹配身份证,中国的身份证为15位或18位
	REG_IDCARD : /d{15}|d{18}/,
	
	//匹配ip地址
	REG_IP : /^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)((d|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/,
	
	//匹配手机号码
	REG_MOBILE : /^[0-9]{10,12}$/
	
};



//正则，提示说明
String.prototype.isIllegal = function(f,t){
	for(var i = 0;i<f.length;i++){
		if(f[i].test(this)){
			return true;
		}
	}
	Ext.Msg.alert("提示",t);
	return false;
};


Number.prototype.isIllegal = function(f,t){
	if(this-2147483647<=0){
		var chars = ""+this;
		return chars.isIllegal(f,t);
	}
	Ext.Msg.alert("提示","数字值不得大于2147483647！");
	return false;
};


Boolean.prototype.isIllegal = function(f,t){
	var chars = ""+v;
	return chars.isIllegal(f,t);
};

