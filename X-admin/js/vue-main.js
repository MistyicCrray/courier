axios.defaults.baseURL = '/main';
// http request 拦截器
axios.interceptors.request.use(

	config => {
		if (sessionStorage.getItem("token")) {
			config.headers.accessToken = sessionStorage.getItem("token");

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