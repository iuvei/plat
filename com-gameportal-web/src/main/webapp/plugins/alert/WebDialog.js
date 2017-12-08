// 普通弹出对话框
function MyAlert(title, content, icon) {
    var dialog = art.dialog({
        id: 'mybox1',
        lock: true,
        content: content,
        title: title,
        fixed: true,
        resize: false,
        icon: icon,
        ok: true,
        width: 240
    });
}

function warn(content){
 var dialog = art.dialog({
        id: 'mybox1',
        lock: true,
        content: content,
        title: '提示',
        fixed: true,
        resize: false,
        icon: 'warn',
        ok: true,
        width: 240
    });
}

function success(content){
 var dialog = art.dialog({
        id: 'mybox1',
        lock: true,
        content: content,
        title: '提示',
        fixed: true,
        resize: false,
        icon: 'success',
        ok: true,
        width: 240
    });
}

function callBack(content,callbackUrl){
	art.dialog({
        lock: true,
        content: content,
        title: '提示',
        fixed: true,
        resize: false,
        icon: 'success',
        width: 240,
        button: [
            {
                name: '确定',
                callback: function () {
                    location.href = callbackUrl;
                }
            }
        ]
    });
}

function fail(content){
 var dialog = art.dialog({
        id: 'mybox1',
        lock: true,
        content: content,
        title: '提示',
        fixed: true,
        resize: false,
        icon: 'fail',
        ok: true,
        width: 240
    });
}

// 框架弹出对话框
function MyAlertThroughIframe(title, content, icon) {
    var throughBox = art.dialog.through;
    throughBox({
        id: 'mybox1',
        lock: true,
        content: content,
        title: title,
        icon: icon,
        resize: false,
//        cancel: false,
        ok: true
    });
}

// 弹出对话框并回调
function MyAlertCallBack(title, content, icon,callbackUrl,ok,cancle) {
   var dialog = art.dialog({
        lock: true,
        content: content,
        title: title,
        fixed: true,
        resize: false,
        icon: icon,
        width: 240,
        button: [
            {
                name: ok,
                callback: function () {
                    location.href = callbackUrl;
                }
            },{
            	name: cancle,
                callback: function () {
                	dialog.close();
                }
            }
        ]
    });
}

// 普通弹出对话框并设置宽度
function MyAlertWidth(title, content, icon) {
    var dialog = art.dialog({
        id: 'mybox1',
        lock: true,
        content: content,
        title: title,
        fixed: true,
        resize: false,
        icon: icon,
        ok: true,
        width: 400
    });
}
