<template>
  <div>
    <el-row>
      <!-- 左侧的监控画面 -->
      <el-col :span="12">
        <div class="monitoring-screen">
          <img  :src="'data:image/jpeg;base64,' + imageData" alt="监控图像" style="width: 100%; height: 400px;" />
        </div>
        <el-button @click="Refresh">
          刷新
        </el-button>
      </el-col>

      <!-- 右侧的表格 -->
      <el-col :span="12">
        <el-card class="data-table">
          <div slot="header"><i class="el-icon-s-release" style="margin-right:4px"></i>缺陷信息</div>
          <div class="table-container">
          <el-table :data="defectList" style="width: 100%">
            <el-table-column prop="category" label="缺陷名称"></el-table-column>
            <el-table-column prop="score" label="概率"></el-table-column>
          </el-table>
          </div>
          <div class="total-defects" style="position:relative;top: 10px">总缺陷数: {{ defectList.length }}</div>
        </el-card>
      </el-col>
    </el-row>

    <div style="position: relative;top: 15px">
      <!-- 下面的另一个表格 -->
      <el-card class="stats-table" >
        <div slot="header"><i class="el-icon-s-order" style="margin-right:4px"></i>统计信息</div>
        <div class="table-container">
          <el-table :data="statsData" style="width: 100%">
            <el-table-column prop="runTime" label="运行时长" sortable :sort-method="sortOpTime"></el-table-column>
            <el-table-column prop="defectionsSum" label="缺陷总数"></el-table-column>
            <el-table-column prop="defectRate" label="缺陷率"></el-table-column>
            <el-table-column prop="highestOccurrenceDefect" label="最高发缺陷"></el-table-column>
            <el-table-column prop="operation" label="系统最新操作"></el-table-column>
            <el-table-column prop="opTime" label="系统最新操作时间" ></el-table-column>
          </el-table>
        </div>
      </el-card>
    </div>


  </div>
</template>


<script>
export default {
  data() {
    return {
      imageData: null, // 存储监控画面的图片数据
      defectList: [], // 存储缺陷名称和概率的列表
      statsData: [{
        runTime:null,
        defectionsSum:null,
        defectRate:null,
        highestOccurrenceDefect:null,
        operation:null,
        opTime:null
      }
      ] // 存储统计信息
    }
  },
  mounted() {
    // 创建 EventSource 对象以接收监控画面数据
    let eventSourcePicture = new EventSource('api/dashboard/pictureInfo', { retry: 20000});
    eventSourcePicture.onopen = function (event) { // 与服务器连接成功回调
      console.log('成功与服务器连接');
    }
    // 监听服务器发送的监控画面数据事件
    eventSourcePicture.onmessage = event => {
      const data = JSON.parse(event.data); // 假设收到的数据是一个 JSON 字符串
      const imageBase64 = data.imgBase64; // 获取Base64编码的图片数据
      console.log("zhelishi img",imageBase64);
      let statsData1 = [];
      statsData1 = data;
      if (imageBase64 !== null && imageBase64 !== undefined) {
        //console.log("这里是图片数据",imageBase64)
        this.imageData = imageBase64;
        this.defectList = data.defections;
      } else if (statsData1 !== null && statsData1 !== undefined) {
        console.log("statsData1在这", statsData1);
        this.$nextTick(() => {
          statsData1.runTime=this.formatRuntime(statsData1.runTime);
          this.statsData=[];
          this.statsData.push(statsData1);
          console.log("zhelishi",this.statsData)
          let newData = statsData1.latestOperations.map((item, index) => ({
            runTime: null,
            defectionsSum: null,
            defectRate: null,
            highestOccurrenceDefect: null,
            operation: statsData1.latestOperations[index],
            opTime: statsData1.latestOperations[index]
          }));
          this.statsData = this.statsData.concat(newData);

          // 删除属性值为空的对象
          this.statsData = this.statsData.filter(obj => Object.values(obj).some(value => value !== null));

          console.log("statsData在这11111111111111", this.statsData);
        });
      }
    };
  }


,




  methods:{
    formatRuntime(seconds) {
      const hours = Math.floor(seconds / 3600);
      const minutes = Math.floor((seconds % 3600) / 60);
      const remainingSeconds = seconds % 60;
      return `${hours}小时${minutes}分钟${remainingSeconds}秒`;
    },
    sortOpTime(a, b) {
      // 按照opTime属性的时间大小进行排序
      const timeA = new Date(a.runTime).getTime();
      const timeB = new Date(b.runTime).getTime();
      return timeA - timeB;
    },
    Refresh() {
      // 创建 EventSource 对象以接收监控画面数据
      eventSourcePicture.close();
      let eventSourcePicture = new EventSource('api/dashboard/pictureInfo', { retry: 20000
      });
      eventSourcePicture.onopen = function (event) { // 与服务器连接成功回调
        console.log('成功与服务器连接');
      }
      // 监听服务器发送的监控画面数据事件
      eventSourcePicture.onmessage = event => {
        const data = JSON.parse(event.data); // 假设收到的数据是一个 JSON 字符串
        const imageBase64 = data.imgBase64; // 获取Base64编码的图片数据
        let statsData1 = [];
        statsData1 = data;
        if (imageBase64 !== null && imageBase64 !== undefined) {
          // console.log("这里是图片数据",imageBase64)
          this.imageData = imageBase64;
          this.defectList = data.defections;
        } else if (statsData1 !== null && statsData1 !== undefined) {
          console.log("statsData1在这", statsData1);
          this.$nextTick(() => {
            this.statsData.push(statsData1);
            let newData = statsData1.latestOperations.map((item, index) => ({
              runTime: null,
              defectionsSum: null,
              defectRate: null,
              highestOccurrenceDefect: null,
              operation: statsData1.latestOperations[index],
              opTime: statsData1.latestOperations[index]
            }));
            this.statsData = this.statsData.concat(newData);

            // 删除属性值为空的对象
            this.statsData = this.statsData.filter(obj => Object.values(obj).some(value => value !== null));

            console.log("statsData在这", this.statsData);
          });
        }
      };
    }
  }
};
</script>

<style>
.table-container {
  height: 280px; /* 设置表格容器的固定高度 */
  overflow-y: auto; /* 设置垂直方向溢出时显示滚动条 */
}
.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 200px; /* 设置间隔为20% */
  margin-bottom: 3rem;
}


.card {
  background: #fff;
  border-radius: 0.25rem;
  box-shadow: 0 2px 3px rgba(0,0,0,0.1);
  overflow: hidden;
}

.card-header {
  background: #007bff;
  color: brown;
  padding: 0.75rem 1.25rem;
}

.card-body {
  padding: 1.25rem;
}

.table {
  margin-bottom: 0; /* 移除表格默认的底部外边距 */
}

.table-hover tbody tr:hover {
  background-color: saddlebrown;
}

.thead-light th {
  background-color: saddlebrown;
  color: saddlebrown;
}
</style>