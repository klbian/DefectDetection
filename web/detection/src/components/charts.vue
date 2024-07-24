<template>
  <div>
    <div style="display:flex;">
      <p style="position: relative;top: -10px;margin-right: 5px">N:</p>
      <el-input v-model="N" type="text" placeholder="请在此处输入n的值" @blur="setN" style="width: 200px;position: relative;margin-right: 13%"> </el-input>
      <p style="position: relative;top: -10px;margin-right: 5px">工单号:</p>
      <el-input v-model="order" type="text" style="width: 200px;position: relative;margin-right: 13%" placeholder="请在此输入工单号" @blur="setOrder" ></el-input>
    </div>
  <div style="height: 210px;width: 100%;display: flex" >
    <div style="height: 210px;width: 100vh" :id="chartIds[0] "></div>
    <div style="height: 210px;width: 100vh" :id="chartIds[1]"></div>
  </div>
  <div style="height: 210px;width: 100%;display: flex">
    <div style="height: 210px;width: 100vh" :id="chartIds[2]"></div>
    <div style="height: 210px;width: 100vh" :id="chartIds[3]"></div>
  </div>
  <div style="height: 210px;width: 100%;display: flex">
    <div style="height: 210px;width: 100vh" :id="chartIds[4]"></div>
    <div style="height: 210px;width: 100vh" :id="chartIds[5]"></div>
  </div>
    <div style="display: flex;position: relative;top: -30px">
      <p>上次刷新时间：</p>
      <el-button @click="refresh">刷新</el-button>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import axios from "axios";
import moment from 'moment';
export default {
  data() {
    return {
      dateRange: [],
      dateL:null,
      dateR:null,
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = moment().endOf('day');
              const start = moment(end).subtract(1, 'week').startOf('day');
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '最近一月',
            onClick(picker) {
              const end = moment().endOf('day');
              const start = moment(end).subtract(1, 'month').startOf('day');
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '最近一年',
            onClick(picker) {
              const end = moment().endOf('day');
              const start = moment(end).subtract(1, 'year').startOf('day');
              picker.$emit('pick', [start, end]);
            }
          }
        ],
        disabledDate(time) {
          return time.getTime() > Date.now(); // 禁止选择未来日期
        }
      },
      charts: [],
      chartIds: ['chart1', 'chart2', 'chart3', 'chart4','chart5','chart6'],
      N:null,
      startTime:'',
      endTime:'',
      order:'',
    };
  },
  computed: {
    isBothInputsFilled() {
      return this.startTime !== '' && this.endTime !== '';
    }
  },
  mounted() {
    this.fetchChartsData();
  },
  methods: {
    setN() {
      axios.put("api/detectInfo/charts/set",null,{
        params:{
          N:this.N,
          order:this.order
        }
      })
          .then(response => {
            if (response.data.code === 200) {
              console.log("11111111111",this.charts);
              this.charts=response.data.data;
              this.renderCharts()
              console.log(this.charts)
              this.$message.success("N设置成功");
            } else {
              this.$message.error("N设置失败");
            }
          })
          .catch(error => {
            this.$message.error("N设置失败");
          });
    },
    setOrder() {
      axios.put("api/detectInfo/charts/set",null,{
        params:{
          N:this.N,
          order:this.order
        }
      })
          .then(response => {
            if (response.data.code === 200) {
              this.charts=response.data.data;
              this.renderCharts();
              this.$message.success("工单号设置成功");
            } else {
              this.$message.error("工单号设置失败");
            }
          })
          .catch(error => {
            this.$message.error("工单号设置失败");
          });
    },
    maintime() {
      console.log("这里是时间段哦", this.dateRange);
      let startTime = new Date(this.dateRange[0]); // 将开始时间字符串转换为日期对象
      let endTime = new Date(this.dateRange[1]); // 将结束时间字符串转换为日期对象
      startTime.setHours(23, 59, 59, 999); // 将开始时间设置为当天的23:59:59
      endTime.setHours(23, 59, 59, 999); // 将结束时间设置为当天的23:59:59
      this.dateL = startTime.getTime(); // 获取开始时间的时间戳
      this.dateR = endTime.getTime(); // 获取结束时间的时间戳
      console.log("开始时间的时间戳：", this.dateL);
      console.log("结束时间的时间戳：", this.dateR);
      axios.put("api/detectInfo/charts/set")
          .then(response => {
            if (response.data.code === 200) {
              this.$message.success("时间段设置成功");
            } else {
              this.$message.error("时间段设置失败");
            }
          })
          .catch(error => {
            this.$message.error("时间段设置失败");
          });
    },
    fetchChartsData() {
      fetch('api/detectInfo/charts/load')
          .then(response => {
            if (response.status === 200) {
              return response.json();
            } else {
              throw new Error('状态码非200，无法获取数据');
            }
          })
          .then(data => {
            console.log("111111111111111111111111",data.data);
            this.charts = data.data;
            console.log(this.charts[0]);
            this.renderCharts();
            this.$message({
              message: '查询到报表信息',
              type: 'success'
            });
          })
          .catch(error => {
            console.error(error);
            this.$message({
              message: '未能查询到报表信息',
              type: 'error'
            });
          });
    },

    refresh(){
      this.fetchChartsData();
    },
    renderCharts() {
      for (let i = 0; i < this.charts.length; i++) {
        this.renderChart(this.charts[i], this.chartIds[i]);
      }
    },
    renderChart(chart, containerId) {
      const myChart = echarts.init(document.getElementById(containerId));
      let option = {};
      switch(chart.type) {
        case '折线图':
          option = this.createLineOption(chart);
          break;
        case '柱状图':
          option = this.createBarOption(chart);
          break;
        case '环形图':
          option = this.createPieOption(chart);
          break;
        case '热力图':
          option = this.createHeatmapOption(chart);
          break;
      }
      // 添加导出配置选项
      option.toolbox = {
        feature: {
          saveAsImage: {}, // 开启导出为图片的功能
          saveAsExcel: { // 添加自定义Excel导出按钮
            show: true,
            title: '导出Excel',
            iconStyle: {
              color: '#663333',
              emphasis: {
                iconStyle: {
                  color: '#009999'
                }
              }
            },
            onclick: () => {
              this.exportChartToExcel(myChart, chart.name); // 调用导出为Excel的方法
            }
          }
        }
      };
      myChart.setOption(option);
    },
    createLineOption(chart) {
      // 将后端给出的键值对数据转换为 ECharts 所需的格式
      const xAxisData = [];
      const seriesData = [];
      for (const key in chart.source) {
        if (chart.source.hasOwnProperty(key)) {
          xAxisData.push(key); // 将 x 数据添加到 xAxisData
          seriesData.push(chart.source[key]); // 将对应的 y 数据添加到 seriesData
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
          type: 'line',
          data: seriesData,
          lineStyle:{
            color:'#669999'
          }
        }]
      };
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

    createPieOption(chart) {
      // 将后端给出的键值对数据转换为 ECharts 所需的格式
      const legendData = Object.keys(chart.source);
      const seriesData = [];
      for (const key in chart.source) {
        if (chart.source.hasOwnProperty(key)) {
          seriesData.push({ name: key, value: chart.source[key] }); // 使用键值对数据，分别为 name 和 value
        }
      }

      return {
        title: {
          text: chart.name, // 图表名称
        },
        legend: {
          data: legendData,
          top:'30px',
          orient:'vertical',
          left:'',
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        series: [{
          type: 'pie',
          data: seriesData,
          //center:['50%','60%'],
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)',
            }
          }
        }],
      };
    },
    createHeatmapOption(chart) {
      return {
        title: {
          text: chart.name
        },
        tooltip: {
          position: 'top'
        },
        xAxis: {
          type: 'category',
          data: chart.source.xAxisData
        },
        yAxis: {
          type: 'category',
          data: chart.source.yAxisData
        },
        visualMap: {
          min: 0,
          max: 100,
          calculable: true,
          orient: 'horizontal',
          left: 'center',
          bottom: '15%',
          padding: [10, 20],
          inRange: {
            color: ['#b2f0e6', '#006400']
          }
        },
        series: [{
          name: chart.name,
          type: 'heatmap',
          data: chart.source.seriesData,
          label: {
            show: true
          },
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      };
    },
    exportChartToExcel(myChart, chartName) {
      const option = myChart.getOption();
      let excelData = null;
      switch (option.series[0].type) {
        case 'line':
        case 'bar':
          excelData = this.extractSeriesData(option.series[0].data);
          break;
        case 'pie':
          excelData = this.extractPieData(option.series[0].data);
          break;
        case 'heatmap':
          excelData = this.extractHeatmapData(option.series[0].data);
          break;
      }
      // 导出excelData到Excel文件的逻辑
    },

// 提取线性和柱状图表数据
    extractSeriesData(data) {
      return data.map(item => ({
        name: item.name,
        value: item.value
      }));
    },

// 提取饼图数据
    extractPieData(data) {
      return data.map(item => ({
        name: item.name,
        value: item.value
      }));
    },

// 提取热力图数据
    extractHeatmapData(data) {
      return data.map(item => ({
        x: item.x,
        y: item.y,
        value: item.value
      }));
    }
  }
};
</script>