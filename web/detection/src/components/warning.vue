<template>
  <div style="height: 80vh;display:flex; border-radius: 10px; border: 1px solid #ccc; padding: 20px;" >
    <div style="height: 75vh; width: 90vh; margin-right: 20px; border-radius: 10px; border: 1px solid #ccc; padding: 20px;overflow: auto">
      <el-divider ><h1 style="color: #669999;font-size: 26px">预警信息</h1></el-divider>
      <div>
        <el-input v-model="searchName" placeholder="搜索预警名称" style="margin-bottom: 10px;width: 200px" prefix-icon="el-icon-search"></el-input>
        <el-select v-model="searchLevel" placeholder="搜索紧急程度" style="margin-bottom: 10px" prefix-icon="el-icon-search">
          <el-option label=3 value="3"></el-option>
          <el-option label=2 value="2"></el-option>
          <el-option label=1 value="1"></el-option>
          <el-option label=无限制 value=null></el-option>
        </el-select>
      </div>
      <el-table :data="filteredWarnings" border>
        <el-table-column prop="type" label="预警名称"></el-table-column>
        <el-table-column prop="level" label="紧急程度"></el-table-column>
        <el-table-column prop="createTime" label="发生时间"></el-table-column>
        <el-table-column prop="content" label="预警信息"></el-table-column>
      </el-table>
      <div class="total-defects" style="position:relative;top: 10px">总报警数: {{ warningsList.length }}</div>
    </div>

  <div style="display: block;border-radius: 10px; border: 1px solid #ccc; padding: 20px;">
    <div style="width: 50vh;position:relative;top: -10px ">
      <el-card class="info-card" shadow="hover" style="max-height: 12vh;" body-style="padding-bottom: 0;">
        <div slot="header" class="info-header" style="height: 10px;font-size: 20px;position: relative;top: -3vh">告警统计</div>
        <el-descriptions column :bordered="false"  size="middle" style="position:relative;top: -2vh ; border-width: 0px" >
          <el-descriptions-item label="告警总数">{{ warningsSum }}</el-descriptions-item>
          <el-descriptions-item label="24小时内告警数">{{ oneDayWarningsSum }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>

    <div style="width: 50vh;height:19vh;border-radius: 10px; border: 1px solid #ccc; padding: 20px;">
      <div :id="chartId" style="width: 100%; height: 140%;"></div>
    </div>

    <div style="position: relative; top: 10px; height: 100%">
      <el-card class="info-card" shadow="hover" style="max-height:27vh" >
        <div slot="header" class="info-header" style="height: 10px; font-size: 20px;position: relative;top: -2vh">告警通知设置</div>
        <el-descriptions column :bordered="true" style="position:relative;top: -1vh">
          <el-descriptions-item label="告警通知">
            <el-radio-group v-model="warningsOpen">
              <el-radio prop="warningOpen" :label="true">启用</el-radio>
              <el-radio prop="warningOpen" :label="false">关闭</el-radio>
            </el-radio-group>
          </el-descriptions-item>
          <el-descriptions-item label="告警级别">
            <el-radio-group v-model="warningsLevel">
              <el-radio prop="warningsLevel" label=1>1</el-radio>
              <el-radio prop="warningsLevel" label=2>2</el-radio>
              <el-radio prop="warningsLevel" label=3>3</el-radio>
            </el-radio-group>
          </el-descriptions-item>
          <el-descriptions-item label="通知方式">
            <el-radio-group v-model="Way">
              <el-radio prop="Way" label="1">手机</el-radio>
              <el-radio prop="Way" label="2">邮箱</el-radio>
            </el-radio-group>
          </el-descriptions-item>
          <el-descriptions-item label="手机">
            <el-input v-model="phone"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="邮箱">
            <el-input v-model="email"></el-input>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
  </div>
  </div>
</template>

<script>
import axios from 'axios';
import * as echarts from 'echarts';

export default {
  data() {
    return {
      warningsOpen: "true",
      warningsLevel: "1",
      Way: "1",
      phone: '',
      email: '',
      searchName: '',
      searchLevel: '',
      warningsList: [],
      warningsSum: 0,
      oneDayWarningsSum: 0,
      levelDistributionData: {},
      notificationLevel: 0,
      notificationMethod: '',
      phoneNumber: '',
      emailAddress: '',
      chartId: 'myChart', // 图表容器的 id
      barChart: {
        title: {
          text: '紧急程度分布'
        },
        tooltip: {},
        xAxis: {
          data: [] // 在数据加载前先清空 x 轴的标签数据
        },
        yAxis: {},
        series: [{
          name: '紧急程度分布',
          type: 'bar',
          data: [] // 在数据加载前先清空柱状图的数据
        }]
      },
    };
  },
  computed: {
    filteredWarnings() {
      let searchLevel = this.searchLevel;
      if (searchLevel === "null") {
        return this.warningsList;
      }
      if (!this.searchName && !searchLevel) {
        return this.warningsList;
      }
      console.log("this.seratch",this.searchName);
      return this.warningsList.filter(warning => {
        const levelMatch = !searchLevel || warning.level == searchLevel;
        const nameMatch = !this.searchName || (!!warning.type && warning.type.includes(this.searchName.trim().toLowerCase()));
        return nameMatch && levelMatch;
      });
    },
  },
  mounted() {
      this.fetchData();
  },
  watch: {
    warningsOpen(newVal, oldVal) {
      //this.sendDataToBackend();
      console.log('告警通知开关变化:', newVal);
    },
    warningsLevel(newVal, oldVal) {
      //this.sendDataToBackend();
      console.log('告警级别变化:', newVal);
    },
    Way(newVal, oldVal) {
      //this.sendDataToBackend();
      console.log('通知方式变化:', newVal);
    },
    phone(newVal, oldVal) {
     // this.sendDataToBackend();
      console.log('手机变化:', newVal);
    },
    email(newVal, oldVal) {
      //this.sendDataToBackend();
      console.log('邮箱变化:', newVal);
    }
  },
  methods:{
    fetchData(){
      axios.get('api/detectInfo/warnings/load')
          .then(response => {
            if(response.data.code === 200){
              console.log("11111111111",response.data.data.warningsOpen)
              if(response.data.data.warningsOpen === false){
                this.warningsOpen=false
              }else{
                this.warningsOpen=true
              }
              this.warningsList = response.data.data.warningsList;
              this.warningsSum = response.data.data.warningsSum;
              this.oneDayWarningsSum = response.data.data.oneDayWarningsSum;
              this.warningsLevel = response.data.data.warningsLevel;
              this.phoneWay = response.data.data.phoneWay;
              this.emailWay=response.data.data.emailWay;
              if(this.phoneWay === true){
                this.Way=1;
              }else if(this.emailWay === true){
                this.Way=2
              }else {
                this.Way=0;
              }
              ;
              this.phone = response.data.data.phone;
              this.email = response.data.data.email;
              this.barChart = response.data.data.barChart;
              console.log("1111111112222222222222222",this.warningsOpen,this.warningsLevel,this.Way,this.barChartOptions);
              this.drawChart();
            }else{
              this.$message.error(response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    drawChart() {
      const xAxisData = [];
      const seriesData = [];
      for (const key in this.barChart.source) {
        if (this.barChart.source.hasOwnProperty(key)) {
          xAxisData.push(key); // 将键作为 x 轴数据
          seriesData.push(this.barChart.source[key]); // 将值作为柱状图 series 数据
        }
      }
      const option = {
        title: {
          text: this.barChart.name,
        },
        xAxis: {
          type: 'category',
          data: xAxisData,
        },
        yAxis: {
          type: 'value',
        },
        series: [{
          type: 'bar',
          data: seriesData,
          itemStyle: {
            color: '#669999'
          }
        }],
      };
      const chartDom = document.getElementById(this.chartId);
      const myChart = echarts.init(chartDom);
      myChart.setOption(option);
      console.log("chart", option);
    },
    createBarOption(chart) {
      // 将后端给出的键值对数据转换为 ECharts 所需的格式
      const xAxisData = [];
      const seriesData = [];
      for (const key in chart.source) {
        if (chart.source.hasOwnProperty(key)) {
          xAxisData.push(key); // 将键作为 x 轴数据
          seriesData.push(chart.source[key]); // 将值作为柱状图 series 数据
        }
      }
      return {
        title: {
          text: chart.name, // 图表名称
        },
        xAxis: {
          type: 'category',
          data: xAxisData
        },
        yAxis: {
          type: 'value'
        },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            return '(' + params[0].name + ', ' + params[0].value + ')';
          }
        },
        series: [{
          type: 'bar',
          data: seriesData,
          itemStyle: {
            color: '#669999'
          }
        }]
      };
    },
    sendDataToBackend() {
      const data = {
        warningsOpen: this.warningsOpen,
        warningsLevel: this.warningsLevel,
        Way: this.Way,
        phone: this.phone,
        email: this.email
      };
      axios.put('http://localhost:3000/warningdata', data)
          .then(response => {
            console.log('Data sent to backend successfully:', response.data);
          })
          .catch(error => {
            console.error('Error sending data to backend:', error);
          });
    }
  }
};
</script>


<style scoped>
.info-card {
  width: 50vh;
  border-radius: 10px;
  border: 1px solid #ccc;
  padding: 20px;
}

.info-header {
  font-size: 12px;
  font-weight: bold;
}
.el-card-define{
  min-height:100%;
  height:100%
}
</style>