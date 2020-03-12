<template>
	<view>
		<cmd-nav-bar back title="修改"></cmd-nav-bar>
		<cmd-page-body type="top">
			<view class="cu-form-group margin-top">
				<view class="title">快递名称</view>
				<input placeholder="快递名称" name="input" v-model="courier.courier"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">取件码</view>
				<input placeholder="取件码" name="input" v-model="courier.pickupCode"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">送往地址</view>
				<input placeholder="送往地址" name="input" v-model="courier.address"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">联系电话</view>
				<input placeholder="联系电话" name="input" v-model="courier.phone"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">入柜日期</view>
				<picker mode="date" :value="date" start="2015-09-01" end="2020-09-01" @change="DateChange">
					<view class="picker">
						{{date}}
					</view>
				</picker>
			</view>
			<view class="cu-form-group">
				<view class="title">选择时间</view>
				<picker mode="time" :value="time" start="09:01" end="21:01" @change="TimeChange">
					<view class="picker">
						{{time}}
					</view>
				</picker>
			</view>
			<view class="cu-form-group">
				<view class="title">是否大件</view>
				<switch @change="SwitchA" :class="switchA?'checked':''" :checked="switchA?true:false"></switch>
			</view>
			<view class="cu-form-group align-start">
				<view class="title">备注</view>
				<textarea maxlength="-1" v-model="courier.remark" :disabled="modalName!=null" placeholder="备注"></textarea>
			</view>
			<view class="cu-bar bg-white tabbar border shop foot">
				<button class="action" open-type="contact" @click="toContract">
					<view class="cuIcon-message text-green">
					</view>
					联系
				</button>
				<view class="bg-blue submit" @click="check()" v-if="courier.pay_status == 0">支付</view>
				<view class="bg-red submit" @click="submit()" v-if="courier.status == 0">修改</view>
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
	import {
		baseUrl
	} from "@/config.js"
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
				userInfo: {}
			};
		},
		onLoad(option) {
			if (option.id != undefined) {
				this.loadData(option.id);
			}
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
				_this.courier.id = id;
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
			submit() {
				let _this = this;
				uni.showLoading({
					mask: true,
					title: '操作中',
				});
				_this.courier.cabinetTime = _this.date + " " + _this.time;
				_this.courier.isBig = _this.switchA ? '1' : '0';
				let url = "courierOrder/update";
				let params = _this.courier;
				console.info(_this.date + " " + _this.time)
				return getRquest(url, params).then(res => {
					console.info(res)
					if (res.code == 200) {
						uni.showToast({
							title: "修改成功",
							icon: 'none'
						})
					}
					uni.hideLoading();
				}).catch(err => {
					console.info(err);
					uni.hideLoading();
				})
			},
			check() {
				let _this = this;
				let url = baseUrl + "courierOrder/alipayH5Pay/" + _this.courier.id
				uni.navigateTo({
					url: "../web-view/web-view?path=" + url
				})
			},
			toContract() {
				let _this = this;
				uni.navigateTo({
					url: "../contract/contract-info?toUserId=" + _this.courier.orderUserId
				})
			}
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
