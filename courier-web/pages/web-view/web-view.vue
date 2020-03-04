<template>
	<view class="content">
		<web-view :src="path">//这个是自定义页面（htmlA）
		</web-view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				path: ''
			}
		},
		onLoad: function(e) {
			this.path = e.path;
		},
		methods: {

		},
	}
</script>