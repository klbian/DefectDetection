<template>
  <div>
    <div style="width: 100%">
      <el-button type="primary" @click="handleAdd">添加</el-button>
      <el-button type="primary" @click="handleAddmore" style="margin-right: 80%">批量添加</el-button>
      <el-button type="danger" @click="deleteMore" >批量删除</el-button>
    </div>
  <el-table :data="tableData" style="width: 100% " @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="50"></el-table-column>
    <el-table-column type="index" label="编号" width="100"></el-table-column>
    <el-table-column prop="createName" label="创建者" width="100"></el-table-column>
    <el-table-column prop="apiKey" label="apiKey" width="120"></el-table-column>
    <el-table-column prop="expirationDate" label="到期日期" width="120">
      <template slot-scope="scope">
        <span>{{ calculateExpirationDate(scope.row.validityPeriod) }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="validityPeriod" label="有效期(天)" width="100">
      <template slot-scope="scope">
        <span>{{ calculateExpirationDate1(scope.row.validityPeriod) }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="validityTimes" label="有效次数" width="100">
      <template slot-scope="scope">
        <span>{{ calculateExpirationDate2(scope.row.validityTimes) }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="permissionLevel" label="权限等级" width="100"></el-table-column>
    <el-table-column prop="status" label="状态" width="120">
      <template slot-scope="scope">
        <span v-if="scope.row.status === 1">启用</span>
        <span v-if="scope.row.status === 0">禁用</span>
      </template>
    </el-table-column>
    <el-table-column prop="remark" label="备注" width="120"></el-table-column>
    <el-table-column label="操作">
      <template slot-scope="scope">
          <div style="display:flex;">
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index)">删除</el-button>
          <el-button size="mini" type="warning" @click="handleedit(scope.$index)">修改</el-button>
          <el-button size="mini" type="primary" @click="handleCopy(scope.$index)">复制</el-button>
          </div>
      </template>
    </el-table-column>
  </el-table>
    <el-dialog title="添加数据" :visible.sync="dialogVisible">
      <el-form ref="formData" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="有效期(天)">
          <el-input v-model="formData.validityPeriod"></el-input>
        </el-form-item>
        <el-form-item label="有效次数">
          <el-input v-model="formData.validityTimes"></el-input>
        </el-form-item>
        <el-form-item label="权限等级">
          <el-select v-model="formData.permissionLevel">
            <el-option label="权限1" value="1"></el-option>
            <el-option label="权限2" value="2"></el-option>
            <el-option label="权限3" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status">
            <el-option label="启用" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addData">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="批量添加数据" :visible.sync="moreAdddialogVisible">
      <el-form ref="formDataMore" :rules="formRules" :model="formDataMore" label-width="100px">
        <el-form-item label="数量" prop="num">
          <el-input v-model="formDataMore.num"></el-input>
        </el-form-item>
        <el-form-item label="有效期(天)">
          <el-input v-model="formDataMore.validityPeriod"></el-input>
        </el-form-item>
        <el-form-item label="有效次数">
          <el-input v-model="formDataMore.validityTimes"></el-input>
        </el-form-item>
        <el-form-item label="权限等级">
          <el-select v-model="formDataMore.permissionLevel">
            <el-option label="权限1" value="1"></el-option>
            <el-option label="权限2" value="2"></el-option>
            <el-option label="权限3" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formDataMore.status">
            <el-option label="启用" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formDataMore.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addDatamore">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑数据" :visible.sync="EditdialogVisible">
      <el-form ref="EditformData" :model="EditformData" :rules="formRules" label-width="100px">
        <el-form-item label="有效期(天)">
          <el-input v-model="EditformData.validityPeriod"></el-input>
        </el-form-item>
        <el-form-item label="有效次数">
          <el-input v-model="EditformData.validityTimes"></el-input>
        </el-form-item>
        <el-form-item label="权限等级">
          <el-select v-model="EditformData.permissionLevel">
            <el-option label="权限1" value="1"></el-option>
            <el-option label="权限2" value="2"></el-option>
            <el-option label="权限3" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="EditformData.status">
            <el-option label="禁用" value="0"></el-option>
            <el-option label="启用" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="EditformData.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="EditdialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="EditData">确 定</el-button>
      </div>
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
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      page: 1,
      pageSize: 10,
      total: 0,
      user: JSON.parse(localStorage.getItem('useradmin') || '{}'),
      formRules: {
        num: [
          { required: true, message: '数量不能为空', trigger: 'blur' },
        ]
      },
      dialogVisible: false,
      moreAdddialogVisible:false,
      tableData: [],
      formData: {
        num:1,
        validityPeriod: '',
        validityTimes: '',
        permissionLevel: '1',
        status: 1,
        remark: '',
        createName:'',
      },
      formDataMore:{
        num:null,
        validityPeriod: '',
        validityTimes: '',
        permissionLevel: '1',
        status: '',
        remark: '',
        createName:'',
      },
      selectedRows: [],
      EditdialogVisible :false,
      EditformData :{
        validityPeriod: '',
        validityTimes: '',
        permissionLevel: '1',
        status: '',
        remark: '',
        createName:'',
      },
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    handleedit(index){
      this.EditformData=[]
      this.EditdialogVisible=true;
      const item = this.tableData[index];
      console.log("item",item);
      this.EditformData = item;
      console.log("1111111111111111111111111111111111111111111111111",this.EditformData)
      this.EditData(this.EditformData);
    },
    async EditData(item) {
        try {
          const response = await axios.patch(`api/sysManage/api/update`, this.EditformData);
          if (response.data.code === 200) { //${this.EditformData.id}
            console.log("111wwwwwwwwwwwwwwwwwwwwwwwww11111111111111111111",this.EditformData)
            this.dialogVisible = false; // 关闭编辑模态框
            this.$message.success("保存编辑数据成功")
            this.fetchData();
          } else {
            this.$message.error("保存编辑数据失败")
          }
        } catch (error) {
          this.$message.error("保存编辑数据失败"+error)
        }
    },

    calculateExpirationDate(validityPeriod) {
      if (validityPeriod < 0) {
        return '无限期';
      } else {
        const today = new Date();
        const expirationDate = new Date(today.getTime() + validityPeriod * 24 * 60 * 60 * 1000);
        return expirationDate.toISOString().slice(0, 10);
      }
    },
    calculateExpirationDate1(validityPeriod) {
      if (validityPeriod < 0) {
        return '无限期';
      } else {
        return validityPeriod;
      }
    },
    calculateExpirationDate2(validityTimes) {
      if (validityTimes < 0) {
        return '无限期';
      } else {
        return validityTimes
      }
    },
    fetchData() {
      axios.get('api/sysManage/api/info', {
        params: {
          page: this.page,
          pageSize: this.pageSize,
          dateL:this.dateL,
          dateR:this.dateR
        }
      })
          .then(response => {
            console.log("response.code",response.data.code);
            if (response.data.code === 200) {
              this.tableData = response.data.data;
              this.total=response.data.data[0].totals;
              this.$message({
                message: "获取数据成功",
                type: "success"
              });
            } else {
              this.$message({
                message: "获取数据失败，错误代码：" + response.data.code,
                type: "error"
              });
            }
          })
          .catch(error => {
            console.error(error);
            this.$message({
              message:"获取数据失败",
              type:"error"
            });
          });
    },
    handleDelete(index) {
      // 删除操作
      const item = this.tableData[index];
      const ids=item.id;
      axios.delete("api/sysManage/api/delete",{
        data: ids
      })
          .then(response => {
            if (response.data.code=== 200) {
              this.tableData.splice(index, 1);
              this.fetchData();
              this.$message({
                message: "删除成功",
                type: "success"
              });
            } else {
              this.$message({
                message:response.data.message,
                type: "error"
              });
            }
          })
          .catch(error => {
            console.error(error);
            this.$message({
              message: "删除失败",
              type: "error"
            });
          });
    },
    handleDisable(index) {
      const item = this.tableData[index];
      axios.put(`api/sysManage/api/111111111111/${item.id}`) // 根据实际情况替换路径和参数
          .then(response => {
            if (item.status === '禁用') {
              this.tableData[index].status = '启用';
            } else {
              this.tableData[index].status = '禁用';
            }
            this.$message({
              message: '修改成功',
              type: 'success'
            });
          })
          .catch(error => {
            console.error(error);
            this.$message({
              message: '修改失败',
              type: 'error'
            });
          });
    },
    handleCopy(index) {
      const item = this.tableData[index];
      if (!item) {
        this.$message({
          message: "没有找到要复制的项",
          type: "warning"
        });
        return;
      }
      const serializedItem = JSON.stringify(item);
      navigator.clipboard.writeText(serializedItem)
          .then(() => {
            this.$message({
              message: "已成功复制到剪贴板",
              type: "success"
            });
          })
          .catch(error => {
            console.error("复制失败", error);
            this.$message({
              message: "复制失败，请重试",
              type: "error"
            });
          });
    },
    handleAdd(){
      this.dialogVisible=true;
    },
    addData() {
      let name = this.user.name;
      this.formData.createName = name;
      this.$refs['formData'].validate((valid) => {
        if (valid) {
          axios.post('api/sysManage/api/add', this.formData)
              .then(response => {
                if (response.data.code === 200) {
                  this.fetchData();
                  this.$message({
                    message: "添加成功",
                    type: "success"
                  });
                  this.fetchData();
                  this.dialogVisible = false;
                } else {
                  this.$message({
                    message: "添加失败",
                    type: "error"
                  });
                }
              })
              .catch(error => {
                this.$message({
                  message: "添加失败",
                  type: "error"
                });
              });
        } else {
          this.$message.warning('apiKey信息不能为空！');
        }
      });
    },
    handleAddmore(){
      this.moreAdddialogVisible=true;
    },
    addDatamore() {
      this.$refs['formDataMore'].validate(function(valid) {
        if (valid) {
          let name = this.user.name;
          this.formDataMore.createName = name;
          axios.post('api/sysManage/api/add', this.formDataMore)
              .then(response => {
                if (response.data.code === 200) {
                  this.fetchData();
                  this.moreAdddialogVisible = false;
                  this.$message({
                    message: "批量添加成功",
                    type: "success"
                  });
                  this.fetchData();
                } else {
                  this.$message({
                    message: "批量添加失败",
                    type: "error"
                  });
                }
              })
              .catch(error => {
                this.$message({
                  message: "批量添加失败",
                  type: "error"
                });
              });
        } else {
          this.$message.warning('请填写完整信息！');
        }
      }.bind(this));
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection;
    },
    deleteMore() {
      if (this.selectedRows.length === 0) {
        this.$message({
          message: "请先勾选要删除的数据",
          type: "warning"
        });
        return;
      }
      const ids = this.selectedRows.map(item => item.id);
      console.log(ids)
      axios.delete("api/sysManage/api/delete", {
              data: ids
          })
          .then(response => {
            if(response.data.code===200){
              this.selectedRows.forEach(item => {
                const index = this.tableData.findIndex(data => data.id === item.id);
                this.tableData.splice(index, 1);
              });
              this.fetchData();
              this.$message({
                message: "批量删除成功",
                type: "success"
              });
            }else{
              this.$message.error("批量删除失败")
            }
          })
          .catch(error => {
            console.error(error);
            this.$message({
              message: "批量删除失败",
              type: "error"
            });
          });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchData();
    },
    handleCurrentChange(val) {
      this.page = val;
      this.fetchData();
    }
  }
};
</script>