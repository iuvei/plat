var share = {};
var base = {};
/**
 * 字符串转数组。
 * 
 * @param str
 *            字符串对象
 * @return 数组
 */
share.str2Ary= function (str) {
	var map = $.parseJSON(str);
 	var list = [];
    for (var key in map) {
        list.push([key, map[key]]);
    }
    return list;
};

/**
 * map转数组。
 * 
 * @param {Map}map
 *            map对象
 * @return 数组
 */
share.map2Ary = function (map) {
    var list = [];
    for (var key in map) {
        list.push([key, map[key]]);
    }
    return list;
};

/**
 * 获取map中的值。
 * 
 * @param {String}value
 *            要渲染的值
 * @param {Map}mapping
 *            Map对象
 * @param {String}defaultText
 *            没有对应的key时的默认值
 * @return {String}对应的value值
 */
share.map = function (value, mapping, defaultText) {
    return mapping[value] || (defaultText === undefined || defaultText === null ? value : defaultText);
};

share.addSelectHtml=function(list,value){
	var html ='';
    for(var i=0;i<list.length;i++){
    	if(value != undefined && value !=''){
    		if(list[i][0]>value){
    			html+="<option value='"+list[i][0]+"'>"+list[i][1]+"</option>";
    		}
    	}else{
    		html+="<option value='"+list[i][0]+"'>"+list[i][1]+"</option>";
    	}
    }
    return html;
};

base.decimal2 = function(value){
	return /^-?\d+\.?\d{0,2}$/.test(value);
}

base.validPwd = function(value){
	return /^[0-9a-zA-Z]{6,8}$/.test(value);
}

base.validphone = function(value){
	return /^1\d{10}$/.test(value);
}

base.validemail = function(value){
	return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+\.([a-zA-Z])+$/.test(value);
}

base.validcard = function(value){
	return /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/.test(value);
}

base.validaccount =function(value){
	return /[0-9A-Za-z]{6,10}$/.test(value);
}

base.validch =function(value){
	return /^[\u4e00-\u9fa5]{0,}$/.test(value);
}

base.validinteger =function(value){
	return /^[1-9]*[1-9][0-9]*$/.test(value);
}

Function.prototype.method = function(name, func) {
	this.prototype[name] = func;
	return this;
};
if(!String.prototype.trim){ //判断下浏览器是否自带有trim()方法
	String.method('trim', function() {
		return this.replace(/^s+|s+$/g, '');
	});
	String.method('ltrim', function() {
		return this.replace(/^s+/g, '');
	});
	String.method('rtrim', function() {
		return this.replace(/s+$/g, '');
	});
} 