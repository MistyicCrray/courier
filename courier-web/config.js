export const formatDate = (time) => {
	var date = new Date(time); //time 为13位时间戳
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	m = m < 10 ? ('0' + m) : m;
	var d = date.getDate();
	d = d < 10 ? ('0' + d) : d;
	var h = date.getHours();
	h = h < 10 ? ('0' + h) : h;
	var minute = date.getMinutes();
	var second = date.getSeconds();
	minute = minute < 10 ? ('0' + minute) : minute;
	second = second < 10 ? ('0' + second) : second;
	return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
}

//接口地址
export const baseUrl = 'http://localhost:8080/'
// 图片地址
export const imgUrl = 'http://localhost/static/'
// 图片上传地址
export const imgUploadUrl = 'http://localhost:8080/'