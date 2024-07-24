<template>
	<view class="container">
		<view class="d-flex w-100  flex-column just-content-center align-items-center">
			<view>
			        <image class="topimg" src="../../static/R.jpg"></image>
			</view>
		</view>
		<view class="back">
			<view class="detect">
				<text class="detect-text">开始检测</text>
			</view>
			<view class="back-top">
				<button type="primary" class="drink-btn" @click="takePhoto">点击拍照</button>
				<button type="primary" class="drink-btn" @click="selectPhoto">相册选择</button>
			</view>
			<view>
				<button type="primary" class="drink-btn2" @click="tocheck">查看抽检数据</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				src: "",
				data:[],
			}
		},
		methods: {
			takePhoto() {
				const value = uni.getStorageSync('user_info');
				if (!value) {
					uni.navigateTo({
						url:"/pages/login/login"
					})
				}
				else{
					uni.chooseImage({
						count:1,
						sourceType:['camera'],
						success: (res) => {
							this.src=res.tempFilePaths[0];
							this.detectimg();	
						}
					})
				}
			},
			selectPhoto(){
				const value = uni.getStorageSync('user_info');
				if (!value) {
					uni.navigateTo({
						url:"/pages/login/login"
					})
				}
				else{
					uni.chooseImage({
						count:1,
						sourceType:['album'],
						success: (res) => {
							this.src=res.tempFilePaths[0];
							uni.showLoading({
								title: '检测中'
							});
							this.detectimg();	
						}
					})
				}
				
				
				
			},
			todetail(item){
				uni.navigateTo({
					url: '/pages/detect/result?item='+encodeURIComponent(JSON.stringify(item))
				})	
			},
			detectimg() {
				uni.uploadFile({
					url: 'http://192.168.15.222:8081/metalDetect/detect', //仅为示例，非真实的接口地址
					filePath: this.src,
					name: 'img',
					success: (uploadFileRes) => {
						const imgdata=JSON.parse(uploadFileRes.data);
						if(imgdata.code==200){
							uni.showToast({
								title:'检测成功！'
							});
							this.data=imgdata.data;
							console.log(this.data);
							uni.hideLoading();
							this.todetail(this.data);
							// const base64="data:image/jpg;base64,"+data.data.imgBase64;
							// that.src=base64;
						}else{
							uni.showToast({
								title:uploadFileRes.data.msg
							})
						}
					},
					fail(res) {
						console.log(res.errMsg);
					}
				});
			},
			tocheck(){
				uni.navigateTo({
					url: '/pages/detect/checklog'
				})
			},
					
		}
	}
</script>

<style>
	page{
		background-color:  #104DA4;
	}
</style>

<style lang="scss" scoped>
	page {
		min-height: 100%;
		background-color: $bg-color;
	}
	
	.container{
		display: flex;
		flex-direction: column;	
		align-items: center;
	}
	.toptitle{
		display: flex;
		width: 750rpx;
		align-items: center;
	}
	.d-flex{
		margin-top: 100rpx;
	}
	.back{
		margin-top: 100rpx;
		display: flex;
		flex-direction: column;
		width: 675rpx;
		height: 450rpx;
		border-radius: 5%;
		justify-content: flex-end;
		align-items: center;
		background-color: #ffffff;
	}
	.back-top{
		display: flex;
		flex-direction: row;
	}
	.icon{
		height: 25rpx;
		width: 25rpx;
		margin-left: 20rpx;
	}
	.text{
		margin-left:10rpx;
		color: #ffffff;
	}

	
	.topimg{
		width: 675rpx;
		height: 379rpx;
		border-radius: 5%;
	}
	.detect{
		margin-bottom: 50rpx;
	}
	.detect-text{
		font-size: 60rpx;
		font-weight: bold;
		color: #104da4;
		
	}

	
	
	.drink-btn {
		font-weight: bold;	
		width: 300rpx;
		height: 100rpx;
		border-radius: 5%;
		margin-bottom: 30rpx;
		margin-left: 20rpx;
		margin-right: 20rpx;
		// margin-bottom: 40rpx;
		font-size: $font-size-base;
		line-height: 100rpx;
		background-color: #FFBA00;
	}
	.drink-btn2{
		
		font-weight: bold;
		width: 650rpx;
		height: 100rpx;
		line-height: 100rpx;
		margin-bottom: 50rpx;
		background-color: #FFBA00;
	}


</style>
