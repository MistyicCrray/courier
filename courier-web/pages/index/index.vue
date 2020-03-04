<template>
	<view class="page">
		<contract v-if="PageCur=='contract'"></contract>
		<courierIndex v-if="PageCur=='courierIndex'"></courierIndex>
		<addCourier v-if="PageCur=='addCourier'"></addCourier>
		<user v-if="PageCur=='user'"></user>
		<view class="cu-bar tabbar bg-black foot">
			<view :class="PageCur =='courierIndex' ? 'action text-blue' : 'action text-gray'" @click="NavChange" data-cur="courierIndex">
				<view class="cuIcon-homefill"></view> 首页
			</view>
			<view class="action text-gray add-action" @click="NavChange" data-cur="addCourier">
				<button class="cu-btn cuIcon-add bg-blue shadow"></button>
				发布
			</view>
			<view :class="PageCur =='contract' ? 'action text-blue' : 'action text-gray'" @click="NavChange" data-cur="contract">
				<view class="cuIcon-message">
				</view>
				聊天
			</view>
			<view :class="PageCur =='user' ? 'action text-blue' : 'action text-gray'" @click="NavChange" data-cur="user">
				<view class="cuIcon-my">
				</view>
				我的
			</view>
		</view>
	</view>
</template>
<script>
	import {
		getRquest
	} from "@/api.js"
	export default {
		data() {
			return {
				PageCur: 'courierIndex',
				courierList: ""
			}
		},
		methods: {
			NavChange: function(e) {
				this.PageCur = e.currentTarget.dataset.cur
			},
		}
	}
</script>
<style>
	.page {
		position: absolute;
		left: 0;
		bottom: 0;
		width: 100%;
	}
</style>
