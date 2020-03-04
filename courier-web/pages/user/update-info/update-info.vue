<template>
	<view>
		<cmd-nav-bar back title="修改个人信息"></cmd-nav-bar>
		<cmd-page-body type="top">
			<view class="cu-form-group margin-top">
				<view class="title">昵称</view>
				<input placeholder="昵称" name="input" v-model="user.userName"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">手机号</view>
				<input placeholder="手机号" name="input" v-model="user.phone"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">电子邮件</view>
				<input placeholder="电子邮件" name="input" v-model="user.email"></input>
			</view>
			<button class="btn-register" @tap="submit()">提交</button>
		</cmd-page-body>
	</view>
</template>
<script>
	import cmdNavBar from "@/components/cmd-nav-bar/cmd-nav-bar.vue"
	import cmdPageBody from "@/components/cmd-page-body/cmd-page-body.vue"
	import cmdTransition from "@/components/cmd-transition/cmd-transition.vue"
	import cmdInput from "@/components/cmd-input/cmd-input.vue"
	import moment from 'moment'
	import {
		getRquest
	} from "@/api.js"
	export default {
		components: {
			cmdNavBar,
			cmdPageBody,
			cmdTransition,
			cmdInput
		},
		data() {
			return {
				user: {
					"userName": "",
					"phone": "",
					"email": ""
				},
				userInfo: {}
			};
		},
		onLoad() {
			let _this = this;
			_this.loadData();
			uni.getStorage({
				key: 'userInfo',
				success(res) {
					_this.userInfo = res.data;
				}
			})
		},
		methods: {
			loadData() {
				let _this = this;
				uni.showLoading({
					mask: true,
					title: '操作中',
				});
				let url = "user/curUser";
				return getRquest(url, '').then(res => {
					console.info(res)
					_this.user = res.data
					uni.hideLoading();
				}).catch(err => {
					console.info(err);
					uni.hideLoading();
				})
			},
			submit() {
				let _this = this;
				uni.showLoading({
					mask: true,
					title: '操作中',
				});
				let url = "user/updateCurUser";
				let params = _this.user;
				return getRquest(url, params).then(res => {
					console.info(res)
					if (res.code == 200) {
						uni.showToast({
							title: "修改成功",
							icon: 'none'
						})
						_this.userInfo.userName = _this.user.userName;
						_this.userInfo.phone = _this.user.phone;
						_this.userInfo.email = _this.user.email;
						uni.setStorage({
							key: 'userInfo',
							data: _this.userInfo
						})
					}
					uni.hideLoading();
				}).catch(err => {
					console.info(err);
					uni.hideLoading();
				})
			},
		}
	}
</script>

<style>
	.btn-register {
		margin-top: 100upx;
		border-radius: 50upx;
		font-size: 16px;
		color: #fff;
		background: linear-gradient(to right, #88a1f9, #9ac6ff);
	}
</style>
