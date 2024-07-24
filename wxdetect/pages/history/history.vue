<template>  
  <div>  
  
<view class="table">  
  <view class="tr head">  
    <view class="th">ID</view>  
    <view class="th">图片名称</view>  
    <view class="th">检测时间</view>  
    <view class="th">工单号</view>  
    <view class="th">缺陷数</view>  
    <view class="th">操作</view>  
  </view>  
  <view v-for="record in historyRecords" :key="record.id" class="tr">  
    <view class="cell">{{ record.id }}</view>  
	<view class="cell"><img :src="`data:image/jpeg;base64,${record.imgBase64}`" alt="Image" style="width: 50px; height: 50px;"> </view>
    <view class="cell">{{ record.time }}</view>  
    <view class="cell">{{ record.workOrderId }}</view>  
    <view class="cell">{{ record.totals }}</view>  
    <view class="cell">  
      <button class="b_details" @click="showDetails(record.id)">查看详情</button>  
      <button class="b_delete" @click="deleteRecord(record.id)">删除</button>  
    </view>  
  </view>  
</view>
  
    <!-- 弹窗用于查看详细信息 -->  
    <div v-if="showModal" class="modal">  
      <div class="modal-content">  
        <span @click="showModal = false" class="close">&times;</span>  
        <h2>{{ detailRecord.name }}</h2>  
        <p>检测时间: {{ detailRecord.time }}</p>  
        <p>工单号: {{ detailRecord.workOrderId }}</p>  
        <p>缺陷数: {{ detailRecord.totals }}</p>  
		<img :src="`data:image/jpeg;base64,${detailRecord.imgBase64}`" alt="Detail Image" style="max-width: 100%;"> 
        <ul>  
          <li v-for="(defection, index) in detailRecord.defections" :key="index">  
            缺陷名称: {{ defection.category }}, 精确度: {{ defection.score }},
			横向长度:{{defection.l}},纵向长度:{{defection.h}},
			横坐标:{{defection.x}},纵坐标:{{defection.y}}
          </li>  
        </ul>  
      </div>  
    </div>  
  </div>  
</template>  
  
<script>  
export default {  
  data() {  
    return {  
	
      historyRecords: [],  
      showModal: false,  
      detailRecord: {}  
    };  
  },  
  onLoad() {
  	uni.request({
  	  url: 'http://192.168.15.222:8081/detectInfo/info/history', // 请求的URL
  	  method: 'GET', // 请求方法，可选值包括：GET/POST/PUT/DELETE等
  	  data: {
  			page:1,
  			pageSize:5,
  		  }, // 请求参数，可根据接口文档自行添加
  	  
  	  success: (res) => { // 请求成功回调函数
  	    console.log(res.data); // 输出返回的数据
  		if(res.data.code==200) 
  		{
			this.historyRecords=res.data.data;
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
  methods: {  
 
    showDetails(id) {  
		const that=this;
      uni.request({
        url: 'http://192.168.15.222:8081/detectInfo/info/details', // 请求的URL
        method: 'GET', // 请求方法，可选值包括：GET/POST/PUT/DELETE等
        data: {
      		id:id
      	  }, // 请求参数，可根据接口文档自行添加
        
        success: (res) => { // 请求成功回调函数
          console.log(res.data); // 输出返回的数据
      	if(res.data.code==200) 
      	{
      		const record = this.historyRecords.find(r => r.id === id);
      		console.log(record);
			if (record) {  
      		  // 假设已经从后端获取了详细的缺陷信息，并附加到记录中  
      		  record.defections = res.data.data.defections;
      		  that.detailRecord = record;  
      		  that.showModal = true;  
			  console.log(that.detailRecord);
      		}  
			
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
    deleteRecord(id) {  
      // 实现删除逻辑，可以通过Ajax请求后端进行删除操作  
      // 这里仅作前端模拟删除  
      this.historyRecords = this.historyRecords.filter(r => r.id !== id);  
      alert('删除成功！');  
    }  
  },  
  // mounted() {  
  //   this.fetchHistory();  
  // }  
};  
</script>  
  
<style>  
.modal {  
  display: block;  
  position: fixed;  
  z-index: 1;  
  left: 0;  
  top: 0;  
  width: 100%;  
  height: 100%;  
  overflow: auto;  
  background-color: rgba(0, 0, 0, 0.4);  
}  
  
.modal-content {  
  background-color: #fefefe;  
  margin: 15% auto;  
  padding: 20px;  
  border: 1px solid #888;  
  width: 80%;  
}  
  
.close {  
  color: #aaa;  
  float: right;  
  font-size: 28px;  
  font-weight: bold;  
  cursor: pointer;  
}  
/* 表格整体样式 */  
.table {  
  display: flex;  
  flex-direction: column;  
  width: 100%;  
}  
  
/* 表格行样式 */  
.tr {  
  display: flex;  
}  
  
/* 表头样式 */  
.head {  
  background-color: #104DA4;  
  color: white;  
}  
  
/* 具体的单元格宽度设置 */  
.th:nth-child(1), .cell:nth-child(1) {  
  width: 8%; /* 第一个单元格宽度为10% */  
    line-height: 140rpx;
}  
  
.th:nth-child(2), .cell:nth-child(2) {  
  width: 20%; /* 第二个单元格宽度为18% */
	  line-height: 140rpx;
}  
  
.th:nth-child(3), .cell:nth-child(3) {  
  width: 22%; /* 第三个单元格宽度为18% */  
}  
.th:nth-child(3){
	line-height: 140rpx;
}
  
.th:nth-child(4), .cell:nth-child(4) {  
	  line-height: 140rpx;
  width: 16%; /* 第四个单元格宽度为18% */  
}  
  
.th:nth-child(5), .cell:nth-child(5) {  
  width: 8%; /* 第五个单元格宽度为18% */  
}  
.cell:nth-child(5){
	line-height: 140rpx;
}
.th:nth-child(6), .cell:nth-child(6) {  
	line-height: 140rpx;
  width: 26%; /* 第六个单元格宽度为18% */  
}  
  
/* 单元格内边距和边框样式 */  
.th, .cell {  
  padding: 10rpx; /* 设置单元格内边距 */  
  border: 1rpx solid #104DA4; /* 设置单元格边框颜色 */  
  box-sizing: border-box; /* 确保边框和内边距包含在宽度内 */  
  text-align: center; /* 文字居中显示 */ 
  height:150rpx ;
}
.b_delete,.b_details{
  height: 50rpx;  
  line-height: 50rpx; /* 设置行高与高度相同，以实现文字垂直居中 */  
  text-align: center; /* 确保文字水平居中，虽然这通常是button的默认设置 */  
  padding: 0 10rpx; /* 可以根据需要调整水平内边距 */  
  border: none; /* 移除边框，如果需要的话 */  
  background-color: #104DA4; /* 设置背景颜色 */  
  color: white; /* 设置文字颜色 */
}
.b_details{
	margin-bottom: 15rpx;
}

</style>