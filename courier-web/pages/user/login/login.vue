<template>
	<view>
		<cmd-nav-bar back title="用户登录" rightText="注册" @rightText="fnRegisterWin"></cmd-nav-bar>
		<cmd-page-body type="top">
			<view class="login">
				<!-- 上部分 start -->
				<view class="login-title">{{ status ? '手机快捷登录': '使用账号密码登录'}}</view>
				<view class="login-explain">{{ status ? '已注册用户可通过手机验证码直接登录': '未注册用户可通过点击右上角进行注册'}}</view>
				<!-- 上部分 end -->
				<!-- 账号表单登录 start -->
				<cmd-transition name="fade-up">
					<view v-if="!status">
						<view class="login-username">
							<cmd-input v-model="account.loginName" type="text" focus maxlength="26" placeholder="请输入账号"></cmd-input>
						</view>
						<view class="login-password">
							<cmd-input v-model="account.password" type="password" displayable maxlength="26" placeholder="请输入密码"></cmd-input>
						</view>
						<button class="btn-login" @tap="fnLogin">登录</button>
					</view>
				</cmd-transition>
				<view class="register-mode" @tap="findPassword">忘记密码?</view>
			</view>
		</cmd-page-body>
	</view>
</template>

<script>
	import cmdNavBar from "@/components/cmd-nav-bar/cmd-nav-bar.vue"
	import cmdPageBody from "@/components/cmd-page-body/cmd-page-body.vue"
	import cmdTransition from "@/components/cmd-transition/cmd-transition.vue"
	import cmdInput from "@/components/cmd-input/cmd-input.vue"
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
				// 账号登录部分数据
				account: {
					loginName: '',
					password: ''
				},
				usernameReg: /^[A-Za-z0-9]+$/,
				passwordReg: /^\w+$/,
				loginAccount: false,
				// 手机登录部分数据
				mobile: {
					phone: '',
					code: ''
				},
				phoneReg: /^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$/,
				loginMobile: false,
				// 验证码
				safety: {
					time: 60,
					state: false,
					interval: ''
				},
				status: false // true手机登录,false账号登录
			};
		},

		methods: {
			findPassword() {
				uni.navigateTo({
					url: '/pages/user/forgot/forgot'
				})
			},
			/**
			 * 登录按钮点击执行
			 */
			fnLogin() {
				let _this = this;
				uni.showLoading({
					mask: true,
					title: '请稍后',
				});
				let url = "user/login";
				let params = _this.account;
				if (_this.account.loginName == '') {
					uni.showToast({
						title: '请输入用户名',
						icon: 'none'
					})
					return;
				}
				if (_this.account.password == '') {
					uni.showToast({
						title: '请输入密码',
						icon: 'none'
					})
					return;
				}
				return getRquest(url, params).then(res => {
					console.info(res);
					if (res.code == 200) {
						uni.showToast({
							title: '登录成功',
							icon: 'none'
						})
						uni.setStorage({
							key: "userInfo",
							data: res.data.userInfo
						})
						uni.setStorage({
							key: "accessToken",
							data: res.data.accessToken
						})
						uni.navigateTo({
							url: '/pages/index/index'
						})
					} else {
						uni.showToast({
							title: res.message,
							icon: 'none'
						})
					}

					uni.hideLoading();
				}).catch(err => {
					console.info(err);
					uni.hideLoading();
				})
			},
			/**
			 * 跳转注册页面
			 */
			fnRegisterWin() {
				uni.navigateTo({
					url: "/pages/user/register/register"
				})
			}
		},

		beforeDestroy() {
			/**
			 * 关闭页面清除轮询器
			 */
			clearInterval(this.safety.interval);
		}
	}
</script>

<style>
	.login {
		margin-top: 56upx;
		margin-right: 72upx;
		margin-left: 72upx;
	}

	.login-title {
		font-size: 56upx;
		font-weight: 500;
	}

	.login-explain {
		font-size: 28upx;
		color: #9E9E9E;
	}

	.login-phone {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		border-bottom: 2upx #dedede solid;
		margin-top: 56upx;
		margin-bottom: 40upx;
	}

	.login-phone-getcode {
		color: #3F51B5;
		text-align: center;
		min-width: 140upx;
	}

	.login-code {
		border-bottom: 2upx #dedede solid;
	}

	.login-username {
		margin-top: 56upx;
		margin-bottom: 40upx;
		border-bottom: 2upx #dedede solid;
	}

	.login-password {
		border-bottom: 2upx #dedede solid;
	}

	.btn-login {
		margin-top: 100upx;
		border-radius: 50upx;
		font-size: 16px;
		color: #fff;
		background: linear-gradient(to right, #88a1f9, #9ac6ff);
	}

	.btn-login-active {
		background: linear-gradient(to right, #365fff, #36bbff);
	}

	.btn-login-hover {
		background: linear-gradient(to right, #365fdd, #36bbfa);
	}

	button[disabled] {
		color: #fff;
	}

	.login-mode {
		text-align: center;
		margin-top: 32upx;
	}

	.register-mode {
		text-align: center;
		margin-top: 32upx;
	}
</style>
