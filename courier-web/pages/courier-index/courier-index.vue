<template name="courierIndex">
	<you-scroll class="page" ref="scroll" :scroll-top="scrollTop" @onPullDown="onPullDown" @onScroll="onScroll" @onLoadMore="onLoadMore">
		<swiper class="screen-swiper" :class="dotStyle?'square-dot':'round-dot'" :indicator-dots="true" :circular="true"
		 :autoplay="true" interval="5000" duration="500">
			<swiper-item v-for="(item,index) in swiperList" :key="index">
				<image :src="item.url" mode="aspectFill" v-if="item.type=='image'"></image>
				<video :src="item.url" autoplay loop muted :show-play-btn="false" :controls="false" objectFit="cover" v-if="item.type=='video'"></video>
			</swiper-item>
		</swiper>
		<view class="grid col-2 padding-sm">
			<view class="padding-sm" @click="isBigF('1')">
				<view class="padding radius text-center shadow-blur" :class="isBig == '1' ? 'bg-gradual-red' : 'bg-gradual-orange'">
					<view class="text-lg">大件物品</view>
					<view class="margin-top-sm text-Abc"></view>
				</view>
			</view>
			<view class="padding-sm" @click="isBigF('0')">
				<view class="padding radius text-center shadow-blur" :class="isBig == '0' ? 'bg-gradual-red' : 'bg-gradual-orange'">
					<view class="text-lg">小件物品</view>
					<view class="margin-top-sm text-Abc"></view>
				</view>
			</view>
		</view>
		<!-- <button @tap="backTop()" class="topBtn cuIcon-top"></button> -->
		<view class="cu-card dynamic my-bottom" :class="isCard?'no-card':''">
			<view class="cu-item shadow" v-for="item in courier" @click="detail(item.id)">
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
					<view style="float: right;margin-right: 10upx;">
						<view class='cu-tag line-red'>￥{{item.price}}元</view>
					</view>
					{{item.remark}}
				</view>
			</view>
		</view>
	</you-scroll>
			
</template>

<script>
	import moment from 'moment'
	import youScroll from '@/components/you-scroll/you-scroll'
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
			youScroll
		},
		data() {
			return {
				isCard: false,
				courier: [],
				cardCur: 0,
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
				isLoading: false,
				isEndPage: false,
				page: 1,
				counts: 6,
				imgUrl: imgUrl,
				isBig: "",
				display: false,
				flag: false,
				scrollTop: 0
			};
		},
		created() {
			this.loadData();
		},
		onPageScroll(e) {
			console.info(e);
		},
		methods: {
			onPullDown(done) {
				let _this = this;
				_this.page = 1;
				_this.isBig = ""
				setTimeout(() => {
					_this.courier = [];
					this.isEndPage = false;
					_this.loadData();
					done(); // 完成刷新
				}, 1000)
			},
			onLoadMore(e) {
				let _this = this;
				_this.page++;
				setTimeout(() => {
					_this.loadData();
				}, 1000 * 1)
				
			},
			IsCard(e) {
				this.isCard = e.detail.value
			},
			loadData() {
				let _this = this;
				this.isLoading = true;
				if (this.isEndPage) {
					uni.showToast({
						title: '没有更多了',
						icon: 'none'
					});
					return
				}
				uni.showLoading({
					mask: true,
					title: '加载中',
				});
				let url = "courierOrder/findList";
				let params = {
					"pageNum": this.page,
					"size": this.counts
				}
				if (_this.isBig != "") {
					params.isBig = _this.isBig;
				}
				return getRquest(url, params).then(res => {
					console.info(res)
					this.courier =this.courier.concat(res.data.rows)
					if (res.data.total == this.courier.length) {
						this.isEndPage = true;
						uni.hideLoading();
						return;
					}
					uni.hideLoading();
				}).catch(err => {
					console.info(err);
					uni.hideLoading();
				})
			},
			detail(id) {
				uni.navigateTo({
					url: '/pages/courier-index/courier-detail?id=' + id
				})
			},
			lower() {
				if (!this.isLoading) {
					this.page += 1;
					this.loadData();
				}
			},
			isBigF(value) {
				let _this = this;
				_this.isEndPage = false;
				_this.isBig = value;
				_this.courier = []
				_this.loadData();
			},
			onScroll(e) {
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

	.my-bottom {
		margin-bottom: 140upx;
	}
	.topBtn {
		height: 50px;
		width: 50px;
		border-radius: 50px;
		line-height: 50px;
		background-color: #007AFF;
		position: fixed;
		z-index: 999;
		bottom: 100px;
		right: 20px;
		color: white;
	}
</style>
