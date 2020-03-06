/* 根据名称获取参数 */
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]);
	return null;
}

function getUrl() {
	return 'http://localhost/main/';
}
// ajax拦截器
$.ajaxSetup({
	type: "POST",
	error: function(jqXHR, textStatus, errorMsg) { // 出错时默认的处理函数
		console.info(jqXHR.status)
		switch (jqXHR.status) {
			case (500):
				alert("服务器系统内部错误");
				break;
			case (401):
				parent.location.href = '../login.html'
				break;
			case (403):
				alert("当前用户没有权限");
				break;
			case (408):
				alert("请求超时");
				break;
			default:
				alert("未知错误");
		}
	}
});
