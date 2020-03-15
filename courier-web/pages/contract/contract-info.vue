<template>
	<view class="page">
		<cmd-nav-bar back :title="toUserInfo.userName"></cmd-nav-bar>
		<cmd-page-body type="top">
			<view class="cu-chat my-bottom" v-for="item in msgList">
				<view class="cu-item self" v-if="item.send_user_id == userInfo.id">
					<view class="main">
						<view class="content bg-green shadow">
							<text>{{item.content}}</text>
						</view>
					</view>
					<view class="cu-avatar radius">
						<image class="cu-avatar" :src="userInfo.avatar == '' ? 'https://avatar.bbs.miui.com/images/noavatar_small.gif' : imgUrl + userInfo.avatar"
						 mode=""></image>
					</view>
					<view class="date">{{item.create_time | dateFilter}}</view>
				</view>
				<view class="cu-item" v-if="item.send_user_id == toUserId">
					<view class="cu-avatar radius">
						<image class="cu-avatar" :src="toUserInfo.avatar == '' ? 'https://avatar.bbs.miui.com/images/noavatar_small.gif' : imgUrl + toUserInfo.avatar"
						 mode=""></image>
					</view>
					<view class="main">
						<view class="content shadow">
							<text>{{item.content}}</text>
						</view>
					</view>
					<view class="date ">{{item.create_time | dateFilter}}</view>
				</view>
			</view>

			<view class="cu-bar foot input" :style="[{bottom:InputBottom+'px'}]">
				<input class="solid-bottom" v-model="message" :adjust-position="false" :focus="false" maxlength="300"
				 cursor-spacing="10" @focus="InputFocus" @blur="InputBlur"></input>
				<button class="cu-btn bg-green shadow" @click="sendMsg">发送</button>
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
	export default {
		components: {
			cmdNavBar,
			cmdPageBody,
			cmdTransition,
			cmdInput
		},
		data() {
			return {
				InputBottom: 0,
				userInfo: {},
				message: '',
				toUserId: '',
				toUserInfo: '',
				msgList: [],
				imgUrl: imgUrl,
				height: 0
			};
		},
		onLoad(option) {
			let _this = this;
			_this.getUserInfo();
			_this.toUserId = option.toUserId;
			let url2 = "contract/findContractInfo";
			let params = {
				"pageNum": 1,
				"size": 10000,
				"toUserId": option.contractId,
				"contractId": option.contractId
			}
			getRquest(url2, params).then(res => {
				console.info(res)
				_this.msgList = res.data.rows;
				console.info(_this.msgList)
			}).catch(err => {
				console.info(err);
			});
			console.info(_this.toUserId)
			if (_this.toUserId != undefined) {
				let url = "user/findById/" + _this.toUserId;
				getRquest(url, "").then(res => {
					_this.toUserInfo = res.data;
				}).catch(err => {
					console.info(err);
				});
			}
			_this.connectSocketInit();
			setTimeout(() => {
				this.scrollTo();
			}, 500)
		},
		methods: {
			InputFocus(e) {
				this.InputBottom = e.detail.height
			},
			InputBlur(e) {
				this.InputBottom = 0
			},
			getUserInfo() {
				let _this = this;
				uni.getStorage({
					key: "userInfo",
					success: function(res) {
						_this.userInfo = res.data
					}
				})
			},
			// 进入这个页面的时候创建websocket连接【整个页面随时使用】
			connectSocketInit() {
				let _this = this;
				// 创建一个this.socketTask对象【发送、接收、关闭socket都由这个对象操作】
				this.socketTask = uni.connectSocket({
					// 【非常重要】必须确保你的服务器是成功的,如果是手机测试千万别使用ws://127.0.0.1:9099【特别容易犯的错误】
					url: "ws://localhost:8080/websocket/" + _this.userInfo.id,
					success(data) {
						console.log("websocket连接成功");
					},
				});

				// 消息的发送和接收必须在正常连接打开中,才能发送或接收【否则会失败】
				this.socketTask.onOpen((res) => {
					console.log("WebSocket连接正常打开中...！");
					this.is_open_socket = true;
					// 注：只有连接正常打开中 ，才能正常收到消息
					this.socketTask.onMessage((res) => {
						console.info(res)
						console.log("收到服务器内容：" + res.data);
						let objItem = {
							send_user_id: _this.toUserId,
							to_user_id: _this.userInfo.id,
							content: res.data,
							create_time: moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
						}
						_this.msgList.push(objItem);
						this.scrollTo();
					});
				})
				// 这里仅是事件监听【如果socket关闭了会执行】
				this.socketTask.onClose(() => {
					console.log("已经被关闭了")
				})
			},
			sendMsg() {
				let _this = this;
				if (_this.message == "") {
					return;
				}
				let url = "push/" + _this.toUserId;
				let params = {
					"message": _this.message
				}
				return getRquest(url, params).then(res => {
					let objItem = {
						send_user_id: _this.userInfo.id,
						to_user_id: _this.toUserId,
						content: _this.message,
						create_time: moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
					}
					_this.msgList.push(objItem);
					console.info(_this.msgList)
					this.scrollTo();
					_this.message = "";
					console.info(res);
				}).catch(err => {
					console.info(err);
				})
			},
			scrollTo() {
				let _this = this;
				uni.getSystemInfo({
					success: function(res) {
						_this.height = res.windowHeight
					}
				});
				uni.pageScrollTo({
					scrollTop: 99999,
					duration: 300
				});
			}
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
		height: 100%;
	}

	.my-bottom {
		margin-bottom: 40upx;
	}

	.margin-top {
		margin-top: 100rpx;
	}
</style>
