<template>
	<view class="container">
		<view class="banner">
			<image src="../../static/images/index/banner.png" mode="" class="bg"></image>
		</view>
		<view class="content">
			<view class="entrance">
				<view class="item" @tap="takein">
					<image src="/static/images/index/history_page.png" class="icon"></image>
					<view class="title">历史检测</view>
				</view>
				<view class="item" @tap="takeout">
					<image src="/static/images/index/excep_page.png" class="icon"></image>
					<view class="title">异常管理</view>
				</view>
			</view>
			<view class="info">
				<view class="qrcode_section" @tap="detect">
					<image src="/static/images/index/photo.png"></image>
				</view>
				<view class="integral_section" @tap="detect">
					<view class="top">
						<text class="value">一键检测</text>
					</view>
				</view>
			</view>
			<view class="member-news">
				<view class="header">
					<view class="title" >流水线作业信息</view>
					<view class="iconfont iconRightbutton" @tap="takein"></view>
				</view>
			</view>
			<view class="waterflood">
				<view class="left">
					<view class="top">
						<view class="title">运行时长</view>
						<view class="value">
							<view class="v1">0</view>
							<view class="v2">小时</view>
							<view class="v1">43</view>
							<view class="v2">分钟</view>
						</view>
					</view>
					<view class="bottom">
						<view class="title">缺陷总数</view>
						<view class="value">12394</view>
					</view>
				</view>
				<view class="right">
					<view class="top">
						<view class="title">缺陷率</view>
						<view class="value">0.35%</view>
					</view>
					<view class="bottom">
						<view class="title">最高发缺陷</view>
						<view class="value">划痕</view>
					</view>
				</view>
			</view>
			
		</view>
	</view>
</template>

<script>

	export default {
		data() {
			return {
			}
		},
		computed: {

		},
		methods: {
			takein() {
				uni.navigateTo({
					url: '/pages/history/history'
				})
			},
			takeout() {
				uni.navigateTo({
					url:'/pages/warning/warning'
				})
			},
			detect() {
				uni.switchTab({
					url: '/pages/detect/detect'
				})
			},
			

		}
	}
</script>

<style lang="scss" scoped>

page {
	height: auto;
	min-height: 100%;
}


.banner {
	position: relative;
	width: 100%;
	height: 500rpx;
	
	.bg {
		width: 100%;
		height: 100%;
	}

}

.content {
	padding: 0 30rpx;
}

.entrance {
	position: relative;
	margin-top: -80rpx;
	margin-bottom: 30rpx;
	border-radius: 10rpx;
	background-color: #ffffff;
	box-shadow: $box-shadow;
	padding: 30rpx 0;
	display: flex;
	align-items: center;
	justify-content: center;
	
	.item {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		position: relative;
		
		&:nth-child(1):after {
			content: '';
			position: absolute;
			width: 1rpx;
			background-color: #ddd;
			right: 0;
			height: 100%;
			transform: scaleX(0.5) scaleY(0.8);
		}
		
		.icon {
			width: 84rpx;
			height: 84rpx;
			margin: 20rpx;
		}
		
		.title {
			font-size: 30rpx;
			color: $text-color-base;
			font-weight: 600;
		}
	}
}

.info {
	position: relative;
	margin-bottom: 30rpx;
	border-radius: 10rpx;
	background-color: #ffba00;
	box-shadow: $box-shadow;
	padding: 30rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	
	
	
	.integral_section {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: left;
		justify-content: center;
		
		.top {
			display: flex;
			align-items: center;
			
			.value {
				font-size: 44rpx;
				font-weight: bold;
			}
		}
		
	}
	
	.qrcode_section {
		color: $color-primary;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		font-size: $font-size-sm;
		width:40%;
		
		image {
			width: 80rpx;
			height: 80rpx;
		}
	}
}
.waterflood{
	width:100%;
	margin-bottom: 20rpx;
	margin-top: -15px;
	border-radius: 10rpx;
	background-color: #ffffff;
	box-shadow: $box-shadow;
	padding: 25rpx;
	display: flex;
	align-items: stretch;
	
	.left,.right{
		flex:0 0 48%;
		display: flex;
		flex-direction: column;
		margin-right: 20rpx;

		.top, .bottom {  
		  display: flex;  
		  flex-direction: column;  
		  justify-content: space-between; /* 垂直方向上顶部和底部对齐 */  
		  padding: 10rpx; /* 内部内边距 */  
		  border-radius: 10rpx; /* 圆角 */  
		  background-color: #f5f5f5; /* 背景色 */  
		  align-items: center; /* 垂直居中 */  
		  height: 185rpx;
		  padding-bottom:33rpx ;
		  
		  .title {  
			color: #FFFFFF;
		    margin-bottom: 5rpx; /* 标题和值之间的间距 */  
		    align-self: flex-start; /* 让标题位于左上角 */  
		  } 
		  
		  .value {  
		    display: flex;  
		    align-items: center; /* 垂直居中 */
			justify-content: center;
		    flex-wrap: wrap; /* 允许内容换行 */  
			font-size: 55rpx;
			.v2{
				font-size: 36rpx;
			}
		  } 
		} 
		
	}
	.left{
		.top{
			background-image:url("../../static/images/index/left_top.png");
			background-size: 100% 100%;
			background-repeat: no-repeat;
			margin-bottom: 20rpx;
			.value{
				color: #FC72A3;
			}
		}
		.bottom{
			background-image:url("../../static/images/index/left_bottom.png");
			background-size: 100% 100%;
			background-repeat: no-repeat;
			.value{
				color: #ffba00;
			}
		}
	}
	.right{
		.top{
			background-image:url("../../static/images/index/right_top.png");
			background-size: 100% 100%;
			background-repeat: no-repeat;
			margin-bottom: 20rpx;
			.value{
				color:#104DA4;
			}
		}
		.bottom{
			background-image:url("../../static/images/index/right_bottom.png");
			background-size: 100% 100%;
			background-repeat: no-repeat;
			.value{
				color: #20CFD1;
			}
		}
	}

}


.member-news {
	width: 100%;
	margin-bottom: 30rpx;
	.header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 20rpx 0;
		
		.title {
			font-size: $font-size-lg;
			font-weight: bold;
		}
		
		.iconfont {
			font-size: 52rpx;
			color: $text-color-assist;
		}
	}
	
	.list {
		width: 100%;
		display: flex;
		flex-direction: column;
		
		.item {
			width: 100%;
			height: 240rpx;
			position: relative;
			
			image {
				width: 100%;
				height: 100%;
				border-radius: 8rpx;
			}
			
			.title {
				position: relative;
				font-size: 32rpx;
				font-weight: 500;
				width: 100%;
				top: -70rpx;
				left: 16rpx;
				color: #ffffff;
			}
		}
	}
}
</style>
