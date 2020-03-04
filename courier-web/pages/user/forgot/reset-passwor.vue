<template>
	<view>
		<cmd-nav-bar back title="密码修改"></cmd-nav-bar>
		<cmd-page-body type="top">
			<cmd-transition name="fade-up">
				<view class="forgot">
					<view class="forgot-code">
						<cmd-input v-model="mobile.oldPassword" type="number" placeholder="请输入旧密码"></cmd-input>
					</view>
					<view class="forgot-password">
						<cmd-input v-model="mobile.passwordOne" type="password" maxlength="26" placeholder="请输入新密码"></cmd-input>
					</view>
					<view class="forgot-password">
						<cmd-input v-model="mobile.passwordTwo" type="password" maxlength="26" placeholder="请再次确认新密码"></cmd-input>
					</view>
					<button class="btn-forgot" hover-class="btn-forgot-hover" @tap="fnForgot">提交</button>
				</view>
			</cmd-transition>
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
				mobile: {
					oldPassword: '',
					passwordOne: '',
					passwordTwo: ''
				},
			};
		},
		methods: {
			/**
			 * 提交按钮点击执行
			 */
			fnForgot() {
				let _this = this;
				if (_this.mobile.oldPassword == '') {
					uni.showToast({
						title: "请输入旧密码",
						icon: "none"
					})
					return;
				}
				if (_this.mobile.passwordOne == '') {
					uni.showToast({
						title: "请输入新密码",
						icon: "none"
					})
					return;
				}
				if (_this.mobile.passwordTwo == '') {
					uni.showToast({
						title: "请输入确认秘密",
						icon: "none"
					})
					return;
				}
				if (_this.mobile.passwordTwo != _this.mobile.passwordOne) {
					uni.showToast({
						title: "新密码与确认秘密不一致",
						icon: "none"
					})
					return;
				}
				let params = _this.mobile;
				let url = "user/resetPassword";
				return getRquest(url, params).then(res => {
					console.info(res);
					uni.showToast({
						title: res.data,
						icon: "none"
					})
					if (res.code == 200) {
						uni.navigateTo({
							url: '../login/login'
						})
					}
				}).catch(err => {
					console.info(err);
				})
			},
		},
	}
</script>

<style>
	.forgot {
		margin-top: 118upx;
		margin-right: 72upx;
		margin-left: 72upx;
	}

	.forgot-phone {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		border-bottom: 2upx #dedede solid;
		margin-top: 118upx;
		margin-bottom: 40upx;
	}

	.forgot-phone-getcode {
		color: #3F51B5;
		text-align: center;
		min-width: 140upx;
	}

	.forgot-password,
	.forgot-code {
		margin-bottom: 40upx;
		border-bottom: 2upx #dedede solid;
	}

	.btn-forgot {
		margin-top: 100upx;
		border-radius: 50upx;
		font-size: 16px;
		color: #fff;
		background: linear-gradient(to right, #88a1f9, #9ac6ff);
	}

	.btn-forgot-active {
		background: linear-gradient(to right, #365fff, #36bbff);
	}

	.btn-forgot-hover {
		background: linear-gradient(to right, #365fdd, #36bbfa);
	}

	button[disabled] {
		color: #fff;
	}
</style>
