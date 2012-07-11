// ******************************************************
// 包含文件 用法： $import('../include/mian.js', 'js');
// 				$import('../style/style.css', 'css');
// ******************************************************
function $import(path, type) {
	if (type == "css") {
		document.write("<link href='" + path + "' rel='stylesheet' type='text/css'></link>");
	} else {
		document.write("<script src='" + path + "' type='text/javascript'></script>");
	}
}

// ******************************************************
// 判断类型
// ******************************************************
function isAlien(a) {
	return isObject(a) && typeof a.constructor != 'function';
}

function isArray(a) {
	return isObject(a) && a.constructor == Array;
}

function isBoolean(a) {
	return typeof a == 'boolean';
}

function isEmpty(o) {
	var i, v;
	if (isObject(o)) {
		for (i in o) {
			v = o[i];
			if (isUndefined(v) && isFunction(v)) {
				return false;
			}
		}
	}
	return true;
}

function isFunction(a) {
	return typeof a == 'function';
}

function isNull(a) {
	return typeof a == 'object' && !a;
}

function isNumber(a) {
	return typeof a == 'number' && isFinite(a);
}

function isObject(a) {
	return (a && typeof a == 'object') || isFunction(a);
}

function isString(a) {
	return typeof a == 'string';
}

function isUndefined(a) {
	return typeof a == 'undefined';
}


String.prototype.getBytes = function() {    
    var cArr = this.match(/[^\x00-\xff]/ig);    
    return this.length + (cArr == null ? 0 : cArr.length);    
}