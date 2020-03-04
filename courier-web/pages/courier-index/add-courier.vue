<template name="addCourier">
	<scroll-view scroll-y class="page">
		<form>
			<view class="cu-form-group margin-top">
				<view class="title">快递名称</view>
				<input placeholder="快递名称" name="input" v-model="courier.courier"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">取件码</view>
				<input placeholder="取件码(我们不会将你的取件码泄露)" name="input" v-model="courier.pickupCode"></input>
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
			<button class="btn-register" @tap="submit()">提交</button>
		</form>
	</scroll-view>
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
				}
			};
		},
		methods: {
			TimeChange(e) {
				let _this = this;
				this.time = e.detail.value
			},
			DateChange(e) {
				let _this = this;
				this.date = e.detail.value
			},
			SwitchA(e) {
				let _this = this;
				this.switchA = e.detail.value
			},
			submit() {
				let _this = this;
				if (_this.courier.courier == '') {
					uni.showToast({
						title: '请输入快递名称',
						icon: 'none'
					})
					return;
				}
				if (_this.courier.pickupCode == '') {
					uni.showToast({
						title: '请输入取件码',
						icon: 'none'
					})
					return;
				}
				if (_this.courier.address == '') {
					uni.showToast({
						title: '请输入地址',
						icon: 'none'
					})
					return;
				}
				if (_this.courier.phone == '') {
					uni.showToast({
						title: '请输入联系电话',
						icon: 'none'
					})
					return;
				}
				if (_this.courier.remark == '') {
					uni.showToast({
						title: '请输入备注',
						icon: 'none'
					})
					return;
				}
				uni.showLoading({
					mask: true,
					title: '操作中',
				});
				_this.courier.cabinetTime = _this.date + " " + _this.time;
				_this.courier.isBig = _this.switchA ? '1' : '0';
				let url = "courierOrder/add";
				let params = _this.courier;
				console.info(_this.date + " " + _this.time)
				return getRquest(url, params).then(res => {
					console.info(res)
					if (res.code == 200) {
						uni.showToast({
							title: "添加成功",
							icon: 'none'
						})
					}
					uni.hideLoading();
				}).catch(err => {
					console.info(err);
					uni.hideLoading();
				})
			}
		}
	}
</script>

<style>
	.page {
		height: 100vh;
	}

	.cu-form-group .title {
		min-width: calc(4em + 15px);
	}

	.btn-register {
		margin-top: 100upx;
		border-radius: 50upx;
		font-size: 16px;
		color: #fff;
		background: linear-gradient(to right, #88a1f9, #9ac6ff);
	}
</style>
