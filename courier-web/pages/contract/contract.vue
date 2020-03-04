<template name="contract">
	<you-scroll class="page" ref="scroll" @onPullDown="onPullDown">
		<cmd-nav-bar title="聊天列表"></cmd-nav-bar>
		<cmd-page-body type="top">
			<view class="cu-list menu-avatar">
				<view class="cu-item" :class="modalName=='move-box-'+ index?'move-cur':''" v-for="(item,index) in contractorList"
				 :key="index" @tap="toContract(item.to_user_id == userInfo.id ? item.user_id : item.to_user_id)" :data-target="'move-box-' + index">
					<view class="cu-avatar round lg">
						<image class="cu-avatar" :src="item.avatar == '' ? 'https://avatar.bbs.miui.com/images/noavatar_small.gif' : imgUrl + item.avatar"
						 mode=""></image>
					</view>
					<view class="content">
						<view class="text-grey">{{item.user_name}}</view>
						<view class="text-gray text-sm">
							{{item.contents[item.contents.length - 1].content}}</view>
					</view>
					<view class="action">
						<view class="text-grey text-xs">{{item.contents[item.contents.length - 1].create_time | dateFilter}}</view>
						<view class="cu-tag round bg-grey sm">{{item.contents.length}}</view>
					</view>
					<view class="move">
						<view class="bg-grey">置顶</view>
						<view class="bg-red">删除</view>
					</view>
				</view>
			</view>
		</cmd-page-body>
	</you-scroll>
</template>

<script>
	import cmdNavBar from "@/components/cmd-nav-bar/cmd-nav-bar.vue"
	import cmdPageBody from "@/components/cmd-page-body/cmd-page-body.vue"
	import cmdTransition from "@/components/cmd-transition/cmd-transition.vue"
	import cmdInput from "@/components/cmd-input/cmd-input.vue"
	import moment from 'moment'
	import youScroll from '@/components/you-scroll/you-scroll'
	import {
		getRquest
	}
	from "@/api.js"
	import {
		imgUrl
	} from '@/config'
	export default {
		components: {
			cmdNavBar,
			cmdPageBody,
			cmdTransition,
			cmdInput,
			youScroll
		},
		data() {
			return {
				modalName: null,
				gridCol: 3,
				gridBorder: false,
				menuBorder: false,
				menuArrow: false,
				menuCard: false,
				skin: false,
				listTouchStart: 0,
				listTouchDirection: null,
				contractorList: [],
				imgUrl: imgUrl,
				userInfo: {}
			};
		},
		created() {
			this.loadData()
		},
		methods: {
			showModal(e) {
				this.modalName = e.currentTarget.dataset.target
			},
			hideModal(e) {
				this.modalName = null
			},
			Gridchange(e) {
				this.gridCol = e.detail.value
			},
			Gridswitch(e) {
				this.gridBorder = e.detail.value
			},
			MenuBorder(e) {
				this.menuBorder = e.detail.value
			},
			MenuArrow(e) {
				this.menuArrow = e.detail.value
			},
			MenuCard(e) {
				this.menuCard = e.detail.value
			},
			SwitchSex(e) {
				this.skin = e.detail.value
			},

			// ListTouch触摸开始
			ListTouchStart(e) {
				this.listTouchStart = e.touches[0].pageX
			},

			// ListTouch计算方向
			ListTouchMove(e) {
				this.listTouchDirection = e.touches[0].pageX - this.listTouchStart > 0 ? 'right' : 'left'
			},

			// ListTouch计算滚动
			ListTouchEnd(e) {
				if (this.listTouchDirection == 'left') {
					this.modalName = e.currentTarget.dataset.target
				} else {
					this.modalName = null
				}
				this.listTouchDirection = null
			},
			toContract(toUserId) {
				uni.navigateTo({
					url: '/pages/contract/contract-info?toUserId=' + toUserId
				})
			},
			loadData() {
				let _this = this;
				let url2 = "contract/find";
				let params = {
					"pageNum": 1,
					"size": '10000'
				}
				uni.getStorage({
					key: 'userInfo',
					success(res) {
						_this.userInfo = res.data;
					}
				})
				return getRquest(url2, params).then(res => {
					console.info(res);
					_this.contractorList = res.data.rows;
				}).catch(err => {
					console.info(err);
				});
			},
			onPullDown(done) {
				let _this = this;
				setTimeout(() => {
					_this.loadData();
					done(); // 完成刷新
				}, 1000)
			},
		},
		filters: {
			dateFilter(value) {
				return moment(value).format("YYYY-MM-DD HH:mm:ss")
			}
		}
	}
</script>

<style>
	.page {
		height: 100vh;
	}
</style>
