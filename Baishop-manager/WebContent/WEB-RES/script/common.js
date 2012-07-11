/**
 * 包含文件 用法：$import('../include/mian.js', 'js'); $import('../style/style.css', 'css');
 * @param path 路径
 * @param type 类型
 */
function $import(path, type) {
	if (type == "css") {
		document.write("<link href='" + path
				+ "' rel='stylesheet' type='text/css'></link>");
	} else {
		document.write("<script src='" + path
				+ "' type='text/javascript'></script>");
	}
}

/**
 * 格式化金额
 * @param n 保留小数点
 * @returns {String} 返回格式化后的金额字符串
 */
Number.prototype.fomatMoney = function(n) {
	var s = this;
	n = n > 0 && n <= 20 ? n : 2;
	s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
	var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
	t = "";
	for ( var i = 0; i < l.length; i++) {
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
	}
	return t.split("").reverse().join("") + "." + r;
};


/**
 * 在数组里插入元素
 * @param index 插入位置
 * @param obj 插入对象
 * @returns
 */
Array.prototype.insert = function(index, obj){
	if(index<0 || !obj)return this;
	if(index==0){
		//return this.unshift(obj);
		var a = new Array(obj);
		return a.concat(this.slice(index));
	}else if(index>this.length){
		this.push(obj);
	}else
		return this.slice(0, index).concat(obj, this.slice(index));
};


/**
 * 重写onItemSelect和onItemDeselect的bug方法
 */
Ext.apply(Ext.view.AbstractView.prototype, {  
	onItemSelect : function(record) {
        var node = this.getNode(record);
        if(node)
        	Ext.fly(node).addCls(this.selectedItemCls);
    },
    onItemDeselect : function(record) {
        var node = this.getNode(record);
        if(node)
        	Ext.fly(node).removeCls(this.selectedItemCls);
    }
}); 

/**
 * Ext 表单验证扩展
 */
Ext.apply(Ext.form.field.VTypes,{
	// 年龄
	"age" : function(_v) {
		if (/^\d+$/.test(_v)) {
			var _age = parseInt(_v);
			if (_age < 200)
				return true;
			else
				return false;
		} else
			return false;
	},
	'ageText' : '年龄格式出错！！格式例如：20',
	'ageMask' : /[0-9]/i,
	
	// 密码验证
	"repassword" : function(_v, field) {
		if (field.initialPassField) {
			var psw = Ext.getCmp(field.initialPassField);
			return (_v == psw.getValue());
		}
		return true;
	},
	"repasswordText" : "密码输入不一致！！",
	"repasswordMask" : /[a-z0-9]/i,
	
	// 邮政编码
	"postcode" : function(_v) {
		return /^[1-9]\d{5}$/.test(_v);
	},
	"postcodeText" : "该输入项目必须是邮政编码格式，例如：226001",
	"postcodeMask" : /[0-9]/i,

	// IP地址验证
	"ip" : function(_v) {
		return /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
				.test(_v);

	},
	"ipText" : "该输入项目必须是IP地址格式，例如：222.192.42.12",
	"ipMask" : /[0-9\.]/i,
	
	// 固定电话及小灵通
	"telephone" : function(_v) {
		return /(^\d{3}\-\d{7,8}$)|(^\d{4}\-\d{7,8}$)|(^\d{3}\d{7,8}$)|(^\d{4}\d{7,8}$)|(^\d{7,8}$)/
				.test(_v);
	},
	"telephoneText" : "该输入项目必须是电话号码格式，例如：0513-89500414,051389500414,89500414",
	"telephoneMask" : /[0-9\-]/i,
	
	// 手机
	"mobile" : function(_v) {
		return /^1[35][0-9]\d{8}$/.test(_v);
	},
	"mobileText" : "该输入项目必须是手机号码格式，例如：13485135075",
	"mobileMask" : /[0-9]/i,
	
	// 身份证
	"IDCard" : function(_v) {
		// return
		// /(^[0-9]{17}([0-9]|[Xx])$)|(^[0-9]{17}$)/.test(_v);
		var area = {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江",
			31 : "上海",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外"
		};
		var Y, JYM;
		var S, M;
		var idcard_array = new Array();
		idcard_array = _v.split("");
		// 地区检验
		if (area[parseInt(_v.substr(0, 2))] == null) {
			this.IDCardText = "身份证号码地区非法!!,格式例如:32";
			return false;
		}
		// 身份号码位数及格式检验
		switch (_v.length) {
		case 15:
			if ((parseInt(_v.substr(6, 2)) + 1900) % 4 == 0
					|| ((parseInt(_v.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(_v
							.substr(6, 2)) + 1900) % 4 == 0)) {
				ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
			} else {
				ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
			}
			if (ereg.test(_v))
				return true;
			else {
				this.IDCardText = "身份证号码出生日期超出范围,格式例如:19860817";
				return false;
			}
			break;
		case 18:
			// 18位身份号码检测
			// 出生日期的合法性检查
			// 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
			// 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
			if (parseInt(_v.substr(6, 4)) % 4 == 0
					|| (parseInt(_v.substr(6, 4)) % 100 == 0 && parseInt(_v
							.substr(6, 4)) % 4 == 0)) {
				ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
			} else {
				ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
			}
			if (ereg.test(_v)) {// 测试出生日期的合法性
				// 计算校验位
				S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10]))
						* 7
						+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
						* 9
						+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
						* 10
						+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
						* 5
						+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
						* 8
						+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
						* 4
						+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
						* 2
						+ parseInt(idcard_array[7])
						* 1
						+ parseInt(idcard_array[8])
						* 6
						+ parseInt(idcard_array[9]) * 3;
				Y = S % 11;
				M = "F";
				JYM = "10X98765432";
				M = JYM.substr(Y, 1);// 判断校验位
				// alert(idcard_array[17]);
				if (M == idcard_array[17]) {
					return true; // 检测ID的校验位
				} else {
					this.IDCardText = "身份证号码末位校验位校验出错,请注意x的大小写,格式例如:201X";
					return false;
				}
			} else {
				this.IDCardText = "身份证号码出生日期超出范围,格式例如:19860817";
				return false;
			}
			break;
		default:
			this.IDCardText = "身份证号码位数不对,应该为15位或是18位";
			return false;
			break;
		}
	},
	"IDCardText" : "该输入项目必须是身份证号码格式，例如：32082919860817201x",
	"IDCardMask" : /[0-9xX]/i,
	
	// 数字
	"Number" : function(_v) {
		if (/^\d+$/.test(_v)) {
			return true;
		} else
			return false;
	},
	'NumberText' : '只能输入数字',
	'NumberMask' : /[0-9]/i
});


/**
 * form提交时的错误处理
 */
Ext.formFailure = function(form, action){
    switch (action.failureType) {
    case Ext.form.action.Action.CLIENT_INVALID:
        Ext.Msg.alert('提示', '输入内容不通过');
        break;
    case Ext.form.action.Action.CONNECT_FAILURE:
    	if(action.response.status==403){
    		Ext.Msg.alert('提示', '您没有权限访问');
    	}else{
        	Ext.Msg.alert('提示', '连接出错');
    	}
        break;
    case Ext.form.action.Action.SERVER_INVALID:
       Ext.Msg.alert('提示', action.result.msg);
       break;
	}	
};


/**
 * ajax提交时的错误处理
 */
Ext.ajaxFailure = function(response, options){
	if(response.status==403){
		Ext.Msg.alert('提示', '您没有权限访问');
	}else{
		Ext.Msg.alert("提示", response.responseText);
	}
};


/**
 * 通用Ext.AJAX提交请求
 */
function postUrl(_url, _params, _msg, _callback){
	if(_msg){
		Ext.MessageBox.show({
			title:'提示',
			width : 250,
			wait : true,
			progress : true,  
	      closable : true,  
	      waitConfig : {  
	          interval : 200
	      },
			msg: _msg + '...',
			icon : Ext.Msg.INFO
		});
	}
	// 发送请求
	readRemoteResponse(_url, _params, function(responseText){
		var json = Ext.decode(responseText);
		if(_callback)_callback(json);
		else{
			if (json.success) {
				Ext.Msg.alert("提示", "操作成功!");
			}else{
				Ext.Msg.alert("提示", json.msg);
			}
		}
	});
};


/**
 * Ajax读远程数据封装
 */
function readRemoteResponse(_url, _params, _callback){
	// 发送请求
	Ext.Ajax.request({
		url : _url,
		params : _params,
		method : "POST",
		success : function(response) {
			if(_callback)_callback(response.responseText);
		},
		failure : function(response, options) {
			Ext.Msg.alert("提示", response.responseText);
		}
	});
};