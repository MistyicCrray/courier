<template>
	<view>
		<cmd-nav-bar back title="用户注册"></cmd-nav-bar>
		<cmd-page-body type="top">
			<view class="register">
				<!-- 上部分 start -->
				<view class="register-title">{{ status ? '手机快捷注册': '账号密码注册'}}</view>
				<view class="register-explain">{{ status ? '用户可通过手机验证码直接注册': '使用账号密码注册后请绑定手机号'}}</view>
				<cmd-transition name="fade-up">
					<view v-if="!status">
						<view class="register-username">
							<cmd-input v-model="account.loginName" type="text" focus maxlength="6" placeholder="请输入账号"></cmd-input>
						</view>
						<view class="register-password">
							<cmd-input v-model="account.password" type="password" displayable maxlength="26" placeholder="请输入密码"></cmd-input>
						</view>
						<view class="register-username">
							<cmd-input v-model="account.email" type="text" placeholder="请输入邮箱"></cmd-input>
						</view>
						<button class="btn-register" @tap="fnRegister">注册</button>
					</view>
				</cmd-transition>
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
				account: {
					loginName: '',
					password: '',
					email: ""
				},
				usernameReg: /^[A-Za-z0-9]+$/,
				passwordReg: /^\w+$/,
				registerAccount: false,
				mobile: {
					phone: '',
					code: ''
				},
				phoneReg: /^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$/,
				registerMobile: false,
				emailReg: /^\w+@[a-z0-9]+\.[a-z]{2,4}$/,
				safety: {
					time: 60,
					state: false,
					interval: ''
				},

				status: false // true手机注册,false账号注册
			};
		},
		methods: {
			/**
			 * 注册按钮点击执行
			 */
			fnRegister() {
				let _this = this;
				uni.showLoading({
					mask: true,
					title: '请稍后',
				});
				let url = "user/register";
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
				if (!this.emailReg.test(_this.account.email)) {
					uni.showToast({
						title: '请输入正确的邮箱',
						icon: 'none'
					})
					return;
				}
				return getRquest(url, params).then(res => {
					console.info(res);
					if (res.code == 200) {
						uni.showToast({
							title: '注册成功',
							icon: 'none'
						})
						setTimeout(() => {
							uni.navigateTo({
								url: '/pages/user/login/login'
							})
						}, 1000)
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
	.register {
		margin-top: 56upx;
		margin-right: 72upx;
		margin-left: 72upx;
	}

	.register-title {
		font-size: 56upx;
		font-weight: 500;
	}

	.register-explain {
		font-size: 28upx;
		color: #9E9E9E;
	}

	.register-phone {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		border-bottom: 2upx #dedede solid;
		margin-top: 56upx;
		margin-bottom: 40upx;
	}

	.register-phone-getcode {
		color: #3F51B5;
		text-align: center;
		min-width: 140upx;
	}

	.register-code {
		border-bottom: 2upx #dedede solid;
	}

	.register-username {
		margin-top: 56upx;
		margin-bottom: 40upx;
		border-bottom: 2upx #dedede solid;
	}

	.register-password {
		border-bottom: 2upx #dedede solid;
	}

	.btn-register {
		margin-top: 100upx;
		border-radius: 50upx;
		font-size: 16px;
		color: #fff;
		background: linear-gradient(to right, #88a1f9, #9ac6ff);
	}

	.btn-register-active {
		background: linear-gradient(to right, #365fff, #36bbff);
	}

	.btn-register-hover {
		background: linear-gradient(to right, #365fdd, #36bbfa);
	}

	button[disabled] {
		color: #fff;
	}

	.register-mode {
		text-align: center;
		margin-top: 32upx;
	}
</style>
