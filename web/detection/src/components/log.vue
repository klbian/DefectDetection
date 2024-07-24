<template>
  <div >
  <div style="display:flex;">
    <p style="position: relative;top: -10px;margin-right: 5px">时间段:</p>
    <div style="display: flex;align-items: center;position:relative;top: -9px">
      <el-date-picker
          v-model="dateRange"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
          style="width: 280px; margin-right: 10px;"
      ></el-date-picker>
      <el-button type="success" style="height: 32px" @click="mainTime">确认时间段</el-button>
    </div>
    <div>
      <el-input v-model="searchName" placeholder="搜索主体或工号" style="margin-left:20px;margin-bottom: 10px;width: 200px" prefix-icon="el-icon-search" @input="search"></el-input>
    </div>
  </div>
  <div>
    <el-table :data="filteredtableData" style="width: 100%">
      <el-table-column type="index" label="编号" width="180"></el-table-column>
      <el-table-column prop="mainRole" label="主体"></el-table-column>
      <el-table-column prop="label" label="工号或账号"></el-table-column>
      <el-table-column prop="op" label="操作"></el-table-column>
      <el-table-column prop="time" label="时间"></el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
  </div>
</template>

<script>
import axios from 'axios';
import moment from "moment/moment";

export default {
  data() {
    return {
      searchName:'',
      tableData: [],
      page: 1,
      pageSize: 10,
      total: 0,
      dateRange: [],
      dateL:'',
      dateR:'',
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
    };
  },
  created() {
    this.fetchData();
  },
  computed:{
    filteredtableData(){
      if (!this.searchName) {
        return this.tableData;
      } else {
        const searchName = this.searchName.toLowerCase();
        return this.tableData.filter(log => {
          return (
              log.mainRole.toLowerCase().includes(searchName) ||
              log.label.toLowerCase().includes(searchName)
          );
        });
      }
    }
  },
  methods: {
    search(){
        this.searchName =this.searchName.trim();
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
      this.fetchData();
    },
    fetchData() {
      axios.get('api/log/info',{
        params:{
          page:this.page,
          pageSize:this.pageSize,
          dateL:this.dateL,
          dateR:this.dateR
        }
      })  // 发起 GET 请求获取数据
          .then(response => {
            if(response.data.code===200){
              this.tableData = response.data.data;  // 将获取的数据填充到tableData中
              console.log("22222222222",response.data.data)
              this.total=this.tableData[0].totals;
              console.log("1111111111111111111",this.total)
              this.$message({
                message: '查询到日志信息',
                type: 'success'
              });
            }else{
              this.$message.error("日志信息获取失败")
            }
          })
          .catch(error => {
            console.error('获取数据失败', error);
            this.$message({
              message: '未能查询到日志信息',
              type: 'error'
            });
          });
    },
    /*fetchData() {
  axios.get('http://localhost:3000/logData', {
    params: {
      pageSize: this.pageSize,
      currentPage: this.currentPage
    }
  })
    .then(response => {
      this.tableData = response.data;  // 将获取的数据填充到tableData中
      this.$message({
        message: '查询到日志信息',
        type: 'success'
      });
    })
    .catch(error => {
      console.error('获取数据失败', error);
      this.$message({
        message: '未能查询到日志信息',
        type: 'error'
      });
    });
},*/
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchData();
    },
    handleCurrentChange(val) {
      this.page = val;
      this.fetchData();
    }
  }
}
</script>