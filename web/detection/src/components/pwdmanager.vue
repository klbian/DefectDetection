<template>
  <div>
  <div>
    <el-button type="primary" @click="AddMore">添加</el-button>
    <el-button type="danger" @click="deleteMore" style="position: relative;margin-right: 20%">批量删除</el-button>
    <el-input  maxlength="6" show-word-limit ref="loginkeyword" prop="loginkeyword" :rules="rules" v-model="loginkeyword" placeholder="批量设置登入密码(6位)" style="width: 200px; margin-bottom: 20px;"></el-input>
    <el-button type="success" style="position: relative;margin-right: 20%" @click="makeLoginPwd">设置</el-button>
    <el-input v-model="keyword" placeholder="输入姓名或工号进行搜索" style="width: 200px; margin-bottom: 20px;"
              @input="search" prefix-icon="el-icon-search"></el-input>
  </div>
  <div>
    <el-table ref="list" :data="filteroperators" style="width: 100%">
      <el-table-column type="selection" width="50" align="center"></el-table-column>
      <el-table-column prop="name" label="姓名" width="160"></el-table-column>
      <el-table-column prop="jobId" label="工号" width="160"></el-table-column>
      <el-table-column label="登入密码" width="160">
        <template slot-scope="scope">
          <div style="display: flex; align-items: center;">
            <el-input show-password v-model="scope.row.loginPwd"  style="width: 100px; margin-right: 5px"></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作密码" width="160">
        <template slot-scope="scope">
          <el-input show-password v-model="scope.row.opPwd"  style="width: 100px; margin-right: 5px"></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="entryTime" label="入职时间" width="160"></el-table-column>
      <el-table-column prop="remark" label="备注" width="160"></el-table-column>
      <el-table-column label="操作"width="160">
        <template slot-scope="scope">
          <el-button @click="editOperator(scope.row)" type="text" size="small">编辑</el-button>
          <el-button @click="deleteOperator(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="showEditDialog">
      <el-form :model="editForm" :rules="editFormRules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="工号" prop="jobId">
          <el-input v-model="editForm.jobId"></el-input>
        </el-form-item>
        <el-form-item label="登入密码" prop="loginPwd">
          <el-input v-model="editForm.loginPwd"></el-input>
        </el-form-item>
        <el-form-item label="操作密码" prop="opPwd">
          <el-input v-model="editForm.opPwd"></el-input>
        </el-form-item>
        <el-form-item label="入职时间" prop="createTime">
          <el-input v-model="editForm.createTime" type="date"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="saveEdit">确 定</el-button>
    <el-button @click="closeEditDialog">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog  :visible.sync="showAddDialog"  title="操作人员信息添加">
      <el-form :rules="editFormRules" :model="addForm"   label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="工号" prop="jobId">
          <el-input v-model="addForm.jobId"></el-input>
        </el-form-item>
        <el-form-item label="登入密码" prop="loginPwd">
          <el-input  v-model="addForm.loginPwd"></el-input>
        </el-form-item>
        <el-form-item label="操作密码" prop="opPwd">
          <el-input  v-model="addForm.opPwd"></el-input>
        </el-form-item>
        <el-form-item label="入职时间" prop="createTime">
          <el-input v-model="addForm.createTime" type="date"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="addForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="AddMoreTo">确 定</el-button>
    <el-button @click="closeEditDialog">取 消</el-button>
      </span>
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
import dayjs from 'dayjs';
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('useradmin1') || '{}'),
      page: 1,
      pageSize: 10,
      total: 0,
      showAddDialog:false,
      addForm:{
        name: null,
        jobId: null,
        loginPwd: null,
        opPwd: null,
        showLoginPwd: false,
        showOpPwd: false,
        createTime: new Date(),
        remark: null,
        createName:null
      },
      loginkeyword:null,
      keyword:null,
      operators: [
        {
          id:"1",
          name: "John Doe",
          jobId: "A123",
          loginPwd: "password1",
          opPwd: "password2",
          showLoginPwd: false,
          showOpPwd: false,
          createTime: dayjs('2024-02-27').format('YYYY-MM-DD'),
          remark: "Some remark",
          createName:null
        },
      ],
      selectedOperators: [],
      editForm: {
        id:null,
        name: null,
        jobId: null,
        loginPwd: null,
        opPwd: null,
        showLoginPwd: false,
        showOpPwd: false,
        createTime: new Date(),
        remark: null,
        createName:null
      },
      showEditDialog:false,
      mainid:0,
      rules:{
        loginkeyword:[
          { required: true, message: '请输入修改的操作密码', trigger: 'blur' },
          { min: 6, max: 6, message: '操作密码固定长度为6', trigger: 'blur' }
        ]
      },
      editFormRules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        jobId: [
          { required: true, message: '请输入工号', trigger: 'blur' },
          { pattern: /^\d{1,5}$/, message: '工号格式不正确，长度不超过5位数字', trigger: 'blur' }
        ],
        opPwd: [
          { required: true, message: '请输入操作密码', trigger: 'blur' },
          { min: 6, max: 6, message: '操作密码固定长度为6', trigger: 'blur' }
        ],
        entryTime: [{ required: true, message: '请选择入职时间', trigger: 'change' }],
        remark: [
          { max: 255, message: '备注最长255个字符', trigger: 'blur' }
        ]
      }
    };
  },
  mounted(){
    this.fetchOperator();
  },
  computed: {
    filteroperators() {
      if (!this.keyword) {
        return this.operators;
      } else {
        let keyword1 = this.keyword.toString().toLowerCase();
        return this.operators.filter(operator => {
          return (
              (operator.name && typeof operator.name === 'string' && operator.name.toLowerCase().includes(keyword1)) ||
              (operator.jobId && operator.jobId.toString().toLowerCase().includes(keyword1))
          );
        });
      }
    }
  },

  methods: {
    makeLoginPwd() {
      this.$confirm('确定要进行批量设置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.filteroperators.forEach(row => {
          row.loginPwd = this.loginkeyword;
        });
        const dataToSend = this.filteroperators.map(row => {
          return {
            id: row.id,
            loginPwd: this.loginkeyword
          };
        });
        const loginPwd=this.loginkeyword;
        axios.put('api/sysManage/key/batchPwd',null,
            {
              params:{
                loginPwd: loginPwd,
                page:this.page,
                pageSize:this.pageSize
              }
        } )
            .then(response => {
              if(response.data.code === 200){
                this.$message.success("设置成功")
              }else{
                this.$message.error("设置失败");
              }
            })
            .catch(error => {
              this.$message.error("设置失败")
            });
      }).catch(() => {
        this.$message.warning("取消设置")
      });
    },
    AddMore(){
      this.showAddDialog=true;
    },
    AddMoreTo(){
      console.log("这里就是createtime",this.addForm.createTime)
      let a=0;
      let b=0;
      a=this.addForm.createTime.length;
      console.log("1111111",a);
      if(a===10){
        this.addForm.createTime=this.addForm.createTime.concat(" 00:00:00");
      }
      b=this.addForm.createTime.length;
      console.log("bbbbb",b)
      axios.post('api/sysManage/key/add',this.addForm)
          .then(response=>{
            if(response.data.code===200){
              this.$message.success("添加成功");
              this.showAddDialog = false;
              this.fetchOperator();
            }else{
              this.$message.error(response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
            this.$message({
              message:"添加数据失败",
              type:"error"
            })
          });
    },
    search(){
      this.keyword = this.keyword.trim();
    },
    changeLoginT(index) {
      console.log("zhelishi index",index)
      this.operators[index].showLoginPwd = !this.operators[index].showLoginPwd;
      console.log(this.operators[index].showLoginPwd)
      this.$nextTick(() => {
        this.$refs.loginPassword.focus()
        console.log("111111111111111111111")
      })
    },
    changeOpT(index){
      console.log("zhelishi index",index)
      this.operators[index].showOpPwd= !this.operators[index].showOpPwd;
      console.log(this.operators[index].showOpPwd)
      this.$nextTick(() => {
        this.$refs.opPassword.focus()
      })
    },
    fetchOperator(){
      axios.get("api/sysManage/key/info",{
        params:{
          page:this.page,
          pageSize:this.pageSize
        }
      })
          .then(response=>{
            if(response.data.code===200){
              this.total=response.data.data[0].totals;
              this.operators=response.data.data;
              this.operators.forEach(operator=>{
                operator.showLoginPwd=false;
                operator.showOpPwd=false;
                console.log(operator);
              });
              this.$message({
                message:"获取操作信息成功",
                type:"success"
              })
            }
          })
          .catch(error => {
            console.error(error);
            this.$message({
              message:"获取数据失败",
              type:"error"
            })
          });
    },
    editOperator(row) {
      this.editForm.createName=row.createName;
      this.mainid=row.id;
      this.editForm.id=row.id;
      this.editForm.name = row.name;
      this.editForm.jobId = row.jobId;
      this.editForm.loginPwd = row.loginPwd;
      this.editForm.opPwd = row.opPwd;
      this.editForm.showLoginPwd = row.showLoginPwd;
      this.editForm.showOpPwd = row.showOpPwd;
      this.editForm.entryTime = row.entryTime;
      this.editForm.remark = row.remark;
      this.showEditDialog = true;
      this.
      console.log("启动成功",this.editForm)
    },
    saveEdit() {
      const editedData = this.editForm;
      console.log("id",this.mainid);
      const id=this.mainid;
      this.editForm.createTime=editedData.createTime.concat(" 00:00:00")
      axios.put("api/sysManage/key/update",editedData,
          {  headers: {
          'Content-Type': 'application/json'
        }
          }
      )
          .then(response => {
            if(response.data.code===200){
              this.$message.success("修改成功")
              this.showEditDialog = false;
              this.fetchOperator();
            }else{
              this.$message.error("编辑失败")
            }
          })
          .catch(error => {
            console.error('错误信息为', error);
          });
    },
    closeEditDialog(){
      this.showEditDialog = false;
    },
    deleteOperator(row) {
      const id = row.id;
      const ids = [id];
      console.log("这是id",id)
        axios.delete('api/sysManage/key/delete',{
          data: ids
        })
            .then(response=> {
              if(response.data.code===200){
                this.$message.success("删除成功")
                this.fetchOperator();
              }else{
                this.$message.error("删除失败")
              }
            })
            .catch((error) => {
              console.log(error);
              this.$message.error("删除失败")
            });
    },
    deleteMore() {
      const selectedRows = this.$refs.list.selection;
      const listlength=selectedRows.length;
      console.log("长度为",listlength)
      if (listlength === 0) {
        this.$message({
          message: "请先勾选要删除的数据",
          type: "warning"
        });
        return;
      }
      this.$confirm("确认删除选中的操作员吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const selectedIds = selectedRows.map(operator => operator.id);
        // 向后端发送请求删除选中的操作员
        console.log("ids",selectedIds)
        const ids= selectedIds
        axios.delete('api/sysManage/key/delete', {data: ids})
          .then(response => {
            if(response.data.code === 200){
              // 从前端界面中删除选中的操作员
              this.selectedOperators.forEach(operator => {
                const index = this.operators.indexOf(operator);
                if (index !== -1) {
                  this.operators.splice(index, 1);
                }
              });
              this.selectedOperators = [];
              this.$message({
                message: "批量删除成功",
                type: "success"
              });
              this.fetchOperator()
            }else{
              this.$message.error(response.data.message);
            }
        }).catch(error => {
          this.$message.error("批量删除失败");
        });
      }).catch(() => {
        // 用户取消删除操作
      });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchOperator();
    },
    handleCurrentChange(val) {
      this.page = val;
      this.fetchOperator();
    }
  }
};
</script>

<style scoped>
.eye-icon {
  cursor: pointer;
  font-size: 14px;
  color: #909399;
}

.eye-icon.active {
  color: #409EFF;
}
</style>