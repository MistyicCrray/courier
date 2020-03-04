<template>
	<view>
		<cmd-nav-bar back title="信息设置"></cmd-nav-bar>
		<cmd-page-body type="top">
			<cmd-transition name="fade-up">
				<view>
					<cmd-cel-item title="头像" slot-right arrow @click="chooseImage">
						<cmd-avatar :src="userInfo.avatar == '' ? 'https://avatar.bbs.miui.com/images/noavatar_small.gif' : userInfo.avatar"></cmd-avatar>
					</cmd-cel-item>
					<cmd-cel-item title="昵称" :addon="userInfo.userName" arrow></cmd-cel-item>
					<cmd-cel-item title="邮箱" :addon="userInfo.email" arrow></cmd-cel-item>
					<cmd-cel-item title="修改个人信息" @click="updateInfo()" arrow></cmd-cel-item>
					<cmd-cel-item title="修改密码" @click="fnClick('')" arrow></cmd-cel-item>
					<button class="btn-logout" @click="logout()">退出登录</button>
				</view>
			</cmd-transition>
		</cmd-page-body>
	</view>
</template>

<script>
	import cmdNavBar from "@/components/cmd-nav-bar/cmd-nav-bar.vue"
	import cmdPageBody from "@/components/cmd-page-body/cmd-page-body.vue"
	import cmdTransition from "@/components/cmd-transition/cmd-transition.vue"
	import cmdCelItem from "@/components/cmd-cell-item/cmd-cell-item.vue"
	import cmdAvatar from "@/components/cmd-avatar/cmd-avatar.vue"
	import {
		getRquest
	} from "@/api.js"
	import {
		imgUrl
	} from '@/config'
	import {
		imgUploadUrl
	} from '@/config'
	export default {
		components: {
			cmdNavBar,
			cmdPageBody,
			cmdTransition,
			cmdCelItem,
			cmdAvatar
		},

		data() {
			return {
				avatar: '',
				userInfo: '',
				token: "",
				imgUrl: imgUrl
			};
		},
		onLoad() {
			this.getUserInfo();
		},
		methods: {
			/**
			 * 点击触发
			 * @param {Object} type 跳转页面名或者类型方式
			 */
			fnClick() {
				uni.navigateTo({
					url: '/pages/user/forgot/reset-passwor'
				})
			},
			getUserInfo() {
				let _this = this;
				uni.getStorage({
					key: "userInfo",
					success: function(res) {
						_this.userInfo = res.data
						_this.userInfo.avatar = _this.imgUrl + res.data.avatar
					}
				})
				uni.getStorage({
					key: 'accessToken',
					success: function(res) {
						_this.token = res.data;
					}
				})
				console.info(_this.userInfo)
			},
			chooseImage() {
				let _this = this;
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album'], //从相册选择
					success: (res) => {
						console.info(res.tempFilePaths[0]);
						_this.userInfo.avatar = res.tempFilePaths[0]
						_this.avatar = res.tempFilePaths[0]
						let params = {
							"img": res.tempFilePaths[0]
						}
						uni.uploadFile({
							url: imgUploadUrl + 'user/changeAvatar',
							filePath: res.tempFilePaths[0],
							name: 'img',
							header: {
								'accessToken': _this.token,
							},
							success: function(uploadFileRes) {
								console.log(uploadFileRes.data);
							},
							fail(err) {
								console.info(err)
							}
						});
						// return getRquest(url, params).then(res => {
						// 	console.info(res);
						// }).catch(err => {
						// 	console.info(err);
						// 	uni.hideLoading();
						// })
					}
				});
			},
			logout() {
				uni.clearStorage();
				uni.navigateTo({
					url: '/pages/user/login/login'
				})
			},
			updateInfo() {
				uni.navigateTo({
					url: '../update-info/update-info'
				})
			}
		}
	}
</script>

<style>
	.btn-logout {
		margin-top: 100upx;
		width: 80%;
		border-radius: 50upx;
		font-size: 16px;
		color: #fff;
		background: linear-gradient(to right, #365fff, #36bbff);
	}

	.btn-logout-hover {
		background: linear-gradient(to right, #365fdd, #36bbfa);
	}
</style>
