<template>
  <div id="myDiv" style="height: 100vh; display: flex; align-items: center; justify-content: center;background-size: cover">
    <div style="display: flex; width: 50%; border-radius: 5px; overflow: hidden ;">
      <div style="flex: 1">
        <img src="../assets/123123.jpg" alt="" style="width: 100%;height: 80% ; opacity: 0.7">
      </div>
      <div style="flex: 1; display: flex; align-items: center; justify-content: center">
        <el-form :model="user" style="width: 80%" :rules="rules" ref="loginRef">     <!--这里是为了让后面有一个改的参数-->
          <div style="font-size: 20px; font-weight: bold; text-align: center; margin-bottom: 20px;color: white">欢迎登录钢铁检测</div>
          <el-form-item prop="account">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入账号" v-model="user.account"></el-input>
          </el-form-item>
          <el-form-item prop="pwd">
            <el-input prefix-icon="el-icon-lock" size="medium" show-password placeholder="请输入密码" v-model="user.pwd"></el-input>
          </el-form-item>
          <el-form-item prop="code">
            <div style="display: flex">
              <el-input placeholder="请输入验证码" prefix-icon="el-icon-circle-check" size="medium" style="flex: 1" v-model="user.code"></el-input>
              <div style="flex: 1; height: 36px">
                <valid-code @update:value="getCode" />
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="login">登录</el-button>
          </el-form-item>
          <div style="display: flex">
            <div style="flex: 1; text-align: right"><span style="color: #0f9876; cursor: pointer" @click="handleForgetPass">忘记密码</span></div>
          </div>
        </el-form>
      </div>
    </div>
    <el-dialog title="忘记密码" :visible.sync="forgetPassDialogVis" width="30%">
      <el-form :model="forgetUserForm" label-width="80px" style="padding-right: 20px">
        请咨询管理员获取相关信息
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import ValidCode from "@/components/ValidCode.vue";
import axios from "axios";
export default {
  name: "Login",
  components: {
    ValidCode
  },
  data() {
    // 验证码校验
    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入验证码'))
      } else if (value.toLowerCase() !== this.code) {
        callback(new Error('验证码错误'))
      } else {
        callback()
      }
    }

    return {
      forgetUserForm: {}, // 忘记密码的表单数据
      forgetPassDialogVis: false,
      code: '', // 验证码组件传递过来的code
      code1:'',
      user: {
        code: '', // 表单里用户输入的code 验证码
        account: 'admin1',
        pwd: '123456',
        id:1,
        name:'admin',
        phoneNumber:'123456789',
        email:'10@qq.com'
      },
      rules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        pwd: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        code: [
          { validator: validateCode, trigger: 'blur' }
        ],
      }
    }
  },
  mounted() {
    const myDiv = document.getElementById('myDiv');
    myDiv.style.backgroundImage = `url(${require('@/assets/背景图片1.jpg')})`;
    localStorage.setItem('useradmin1', JSON.stringify(this.user))
  },
  created() {

  },
  methods: {
    handleForgetPass() {   //  初始化表单的数据
      this.forgetUserForm = {}
      this.forgetPassDialogVis = true
    },
    getCode(code) {
      this.code = code.toLowerCase();
      console.log("1111111this is code",this.code)
    },
    login() {
      const user=this.user;
      const account=user.account;
      const pwd=user.pwd;
      this.$refs['loginRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          console.log("1111111111",this.code1)
          axios.post('api/login/in',
              {account:account,pwd:pwd})
            .then(res => {
              console.log("这里是code",res.data.code)
            if (res.data.code === 200) {
              localStorage.setItem('useradmin1', JSON.stringify(this.user))
              this.$message.success('登录成功')
              localStorage.setItem("useradmin", JSON.stringify(res.data.data)) // 存储用户数据
              this.$router.push('/dashboard')
            } else {
              this.$message.error(res.data.message)
            }
          })
        }
        else {
          this.$message.error("输入格式错误")
        }
      })
    }
  }
}
</script>

<style scoped>


</style>