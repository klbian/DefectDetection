<template>
	<view class="container">
		<view class="d-flex w-100  flex-column just-content-center align-items-center">
			<view>
			    <image class="resimg" :src="src"></image>	
			</view>
		</view>
		
		<view class="back">
			<uni-table border stripe emptyText="未发现缺陷">
				<uni-tr>
					<uni-th width="100" align="center">序号</uni-th>
					<uni-th width="120" align="center">缺陷类别</uni-th>
					<uni-th width="120" align="center">缺陷长度</uni-th>
					<uni-th width="120" align="center">缺陷宽度</uni-th>
					<uni-th width="120" align="center">缺陷x坐标</uni-th>
					<uni-th width="120" align="center">缺陷y坐标</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in paginatedItems" :key="index">
					<uni-td align="center">{{ item.id }}</uni-td>
					<uni-td align="center">{{ item.category }}</uni-td>
					<uni-td align="center"> {{ item.l }}</uni-td>
					<uni-td align="center">{{ item.h }}</uni-td>
					<uni-td align="center">{{ item.x }}</uni-td>
					<uni-td align="center">{{ item.y }}</uni-td>
					
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
				pageSize: 2,
				// 当前页
				pageCurrent: 1,
				// 数据总量
				total: 0,
				
			}
		},
		onLoad(option) {
			const item = JSON.parse(decodeURIComponent(option.item));
			console.log(item);
			this.src="data:image/jpg;base64,"+item.imgBase64;
			
			this.defections=item.defections;
			this.defections.forEach((i,index)=>{
				this.$set(i,"id",index+1);
			});
			this.total = this.defections.length;
		},
		computed: {
			paginatedItems() {
				const startIndex = (this.pageCurrent - 1) * this.pageSize;
				const endIndex = startIndex + this.pageSize;
				return this.defections.slice(startIndex, endIndex);
			}
		},
		
		methods: {
			
			change(e) {
				console.log(e.current);
				this.pageCurrent = e.current;
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
