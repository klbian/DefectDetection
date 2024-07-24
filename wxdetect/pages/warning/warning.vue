<template>  
  <view class="warning-management">  
<!--    <view class="h1">异常管理</view>  -->
  
    <!-- 警告通知设置 -->  
    <view class="warning-notification-settings">  
      <!-- 这里添加开关、紧急程度选择、联系方式选择等 -->  
    </view>  
  
    <!-- 搜索和筛选 -->  
  <view class="filters">  
  <view class="search">
    <input  
      type="text"  
      placeholder="搜索预警名称"  
      v-model="searchQuery"  
      class="search-input"  
    />  
    <button class="search-button" @click="search">搜索</button>  </view>
    <!-- 这里假设你有一个按钮或其他元素来触发picker的显示 -->  
    <!-- picker应该由某个逻辑控制其显示，这里为了简化，我们用一个布尔值控制 -->  
	
	<view>
		<view class="select-picker">
			<picker
			  v-if="showPickerVisible"  
			  mode="selector"  
			  :range="levelOptions"  
			  v-model="selectedLevelIndex"  
			  @change="bindPickerChange"  
			  style="width: 100%; height: 30px;"  
			  class="picker"  
			> 
			<view class="p-block">
				<view class="select-pick">{{selectedLevelText}}</view>
				<view class="iconfont iconRightbutton"></view>
			</view>
			</picker>  
		</view>
	</view>
  </view> 
  
    <!-- 预警信息表格 -->  
    <view class="table">  
      <view class="thead">  
        <view class="tr">  
          <view class="th">预警名称</view>  
          <view class="th">紧急程度</view>  
          <view class="th">发生时间</view>  
          <view class="th">详细预警信息</view>  
        </view>  
      </view>  
      <view class="tbody">  
        <!-- 使用v-for渲染列表 -->  
        <view v-for="item in filteredWarnings" :key="item.id" class="tr">  
          <view class="td">{{item.type}}</view>  
          <view class="td">{{item.level}}</view>  
          <view class="td">{{item.createTime}}</view>  
          <view class="td">{{item.content}}</view>  
        </view>  
      </view>  
    </view>
	  
	  <!-- 显示总数和24小时预警数 -->
	  <view class="summary">  
	    <view class="sum">预警总数: {{warningsSum}}</view>  
	    <view class="twentyfour">24小时内预警数: {{twentyFourHourWarningsSum}}</view>  
	  </view>  
  </view>  
</template>
  
<script>  
export default {  
  data() {  
    return {  
      warnings: [], // 存储所有预警信息  
      warningsSum: 0,  
      twentyFourHourWarningsSum: 0,  
      searchQuery: '', // 搜索查询字符串  
      selectedLevelIndex: 0, // 选择的预警级别索引  
	  selectedLevelText: '请选择紧急程度',
      levelOptions: ['不进行筛选','1', '2', '3'], // 预警级别选项  
      showPickerVisible: true // 控制picker是否显示  
    };  
  },  
  computed: {  
    // 过滤后的预警信息  
    filteredWarnings() {  
      return this.warnings.filter(warning => {  
        return (  
          !this.searchQuery || warning.type.includes(this.searchQuery)  
        ) && (!this.selectedLevel || warning.level === parseInt(this.selectedLevel, 10));  
      });  
    },  
  },  
  created() {  
    // 在组件创建时加载预警信息  
    this.loadWarnings();  
  },  
  methods: {  
    async loadWarnings() {  
      try {  
        // 发起GET请求到warning/load  
        const response = await fetch('/api/warning/load');  
        const data = await response.json();  
          
        if (data.code === 200) {  
          this.warnings = data.data.warningsList;  
          this.warningsSum = data.data.warningsSum;  
          this.twentyFourHourWarningsSum = data.data['24hourWarningsSum']; 
          // 设置其他相关属性  
        } else {  
          // 处理错误  
          console.error(data.message);  
        }  
      } catch (error) {  
        // 处理网络错误或其他异常  
        console.error(error);  
      }  
    },  
    // ... 其他方法，如设置警告通知等  
	search() {  
	  // 这里可以执行搜索相关的逻辑  
	  console.log('搜索:', this.searchQuery);  
	},  
  bindPickerChange(e) {    
    this.selectedLevelIndex = e.detail.value;  
    if (this.selectedLevelIndex === 0) {  
      this.selectedLevelText = '请选择紧急程度'; // 当选择“不进行筛选”时  
    } else {  
      this.selectedLevelText = this.levelOptions[this.selectedLevelIndex]; // 当选择其他紧急程度时  
    }  
  }, 
  },  
};  
</script>  
  
<style scoped>  
.h1{
	text-align: center;
	color:#104da4;
	font:bolder;
	font-size: 100rpx;
	margin: 20rpx 0;
}
/* 添加样式 */  
.summary {  
  display: flex;  
  justify-content: space-between; /* 让子元素之间有空隙并且均匀分布 */  
  align-items: center; /* 垂直居中 */  
  padding:0 30rpx;
}  
.filters {  
  display: flex;  
  justify-content: space-between;
  align-items: center;  
}  
  
.search {  
  display: flex;  
   flex-direction: row;
}  
  
.search-input {  
  border: 1px solid #FC72A3;  
  padding: 5px;  
  height: 50rpx;  
  text-align: center;  
  flex: 1; /* 让.search-input占据.search的剩余空间，但因为有.search-button，所以不会达到100% */  
}  
  
.search-button {  
  border: none;  
  background-color: #FC72A3;  
  color: white;  
  cursor: pointer;  
  height: 50rpx;  
  line-height: 50rpx;  
  /* 不需要flex-shrink: 0，因为默认就不会缩小 */  
  /* 你可能需要设置一个具体的宽度，或者使用margin/padding来确保空间 */  
}  
  
.p-block {    
  display: flex;    
  flex-direction: row;  
  justify-content: space-between; /* 使得子元素在主轴上均匀分布，首尾贴边 */  
  align-items: center; /* 使得子元素在交叉轴上居中 */  
  background-color: #ffba00;  
  padding: 0 10rpx;  
  width: 300rpx;  
  border-radius: 15rpx;
 /* 左上角 右上角 右下角 左下角 */  
    /* 或者使用缩写形式，只指定左上角和右下角 */  
}    
  
.select-pick {    
  /* 无需设置宽度，因为默认会占据剩余空间（除非设置了flex-grow） */  
  height: 50rpx;    
  line-height: 50rpx;    
  text-align: center;  
  color: #ffffff;  
  /* 如果需要确保select-pick不会占据全部剩余空间，可以设置flex-grow为一个小于1的值 */  
  flex-grow: 1; /* 可选，取决于你希望select-pick占据多少空间 */  
}    
  
.iconfont {    
  /* iconfont靠右显示，由于justify-content: space-between已经设置，所以它会自然靠右 */  
  font-size: 52rpx;    
  color: #ffffff; /* 注意：确保这里使用的是有效的CSS颜色值 */  
  /* 如果需要设置iconfont与右侧边界的距离，可以使用margin-right */  
  margin-right: 10rpx; /* 可选，用于微调与右侧的间距 */  
}
  

.table{
	display: flex;
	flex-direction: column;
	width:100%
}
.tr{
	display: flex;
}
.thead{
	background-color: #104da4;
	color:#ffffff;
}
.td,.th{
	padding: 20rpx; /* 单元格内边距 */
	border: 1rpx solid #104DA4; /* 设置单元格边框颜色 */  
	box-sizing: border-box; /* 确保边框和内边距包含在宽度内 */  
	text-align: center; /* 文字居中显示 */ 
	height:100rpx;
	line-height: 60rpx;
}

</style>