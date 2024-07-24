<template>
	<view class="container">
		<view class="intro">
			<image src="/static/login.png"></image>
			<view class="tips">智检控-精密元件智能监测系统</view>
		</view>
		<view class="sun-login-box">
			<view class="sun-label">
				<image style="width: 40rpx;height:40rpx;" src="@/static/账号.png"/>
				<text class="label-text">账号</text>
			</view>
			<view class="sun-input-box">
				<input v-model="mobile" type="text" placeholder="请输入账号" />
				<image @click="mobile=''" class="close-icon" src="@/static/close_icon.png"/>
			</view>
		</view>
		<view class="sun-login-box">
			<view class="sun-label">
				<image style="width: 40rpx;height:40rpx;" src="@/static/密码.png"/>
				<text class="label-text">密码</text>
			</view>
			<view class="sun-input-box">
				<input v-model="password" type="password" placeholder="请输入密码" />
				<image @click="password=''" class="close-icon" src="@/static/close_icon.png"/>
			</view>
		</view>
		
		
		
		<view class="bottom">

				<button  size="default" class="login-btn" @tap="login">
					登录
				</button>


			
		</view>
	</view>
</template>

<script>

	import {mapMutations} from 'vuex'
	
	export default {
		data() {
			return {
				mobile:"",
				password:""
			}
		},
		methods: {
			login() {
				if(!this.mobile) return uni.showToast({title: '请输入账号',duration: 1500})
				if(!this.password) return uni.showToast({title: '请输入密码',duration: 1500})
				
				const that = this;
				uni.request({
				  url: 'http://192.168.15.222:8081/login/in', // 请求的URL
				  method: 'POST', // 请求方法，可选值包括：GET/POST/PUT/DELETE等
				  data: {
						account:that.mobile,
						pwd:that.password,
					  }, // 请求参数，可根据接口文档自行添加
				  
				  success: (res) => { // 请求成功回调函数
				    console.log(res.data); // 输出返回的数据
					if(res.data.code==200) 
					{
						uni.showToast({title:'登录成功',duration: 1500});
						uni.setStorageSync('user_info', res.data.data);
						console.log("已存储用户数据", res.data.data);
						if(res.data.data.warningsLevel==0) uni.setStorageSync('isLogin', 1);
						else uni.setStorageSync('isLogin', 2);
						
						setTimeout(() => {
							uni.navigateBack()
						}, 1000)	
					}
					else{
						uni.showToast({title:res.data.message,duration: 1500});
					}
				  },
				  fail: (err) => { // 请求失败回调函数
				    console.log(err);
					uni.showToast({title:'网络请求失败',duration: 1500});
				  }
				});	
				
				
				
				
				
				
				
			}
		}
	}
</script>

<style>
	page{
		background-color: #104da4;
	}
</style>
<style lang="scss" scoped>
	.intro {
		width: 100%;
		height: 400rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-evenly;
		font-size: $font-size-base;
		color: #fff;
		
		image {
			width: 165rpx;
			height: 165rpx;
		}
		
		.tips {
			line-height: 72rpx;
			text-align: center;
		}
	}
	
	.bottom {
		height: 40vh;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		padding: 0 40rpx;
		margin-top: 20rpx;
		.login-btn {
			width: 100%;
			border-radius: 50rem !important;
			display: flex;
			align-items: center;
			justify-content: center;
			padding: 10rpx 0;
			background-color: #ffba00;
			color: #fff;
			font-weight: bold;
		}

	}
	
	
	
	.close-icon {
		width: 36rpx;
		height: 34rpx;
	}

	.sun-login-box {
		padding: 20rpx 60rpx;
	}
	.sun-label {
		display: flex;
		align-items: center;
	}
	.label-text {
		margin-left: 16rpx;
		font-weight: 500;
		color: #fff;
		font-size: 30rpx;
	}
	.sun-input-box {
		
		display: flex;
		align-items: center;
		height: 50rpx;
		border-bottom: 1rpx solid #fff;
		padding: 0px 10rpx;
	}
	.sun-input-box input {
		flex: 1;
		color: #fff;
		;
	}
</style>
