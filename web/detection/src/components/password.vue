<template>
  <div>
    <el-card style="width: 50%;margin-left: 25%;position:relative;">
      <el-form ref="formRef" :model="user" :rules="rules" label-width="80px" style="padding-right: 20px">
        <div style="margin: 15px; text-align: center">
          <img src="../assets/软件学院院徽.png" height="124" width="125"/>
        </div>
        <div style="border: #000c17;margin-left: 30%;font-size: 20px">钢铁检测系统密码修改处</div>
        <el-form-item label="原始密码" prop="pwd">
          <el-input show-password v-model="this.pwd" placeholder="原始密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPwd">
          <el-input show-password v-model="user.newPwd" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd">
          <el-input show-password v-model="user.confirmPwd" placeholder="再次输入新密码"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">确认修改</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Password",
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.confirmPwd) {
        callback(new Error('确认密码需和新密码一致'))
      } else {
        callback()
      }
    }

    return {
      pwd:null,
      user: JSON.parse(localStorage.getItem('useradmin') || '{}'),
      user1: JSON.parse(localStorage.getItem('useradmin1') || '{}'),
      rules: {
        pwd: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPwd: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPwd: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  created() {
      this.pwd=this.user1.pwd;
  },
  methods: {
    update() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.user.pwd = this.user.newPwd;
          // 保存当前的用户信息到数据库
          this.$request.put('/user/savePwd', this.user,{
            params:{
              oldPwd:this.pwd,
              newPwd:this.user.newPwd
            }
          }).then(res => {
            if (res.code === '200') {
              // 成功更新
              this.$message.success('保存成功')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
</style>