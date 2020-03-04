import {
	baseUrl
} from './config'
class AJAX {
	constructor() {
		this.baseUrl = baseUrl;
	}
	get(url, params) {
		let obj = {
			url: url,
			method: 'GET',
			params: params
		};
		return new Promise((resolve, reject) => {
			this._request(obj, resolve, reject)
		})
	}
	post(url, params) {
		let obj = {
			url: url,
			method: 'POST',
			params: params
		};
		return new Promise((resolve, reject) => {
			this._request(obj, resolve, reject)
		})
	}
	_request(obj, resolve, reject) {
		let that = this;
		let token = "";
		uni.getStorage({
			key: 'accessToken',
			success: function(res) {
				token = res.data;
			}
		})
		uni.request({
			url: that.baseUrl + obj.url,
			data: obj.params,
			method: obj.method,
			header: {
				'content-type': 'application/x-www-form-urlencoded',
				'accessToken': token
			},
			success: (res) => {
				resolve(res.data)
			},
			fail: (err) => {
				uni.navigateTo({
					url: '/pages/user/login/login'
				})
			}
		})
	}
}
const ajax = new AJAX()
export default ajax
