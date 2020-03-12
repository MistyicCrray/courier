<template>
	<view>
		<cmd-nav-bar back :title="findType[typeS]"></cmd-nav-bar>
		<cmd-page-body type="top">
			<scroll-view scroll-x class="bg-white nav text-center">
				<view class="cu-item" :class="index==TabCur?'text-blue cur':''" v-for="(item,index) in 2" :key="index" @tap="tabSelect"
				 :data-id="index">
					{{tab[index]}}
				</view>
			</scroll-view>
			<view class="cu-card dynamic" :class="isCard?'no-card':''">
				<view class="cu-item shadow" v-for="(item, index) in courier" @click="detail(item.id)" @longpress="del(item.id, item.status, index)">
					<view class="cu-list menu-avatar">
						<view class="cu-item">
							<view class="cu-avatar round lg">
								<image class="cu-avatar" :src="item.avatar == '' ? 'https://avatar.bbs.miui.com/images/noavatar_small.gif' : imgUrl + item.avatar"
								 mode=""></image>
							</view>
							<view class="content flex-sub">
								<view>{{item.user_name}}</view>
								<view class="text-gray text-sm flex justify-between">
									{{item.create_time | dateFilter}}
									<view :class="item.status == 1 ? 'cu-tag line-red' : 'cu-tag line-blue'">
										{{item.status | statusFilter}}
									</view>
								</view>
							</view>
						</view>
					</view>
					<view class="text-content">
						{{item.remark}}
					</view>
				</view>
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
	}
	from "@/api.js"
	import {
		imgUrl
	} from '@/config'
	let status = ['待接取', '已接取', '已完成'];
	export default {
		components: {
			cmdNavBar,
			cmdPageBody,
			cmdTransition,
			cmdInput
		},
		data() {
			return {
				isCard: false,
				courier: [],
				cardCur: 0,
				TabCur: 0,
				tab: ['待付款', '已付款'],
				scrollLeft: 0,
				swiperList: [{
					id: 0,
					type: 'image',
					url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big84000.jpg'
				}, {
					id: 1,
					type: 'image',
					url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big37006.jpg',
				}, {
					id: 2,
					type: 'image',
					url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big39000.jpg'
				}, {
					id: 3,
					type: 'image',
					url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big10001.jpg'
				}, {
					id: 4,
					type: 'image',
					url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big25011.jpg'
				}, {
					id: 5,
					type: 'image',
					url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big21016.jpg'
				}, {
					id: 6,
					type: 'image',
					url: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big99008.jpg'
				}],
				dotStyle: false,
				towerStart: 0,
				direction: '',
				findType: ['我的代领', '我接取的'],
				typeS: 0,
				imgUrl: imgUrl,
				findTypeNum: 0,
				loadingText: '加载中...',
				page: 1,
				counts: 6,
				loadMore: false,
			};
		},
		onLoad(option) {
			let _this = this;
			_this.typeS = option.type;
			_this.loadData(option.type, 0);
		},
		onReachBottom() {
			let _this = this;
			_this.page++;
			_this.loadData(_this.typeS, _this.findTypeNum);
		},
		methods: {
			IsCard(e) {
				this.isCard = e.detail.value
			},
			loadData(type, payStatus) {
				uni.showLoading({
					mask: true,
					title: '加载中',
				});
				if (this.loadMore) {
					uni.showToast({
						title: '没有更多了',
						icon: 'none'
					});
					return
				}
				let url = "courierOrder/findList";
				let params = {
					"pageNum": this.page,
					"size": this.counts
				}
				let userId = ''
				uni.getStorage({
					key: 'userInfo',
					success: function(res) {
						userId = res.data.id;
					}
				})
				if (type == 0) {
					params.userId = userId;
				} else {
					params.orderUserId = userId;
				}
				params.payStatus = payStatus;
				return getRquest(url, params).then(res => {
					console.info(res);
					if (res.data.total == this.courier.length) {
						uni.hideLoading();
						this.loadMore = true;
						return;
					}
					this.courier = this.courier.concat(res.data.rows);
					uni.stopPullDownRefresh()
					uni.hideLoading();
				}).catch(err => {
					console.info(err);
					uni.hideLoading();
				})
			},
			detail(id) {
				let _this = this;
				if (_this.typeS == 0) {
					uni.navigateTo({
						url: '/pages/courier-index/courier-edit?id=' + id
					})
				} else {
					uni.navigateTo({
						url: '/pages/courier-index/courier-detail?id=' + id
					})
				}
			},
			tabSelect(e) {
				let _this = this;
				_this.TabCur = e.currentTarget.dataset.id;
				_this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60;
				console.info(e.target.dataset.id);
				_this.findTypeNum = e.target.dataset.id;
				_this.courier = [];
				this.loadMore = false;
				_this.loadData(_this.typeS, e.target.dataset.id);
			},
			del(id, status, index) {
				console.info(id)
				let _this = this;
				if (status == 0) {
					uni.showModal({
						title: '提示',
						content: '是否要删除改订单?',
						success: function(res) {
							if (res.confirm) {
								let url = "courierOrder/delete/" + id;
								let params = {}
								return getRquest(url, params).then(res => {
									_this.courier.splice(index, 1);
									uni.showToast({
										title: "删除成功",
										icon: "none"
									})
								}).catch(err => {
									console.info(err);
								})
							}
						}
					});
				}
			}
		},
		filters: {
			dateFilter(value) {
				return moment(value).format("YYYY-MM-DD HH:mm:ss")
			},
			statusFilter(value) {
				return status[value];
			}
		}
	}
</script>

<style>
	.page {
		height: 100vh;
	}
</style>
