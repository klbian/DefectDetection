<template>
    <view class="box">
        <view class="title">
            缺陷占比图
        </view>
        <view class="charts-box">
            <qiun-data-charts type="pie" :chartData="pieData" />
        </view>
        <view class="title">
            缺陷占比图
        </view>
        <view class="charts-box">
            <qiun-data-charts type="rose" :chartData="pieData" />
        </view>
        <view class="title">
            缺陷分布图
        </view>
        <view class="charts-box">
            <qiun-data-charts type="column" :chartData="detecData" :ontouch="true" />
        </view>
        <view class="title">
            缺陷变化率图
        </view>
        <view class="charts-box">
            <qiun-data-charts type="line" :chartData="chartData" :ontouch="true" />
        </view>
		<view class="title">
		    各级别警告数量图
		</view>
		<view class="charts-box">
		    <qiun-data-charts type="column" :chartData="alarmData" :ontouch="true" />
		</view>
    
    </view>
</template>

<script>
    export default {
        data() {
            return {
				datas:[],//所有表格数据
                chartData:{
                  categories: ["40", "80", "120", "160", "200", "240", "280", "320"],
                  series: [{
                    name: "合格率",
                    data: [0.81, 0.819, 0.815, 0.824,0.833, 0.819, 0.84, 0.851]
                  }]
                },
                pieData: {
                    "series":[
                        {"name":"划痕Sc","data":57},
                        {"name":"夹杂In","data":30},
                        {"name":"点蚀Ps","data":20},
                        {"name":"开裂Cr","data":18},
                        {"name":"斑块Ra","data":8},
                		{"name":"氧化铁皮压入RS","data":8},
                    ],
                },
                alarmData:{
                	categories: ["1", "2", "3"],
                	series: [{
                	  name: "警告数",
                	  data: [2, 1, 0]
                	}]
                },
				detecData:{
					categories:["划痕","夹杂","点蚀","开裂","班块","氧化铁皮压入"],
					series:[{
						name:"数量",
						data:[57,30,20,18,8,8]
					}]
				},
            }
        },
        methods: {
        }
    }
</script>

<style>
	.box{
		padding:30rpx;
	}
	.title{
		padding-left:20rpx;
		font-weight: bold;
		font-size: 36rpx;
		margin-bottom: 20rpx;
	}
	.charts-box{
		margin-bottom: 30rpx;
	}
</style>

