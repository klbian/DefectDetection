<template>
  <view class="container">
    <view class="user-info-box">
		<image class="topback" src="../../static/background.jpg"></image>
		<view class="card-one"></view>
    </view>

    
	<view class="menu-box1">
		<view class="avatar-box">
			<image v-if="isLogin" class="avatar" src="../../static/logo.png"></image>
			<image v-else class="avatar" src='../../static/nologin.png'></image>
			<view v-if="isLogin" @tap="userinfo" class="avatar-text">
				<view class="name">{{ user.name }}</view>
				<view class="desc">{{ user.email}}</view>
			</view>
			<view v-else class="avatar-text">
				<view class="name" @tap="login">请登录</view>
				<view class="desc">天道有常，或因势而迟，然终不误。</view>
			</view>
		</view>  
	</view>
	

   

    <div class="user-center-box">
      <view class="user-center-item" @tap="towarning">
        <view class="user-center-star-icon">
         <image src="../../static/warning.png"></image>
        </view>
        <view class="user-center-star-title" >
          异常报警
        </view>
      </view>
      <view class="user-center-item" @tap="tohistory">
        <view class="user-center-star-icon" >
          <image src="../../static/历史记录.png"></image>
        </view>
        <view class="user-center-star-title">
          检测记录
        </view>
      </view>
      <view class="user-center-item" @tap="todata">
        <view class="user-center-star-icon">
          <image src="../../static/数据统计.png"></image>
        </view>
        <view class="user-center-star-title">
          数据统计
        </view>
      </view>
    </div>


    
    <!-- 我的积分/我的任务/我的收藏 -->
    <view class="menu-box">
      <view class="menu-item">
        <view class="menu-item-left">
          <image src="../../static/logo.png"></image>
          <view class="menu-item-left-text">
            个人信息
          </view>
        </view>
        <view class="menu-item-right">
          <view class="arrow-right"></view>
        </view>
      </view>
      <view class="menu-item">
        <view class="menu-item-left">
          <image src="../../static/logo.png"></image>
          <view class="menu-item-left-text">
            其他信息
          </view>
        </view>
        <view class="menu-item-right">
          <view class="arrow-right"></view>
        </view>
      </view>
      <view class="menu-item">
        <view class="menu-item-left">
          <image src="../../static/logo.png"></image>
          <view class="menu-item-left-text">
            设置
          </view>
        </view>
        <view class="menu-item-right">
          <view class="arrow-right"></view>
        </view>
      </view>
    </view>
    

	
	<view style="margin-top: 20rpx;">	
		<button v-if="isLogin" class="btn" @tap="logout">退出登录</button>
	</view>
	
	<view style="height: 20rpx;"></view>
	
  </view>
</template>

<script>
  export default {
    data() {
      return {
        title: 'Hello',
		isLogin:false,
		user:[]
      }
    },
    onShow() {
		const value = uni.getStorageSync('user_info');
		if (value) {
			console.log("获取用户信息", value);
			this.user=value;
			this.isLogin=true;
		}else {
			this.user='';
			this.isLogin=false;
		}
		
    },
    methods: {
		login(){
			uni.navigateTo({url: '/pages/login/login'})
		},
		logout(){
			uni.removeStorageSync('user_info');
			uni.removeStorageSync('isLogin');
			console.log('数据清除成功');
			uni.navigateTo({
				url:"pages/mine/mine"
			})
		},
		tohistory() {
			uni.navigateTo({
				url: '/pages/history/history'
			})
		},
		towarning() {
			uni.navigateTo({
				url:'/pages/warning/warning'
			})
		},
		todata() {
			uni.switchTab({
				url:'/pages/data/data'
			})
		},
    }
  }
</script>



<style >
	page{
		height: auto;
		min-height: 100%;
		background-color:  #104DA4;
	}
	
	
  /* 自定义头部 */
  .head {
    height: 140rpx;
  }

  /* 用户信息部分 */
  .user-info-box {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: flex-end;
    /* position: fixed; */
    top: 0;
    left: 0;
    width: 100%;
    height: 500rpx;
    /* background-image: url( "../../static/background.jpg");
	background-size: 100%; */
  }
  

.menu-box1 {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
	align-items: center;
    background-color: #fff;
    border-radius: 16rpx;
    margin: 0rpx 40rpx 0 40rpx;
	height: 200rpx;
  }

 .avatar-box {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    align-items: center;
    padding-left: 40rpx;
  }

  .avatar-box .avatar {
    width: 116rpx;
    height: 116rpx;
    border-radius: 116rpx;
  }

  .avatar-box .avatar-text {
    padding-left: 20rpx;
  }

  .avatar-text .name {
    font-size: 20px;
    font-weight: 500;
    color: #000;
  }

  .avatar-text .desc {
    color: #000;
    padding-top: 10rpx;
    font-size: 12px;
  }

.topback{
	width: 100%;
	height: 100%;
}

  .card-one {
    height: 80rpx;
    width: 100%;
    background: #104DA4;
    border-radius: 50rpx 50rpx 0rpx 0rpx;
	margin-top: -80rpx;
    /* position: relative;
    bottom: 0rpx; */
	z-index: 2;
  }


  .size-seat {
    margin-top: 125rpx;
  }

  /* 中间部分 */
  .user-center-box {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    padding: 0 30rpx;
  }

  .user-center-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    background-color: #ffba00;
    width: 210rpx;
    margin: 30rpx 10rpx 0 10rpx;
    padding: 40rpx 30rpx 40rpx 30rpx;
    
    border-radius: 16rpx;
  }

  .user-center-star-icon {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    width: 80rpx;
    height: 80rpx;
    border-radius: 80rpx;
    border-radius: 80rpx;
    
    margin-right: 10rpx;
  }

  .user-center-star-icon image {
    width: 60%;
    height: 60%;
  }

  .user-center-star-title {
    font-weight: 550;
    font-size: 12px;
    padding-top: 10rpx;
    color: #fff;
  }

  /* 菜单区域 */
  .menu-box {
    background-color: #fff;
    border-radius: 16rpx;
    margin: 30rpx 40rpx 0 40rpx;
  }

  .menu-item {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1rpx solid #F6F6F6;
    padding: 30rpx;
  }

  .menu-item-left {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
  }

  .menu-item-left image {
    width: 38rpx;
    height: 38rpx;
  }

  .menu-item-left-text {
    padding-left: 20rpx;
    font-size: 14px;
  }

  .arrow-right {
    width: 15rpx;
    height: 15rpx;
    border-top: 3rpx solid #a9acb3;
    border-right: 3rpx solid #a9acb3;
    transform: rotate(45deg);
  }
  .btn {
	border-radius: 16rpx;
	margin: 0rpx 40rpx 0 40rpx;
	height: 100rpx;
  	font-weight: bold;	
  	line-height: 100rpx;
  	background-color:  #FC72A3;
	color: #fff;
  }
</style>
