axios.defaults.baseURL = '/EggShellBackGroud';
if (localStorage.length > 0 && sessionStorage.length <= 0) {

	sessionStorage.setItem("userId", resultdata.data.userInfo.id);
	sessionStorage.setItem("username", resultdata.data.userInfo.username);
	sessionStorage.setItem("accessToken", resultdata.data.accessToken);
}

// http request 拦截器
axios.interceptors.request.use(
	config => {

		if (sessionStorage.accessToken) { // 判断是否存在token，如果存在的话，则每个http header都加上token
			//			config.headers.Authorization = `token ${localStorage.JWT_TOKEN}`;
			//			config.headers.accessToken = `token ${localStorage.accessToken}`;
			config.headers.accessToken = sessionStorage.accessToken;

		}
		return config;
	},
	err => {
		return Promise.reject(err);
	});

// http response 拦截器
axios.interceptors.response.use(
	response => {
		return response;
	},
	error => {
		if (error.response) {
			console.log('axios:' + error.response.status);
			switch (error.response.status) {
				case 401:
					parent.location.href = "login.html?returnUrl=" + top.location.href;
					break;
				default:
					if (error.response.data.message) {
						alert(error.response.data.message);
					} else {
						alert("出错啦");
					}
					break;
			}
		}
		return Promise.reject(error.response.data); // 返回接口返回的错误信息
	});

Vue.prototype.$http = axios;