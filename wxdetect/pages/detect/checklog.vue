<template>
	<view class="container">

		
		<view class="back">
			<uni-table border stripe emptyText="未发现缺陷">
				<uni-tr>
					<uni-th width="100" align="center">序号</uni-th>
					<uni-th width="120" align="center">缺陷图片</uni-th>
					<uni-th width="120" align="center">缺陷总数</uni-th>
					<uni-th width="120" align="center">开裂</uni-th>
					<uni-th width="120" align="center">夹杂</uni-th>
					<uni-th width="120" align="center">点蚀</uni-th>
					<uni-th width="120" align="center">划痕</uni-th>
					<uni-th width="120" align="center">斑块</uni-th>
					<uni-th width="120" align="center">氧化</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in defections" :key="index">
					<uni-td align="center">{{ item.id }}</uni-td>
					
					<uni-td align="center"> <image class="logimg" :src="`data:image/jpeg;base64,${item.imgBase64}`"></image></uni-td>
					<uni-td align="center">{{ item.defectnum }}</uni-td>
					<uni-td align="center">{{ item.kl }}</uni-td>
					<uni-td align="center">{{ item.jz }}</uni-td>
					<uni-td align="center">{{ item.ds }}</uni-td>
					<uni-td align="center">{{ item.hh }}</uni-td>
					<uni-td align="center">{{ item.bk }}</uni-td>
					<uni-td align="center">{{ item.yh }}</uni-td>
				</uni-tr>
			</uni-table>
			<view class="uni-pagination-box">
				<uni-pagination type="line" showIcon="false" :page-size="pageSize" :current="pageCurrent" :total="total" @change="change" />
				
			</view>
		
		</view>
		
	</view>
	
</template>

<script>
	import listCell from '@/components/list-cell/list-cell'
	import {mapState} from 'vuex'
import { detect } from '../../api/detect'
	// import {detect} from '../../api/detect.js'
	
	export default {

		data() {
			return {
				src: "",
				defections:[],
				
				
				// 每页数据量
				pageSize: 5,
				// 当前页
				pageCurrent: 1,
				// 数据总量
				total: 0,
				
			}
		},
		onLoad() {
			this.checklog();
			
		},
		computed: {
			// paginatedItems() {
			// 	const startIndex = (this.pageCurrent - 1) * this.pageSize;
			// 	const endIndex = startIndex + this.pageSize;
			// 	return this.defections.slice(startIndex, endIndex);
			// }

		},
		
		methods: {
		
			checklog(){
				const that=this;
				uni.request({
				  url: 'http://192.168.15.222:8081/check/info', // 请求的URL
				  method: 'GET', // 请求方法，可选值包括：GET/POST/PUT/DELETE等
				  data: {
						page:that.pageCurrent,
						pageSize:that.pageSize,
					}, // 请求参数，可根据接口文档自行添加
				  
				  success: (res) => { // 请求成功回调函数
				    console.log(res.data); // 输出返回的数据
					if(res.data.code==200) 
					{
						that.defections=res.data.data.checkList;
						that.total=res.data.data.totalPages*this.pageSize;
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
				
			},
			
			
			
			change(e) {
				console.log(e.current);
				this.pageCurrent = e.current;
				this.checklog();
			},

		}
	}
</script>

<style lang="scss" scoped>
	/* #ifdef H5 */
	page {
		min-height: 100%;
		background-color: $bg-color;
	}
	/* #endif */
	.order-box {
		padding: 20rpx;
		/* #ifdef H5 */
		margin-bottom: 100rpx;
		/* #endif */
	}
	.d-flex{
		margin-top: 100rpx;
	}
	.container{
		display: flex;
		flex-direction: column;
		
		align-items: center;
	}
	.resimg{
		width: 675rpx;
	}
	.back{
		margin-top: 100rpx;
		display: flex;
		flex-direction: column;
		width: 675rpx;
		border-radius: 5%;
		justify-content: center;
		align-items: center;
		// background-color: #ffffff;
	}
	
	.drinks-img {
		width: 260rpx;
		height: 260rpx;
	}
	
	.tips {
		margin: 60rpx 0 80rpx;
		line-height: 48rpx;
	}
	
	.drink-btn {
		width: 320rpx;
		border-radius: 50rem !important;
		margin-bottom: 40rpx;
		font-size: $font-size-base;
		line-height: 3.0;
	}
	.logimg{
		width: 120rpx;
		height: 120rpx;
	}
	
	@mixin arch {
		content: "";
		position: absolute;
		background-color: $bg-color;
		width: 30rpx;
		height: 30rpx;
		bottom: -15rpx;
		z-index: 10;
		border-radius: 100%;
	}
	
	.section {
		position: relative;
		
		&::before {
			@include arch;
			left: -15rpx;
		}
		
		&::after {
			@include arch;
			right: -15rpx;
		}
	}
	
	.pay-cell {
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: $font-size-base;
		color: $text-color-base;
		margin-bottom: 40rpx;

		&:nth-last-child(1) {
			margin-bottom: 0;
		}
	}
	
	.sort-num {
		font-size: 64rpx;
		font-weight: bold;
		color: $text-color-base;
		line-height: 2;
	}
	
	.steps__img-column {
		display: flex;
		margin: 30rpx 0;
		
		.steps__img-column-item {
			flex: 1;
			display: flex;
			align-items: center;
			justify-content: center;
			
			image {
				width: 80rpx;
				height: 80rpx;
			}
		}
	}
	
	.steps__text-column {
		display: flex;
		margin-bottom: 40rpx;
		
		.steps__text-column-item {
			flex: 1;
			display: inline-flex;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: $font-size-base;
			color: $text-color-assist;
			
			&.active {
				color: $text-color-base;
				font-weight: bold;
				
				.steps__column-item-line {
					background-color: $text-color-base;
				}
			}
			
			.steps__column-item-line{
				flex: 1;
				height: 2rpx;
				background-color: #919293;
				transform: scaleY(0.5);
			}
			
			.steps__text-column-item-text {
				margin: 0 8px;
			}
		}
	}
</style>
