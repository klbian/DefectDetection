<template>
  <div>
    <div style="display: flex">
      <p style="position: relative;top: -10px;margin-right: 5px">时间段:</p>
      <div style="display: flex;align-items: center;position:relative;top: -9px">
        <el-date-picker
            ref="pagination"
            v-model="dateRange"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
            style="width: 280px; margin-right: 10px;"
        ></el-date-picker>
        <el-button type="success" style="height: 32px" @click="maintime">确认时间段</el-button>
      </div>
    </div>
  <div>
    <el-table :data="tableData" >
      <el-table-column type="index" label="编号" width="180"></el-table-column>
      <el-table-column label="图片" width="180">
        <template slot-scope="scope">
          <el-popover
              placement="right"
              width="400"
              trigger="click">
            <template slot="reference">
              <img :src="getBase64ImageUrl(scope.row.imgBase64)" width="80" height="80" />
            </template>
            <img :src="getBase64ImageUrl(scope.row.imgBase64)" width="400" height="400" />
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="检测时间" sortable></el-table-column>
      <el-table-column prop="workOrderId" label="工单号"></el-table-column>
      <el-table-column prop="defectionsSum" label="缺陷数" sortable></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button @click="handleShow(scope.row)">查看</el-button>
          <el-button @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialogVisible" title="详细信息" width="60%">
      <el-card  >
        <div style="display: flex">
        <div >
          <img :src="getBase64ImageUrl(dialogImageUrl)" width="400" height="400" alt="放大的图片"/>
        </div>
          <div style="position: relative;margin-left: 50px">
            <span style="font-size: 16px;position: relative;">{{ '精确度: ' + this.tableDataShow.score }}</span>
            <br><br>
            <span style="font-size: 16px;position: relative;top: 20px">{{ '横向长度: ' + this.tableDataShow.l }}</span>
            <br><br>
            <span style="font-size: 16px;position: relative;top: 40px">{{ '纵向长度: ' + this.tableDataShow.h }}</span>
            <br><br>
            <span style="font-size: 16px;position: relative;top: 60px">{{ '横坐标: ' + this.tableDataShow.x }}</span>
            <br><br>
            <span style="font-size: 16px;position: relative;top: 80px">{{ '纵坐标: ' + this.tableDataShow.y }}</span>
            <br><br>
            <span style="font-size: 16px;position: relative;top: 100px">{{ '缺陷名称: ' + this.tableDataShow.category }}</span>
            <br><br>
            <span style="font-size: 16px;position: relative;top: 120px">{{ '缺陷类别编号: ' + this.tableDataShow.categoryId }}</span>
            <br><br>
          </div>
        </div>
      </el-card>
    </el-dialog>
    <el-dialog :visible.sync="dialogVisibleimg" style="justify-content: center;align-items: center" title="放大的图片" width="30%">
      <img :src="getBase64ImageUrl(dialogImageUrl)" style="width: 100%;height: 100%" alt="放大的图片"/>
    </el-dialog>
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
import axios from "axios";
import moment from 'moment';
export default {
  name: "history",
  data(){
    return{
      dateRange: [],
      totalPages:null,
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
      tableData:[
      {
        id:1,
        name:"1",
        time:Date.now(),
        workOrderId:1,
        defectionsSum:1,
        imgBase64:null
      }
      ],
      tableDataShow:[
        {
            "score": 0.01,
            "l": 0.01,
            "h": 0.01,
            "x": 0.01,
            "y": 0.01,
            "category": "裂缝1",
            "categoryId":1,
      }
      ],
      dialogVisible: false,
      dialogVisibleimg:false,
      dialogImageUrl : null,
      page: 1,
      pageSize: 10,
      total: 0,
      dateL:null,
      dateR:null,
      imagBase64:null,
      selectedImage : null
    }
  },
  created(){

  },
  mounted() {
    this.fetchData();
  },
  methods: {
    handleImageClick1(imgBase64) {
      this.selectedImage = imgBase64;
    },
    getBase64ImageUrl(base64Data) {
      return `data:image/jpeg;base64,${base64Data}`;
    },
    fetchData() {
      axios.get('api/detectInfo/info/history', {
        params: {
          page: this.page,
          pageSize: this.pageSize,
          dateL:this.dateL,
          dateR:this.dateR
        }
      })
          .then(response => {
            console.log("11111111111111111111", response.data);
            if (response.data.code === 200) {
              this.tableData = response.data.data;
              this.total=this.tableData[0].totals;
              console.log("这里是total",this.total)
              this.$message({
                type: "success",
                message: "查询到历史数据"
              });
              this.tableData.forEach(item => {
                const timestamp = item.time;
                const date = new Date(timestamp);
                console.log("这里是时间戳11111111", date);
                const year = date.getFullYear();
                const month = date.getMonth() + 1;
                const day = date.getDate();
                const hours = date.getHours();
                const minutes = date.getMinutes();
                let seconds = date.getSeconds();

                // 如果秒数为单数，则在前面补上一个 0
                if (seconds < 10) {
                  seconds = '0' + seconds;
                }

                const formattedDateTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
                console.log("格式化后的日期时间字符串：", formattedDateTime);

                item.time = formattedDateTime;
              });

            } else {
              console.error('请求成功，但返回的数据不符合预期', response.data);
            }
          })
          .catch(error => {
            console.error('请求出现错误：', error);
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
      this.fetchData();
    },
    handleShow(row) {
      // 查看按钮点击事件处理
      const  id=row.id;
      this.dialogImageUrl=row.imgBase64;
      console.log(id);
      fetch(`api/detectInfo/info/details?id=${id}`)
          .then(response => response.json())
          .then(response => {
            console.log("22222222222",response.code)
            if (response.code === 200) {
              console.log("111121231231231231231232",response.data.defections)
              if(response.data.defections [0] ===undefined ){
                this.tableDataShow.score = '0';
                this.tableDataShow.h = '0';
                this.tableDataShow.l = '0';
                this.tableDataShow.category = '0';
                this.tableDataShow.categoryId = '0';
                this.tableDataShow.x = '0';
                this.tableDataShow.y = '0';
              }else{
                this.tableDataShow = response.data.defections [0];
              }
              console.log("111121231231231231231232",this.tableDataShow)
              this.imagBase64 = response.data.imgBase64;
              this.dialogVisible=true;
              this.$message({
                type:"success",
                message: "查询到详细信息"
              });
              console.log("初始的tabledataShow数据为：", this.tableDataShow);
            } else {
              console.error('请求成功，但返回的数据不符合预期', response);
            }
          })
    },
    handleDelete(row) {
      console.log('删除列:', row);
      const id = row.id;
      const ids = [id];

      axios.delete('api/detectInfo/info/delete', { data: ids })

          .then(response => {
            console.log(response.data);
            if (response.data.code === 200) {
              const index = this.tableData.findIndex(item => item.id === id);
              if (index !== -1) {
                this.tableData.splice(index, 1);
                this.$message({
                  type: 'success',
                  message: '删除成功'
                });
                this.fetchData();
              } else {
                console.warn('前端数据与要删除的记录不一致');
              }
            } else {
              console.error('删除失败', response.data);
            }
          })
          .catch(error => {
            console.error('删除请求失败', error);
          });
    },
    handleSort({ prop, order }) {
      // 排序的回调函数
      this.tableData.sort((a, b) => {
        const aProp = a[prop];
        const bProp = b[prop];

        if (order === 'ascending') {
          return aProp > bProp ? 1 : -1;
        } else {
          return aProp < bProp ? 1 : -1;
        }
      });
    },
    handleImageClick(imageUrl){
      this.dialogImageUrl = imageUrl; // 设置要展示的图片路径
      this.dialogVisibleimg = true; // 打开 Dialog
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchData();
    },
    handleCurrentChange(val) {
      this.page = val;
      this.fetchData();
      // 判断是否解锁向上页按钮
      if (this.page > 1) {
        this.$refs.pagination.prevDisabled = false;
      } else {
        this.$refs.pagination.prevDisabled = true;
      }
      // 判断是否解锁向下页按钮
      if (this.page < this.totalPages) {
        this.$refs.pagination.nextDisabled = false;
      } else {
        this.$refs.pagination.nextDisabled = true;
      }
    },
  },
};



</script>








<style scoped>

</style>