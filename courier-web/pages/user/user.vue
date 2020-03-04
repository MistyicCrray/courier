<template>
	<view class="page">
		<view class="person-head" v-if="userInfo != ''" @click="fnInfoWin()">
			<cmd-avatar :src="userInfo.avatar == '' ? 'https://avatar.bbs.miui.com/images/noavatar_small.gif' : imgUrl + userInfo.avatar" @click="fnInfoWin" size="lg" :make="{'background-color': '#fff'}"></cmd-avatar>
			<view class="person-head-box">
				<view class="person-head-nickname">{{userInfo.userName == null ? '跑腿者' : userInfo.userName}}</view>
				<view class="person-head-username">ID：{{userInfo.loginName}}</view>
			</view>
		</view>
		<view class="person-head" v-if="userInfo == ''" @click="login()">
			<cmd-avatar src="https://avatar.bbs.miui.com/images/noavatar_small.gif" @click="fnInfoWin" size="lg" :make="{'background-color': '#fff'}"></cmd-avatar>
			<view class="person-head-box">
				<view class="person-head-nickname">登录</view>
			</view>
		</view>
		<view class="person-list">
			<cmd-cell-item title="我的代领" slot-left arrow @click="myCourier(0)">
				<cmd-icon type="bullet-list" size="24" color="#368dff"></cmd-icon>
			</cmd-cell-item>
			<cmd-cell-item title="我接取的" slot-left arrow @click="myCourier(1)">
				<cmd-icon type="message" size="24" color="#368dff"></cmd-icon>
			</cmd-cell-item>
			<cmd-cell-item title="版本" addon="v1.0" slot-left arrow>
				<cmd-icon type="alert-circle" size="24" color="#368dff"></cmd-icon>
			</cmd-cell-item>
		</view>
	</view>
</template>

<script>
	import cmdAvatar from "@/components/cmd-avatar/cmd-avatar.vue"
	import cmdIcon from "@/components/cmd-icon/cmd-icon.vue"
	import cmdCellItem from "@/components/cmd-cell-item/cmd-cell-item.vue"
	import {
		imgUrl
	} from '../../config'
	export default {
		components: {
			cmdAvatar,
			cmdCellItem,
			cmdIcon
		},
		data() {
			return {
				userInfo: '',
				imgUrl: imgUrl
			};
		},
		onLoad() {
			let _this = this;
			uni.getStorage({
				key: 'userInfo',
				success: function(res) {
					console.info(res.data);
					_this.userInfo = res.data;
				}
			})
		},
		created() {
			let _this = this;
			uni.getStorage({
				key: 'userInfo',
				success: function(res) {
					console.info(res.data);
					_this.userInfo = res.data;
				}
			})
		},
		methods: {
			/**
			 * 打开用户信息页
			 */
			fnInfoWin() {
				uni.navigateTo({
					url: '/pages/user/info/info'
				})
			},
			login() {
				uni.navigateTo({
					url: '/pages/user/login/login'
				})
			},
			myCourier(type) {
				uni.navigateTo({
					url: '/pages/courier-index/courier-list?type=' + type
				})
			}
		}
	}
</script>

<style>
	.page {
		height: 100vh;
	}

	.person-head {
		display: flex;
		flex-direction: row;
		align-items: center;
		height: 150px;
		padding-left: 20px;
		background: linear-gradient(to right, #365fff, #36bbff);
	}

	.person-head-box {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: flex-start;
		margin-left: 10px;
	}

	.person-head-nickname {
		font-size: 18px;
		font-weight: 500;
		color: #fff;
	}

	.person-head-username {
		font-size: 14px;
		font-weight: 500;
		color: #fff;
	}

	.person-list {
		line-height: 0;
	}
</style>
