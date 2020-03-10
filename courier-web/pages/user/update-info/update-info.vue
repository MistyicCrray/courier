<template>
	<view>
		<cmd-nav-bar back title="修改个人信息"></cmd-nav-bar>
		<cmd-page-body type="top">
			<view class="cu-form-group margin-top">
				<view class="title">昵称</view>
				<input placeholder="昵称" name="input" v-model="user.userName"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">专业</view>
				<input placeholder="专业" name="input" v-model="user.specialty"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">班级</view>
				<input placeholder="班级" name="input" v-model="user.className"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">手机号</view>
				<input placeholder="手机号" name="input" v-model="user.phone"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">电子邮件</view>
				<input placeholder="电子邮件" name="input" v-model="user.email"></input>
			</view>
			<view class="cu-bar bg-white margin-top">
				<view class="action">
					学生证
				</view>
			</view>
			<view class="cu-form-group">
				<view class="grid col-4 grid-square flex-sub">
					<view class="bg-img" v-for="(item,index) in imgList" :key="index" @tap="ViewImage" :data-url="imgList[index]">
						<image :src="userInfo.studentIdCard == '' ? imgList[index] : imgUrl + userInfo.studentIdCard" mode="aspectFill"></image>
						<view class="cu-tag bg-red" @tap.stop="DelImg" :data-index="index">
							<text class='cuIcon-close'></text>
						</view>
					</view>
					<view class="solids" @tap="ChooseImage" v-if="imgList.length < 4">
						<text class='cuIcon-cameraadd'></text>
					</view>
				</view>
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
	import {
		imgUploadUrl
	} from "@/config.js"
	import {
		imgUrl
	} from '@/config'
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
				userInfo: {},
				imgList: [],
				imgPath: "",
				token: "",
				imgUrl: imgUrl,
				phoneReg: /^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$/,
				emailReg: /^\w+@[a-z0-9]+\.[a-z]{2,4}$/,
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
			uni.getStorage({
				key: 'accessToken',
				success: function(res) {
					_this.token = res.data;
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
				if (!this.phoneReg.test(_this.user.phone)) {
					uni.showToast({
						title: '请输入正确的手机号',
						icon: 'none'
					})
					return;
				}
				if (!this.emailReg.test(_this.user.email)) {
					uni.showToast({
						title: '请输入正确的手机号',
						icon: 'none'
					})
					return;
				}
				let url = "user/updateCurUser";
				let params = _this.user;
				uni.uploadFile({
					url: imgUploadUrl + 'user/changeStudentIdCard',
					filePath: this.imgPath,
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
			ChooseImage() {
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album'], //从相册选择
					success: (res) => {
						this.imgList = res.tempFilePaths
						this.imgPath = res.tempFilePaths[0]
					}
				});
			},
			DelImg(e) {
				this.imgList.splice(e.currentTarget.dataset.index, 1)
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
