<template>
	<view class="page">
		<cmd-nav-bar back title="查看详情"></cmd-nav-bar>
		<cmd-page-body type="top">
			<view class="cu-form-group margin-top">
				<view class="title">快递名称</view>
				<view>{{courier.courier}}</view>
			</view>
			<view class="cu-form-group">
				<view class="title">取件码</view>
				<view>{{courier.pickupCode}}</view>
			</view>
			<view class="cu-form-group">
				<view class="title">送往地址</view>
				<view>{{courier.address}}</view>
			</view>
			<view class="cu-form-group">
				<view class="title">联系电话</view>
				<view>{{courier.phone}}</view>
			</view>
			<view class="cu-form-group">
				<view class="title">入柜日期</view>
				<view>
					{{date}}
				</view>
			</view>
			<view class="cu-form-group">
				<view class="title">选择时间</view>
				<view>
					{{time}}
				</view>
			</view>
			<view class="cu-form-group">
				<view class="title">是否大件</view>
				{{courier.isBig == '0' ? '否' : '是'}}
			</view>
			<view class="cu-form-group">
				<view class="title">备注</view>
				<view>{{courier.remark}}</view>
			</view>
			<view class="cu-bar bg-white tabbar border shop foot">
				<button class="action" open-type="contact" @click="toContract">
					<view class="cuIcon-message text-green">
					</view>
					联系
				</button>
				<!-- <view class="action">
				<view class="cuIcon-cart">
					<view class="cu-tag badge">99</view>
				</view>
				购物车
			</view> -->
				<!-- <view class="bg-orange submit">加入购物车</view> -->
				<view class="bg-red submit" @click="accept()">接单</view>
			</view>
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
				index: -1,
				time: '12:01',
				date: '2018-12-25',
				switchA: false,
				radio: 'A',
				modalName: null,
				courier: {
					"pickupCode": '',
					"remark": "",
					"cabinetTime": '',
					"courier": '',
					"isBig": '',
					"address": '',
					'phone': ''
				},
				id: '',
				userInfo: ''
			};
		},
		onLoad(option) {
			let _this = this;
			_this.id = option.id;
			_this.loadData(option.id);
			uni.getStorage({
				key: "userInfo",
				success(res) {
					_this.userInfo = res.data;
				}
			})
		},
		methods: {
			TimeChange(e) {
				let _this = this;
				_this.time = e.detail.value
			},
			DateChange(e) {
				let _this = this;
				_this.date = e.detail.value
			},
			SwitchA(e) {
				let _this = this;
				_this.switchA = e.detail.value
			},
			loadData(id) {
				let _this = this;
				uni.showLoading({
					mask: true,
					title: '操作中',
				});
				let url = "courierOrder/findById/" + id;
				return getRquest(url, '').then(res => {
					console.info(res)
					if (res.code == 200) {
						_this.courier = res.data;
						_this.date = moment(res.data.cabinetTime).format("YYYY-MM-DD")
						_this.time = moment(res.data.cabinetTime).format("HH:mm")
						_this.switchA = res.data.isBig == 1 ? true : false;
					}
					uni.hideLoading();
				}).catch(err => {
					console.info(err);
					uni.hideLoading();
				})
			},
			accept() {
				let _this = this;
				uni.showLoading({
					mask: true,
					title: '操作中',
				});
				if (_this.courier.userId == _this.userInfo.id) {
					uni.showToast({
						title: '不能接取自己的订单',
						icon: 'none'
					})
					return;
				}
				let url = "courierOrder/accept/" + _this.id;
				return getRquest(url, '').then(res => {
					console.info(res)
					if (res.code == 200) {
						uni.showToast({
							title: '接取成功',
							icon: 'none'
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
			toContract() {
				let _this = this;
				if (_this.courier.userId == _this.userInfo.id) {
					uni.showToast({
						title: '你不能联系自己',
						icon: 'none'
					})
					return;
				}
				uni.navigateTo({
					url: "../contract/contract-info?toUserId=" + _this.courier.userId
				})
			}
		}
	}
</script>

<style>
	.page {
		height: 100%;
	}

	.bottom {
		position: absolute;
		left: 0;
		bottom: 0;
		width: 100%;
	}
</style>
