<template>
  <div>
    <el-card style="width: 50%;margin-left: 25%">
      <el-form :model="user" label-width="80px" style="padding-right: 20px">
        <div style="margin: 15px; text-align: center">
          <img src="../assets/软件学院院徽.png" height="124" width="125"/>
        </div>
        <div style="border: #000c17;margin-left: 28%;font-size: 20px">钢铁检测管理系统个人中心</div>
        <p></p>
        <el-form-item label="账号" prop="account">
          <el-input v-model="user.account" placeholder="账号" ></el-input>
        </el-form-item>
        <p></p>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名" ></el-input>
        </el-form-item>
        <p></p>
        <el-form-item label="手机号" prop="phoneNumber">
          <el-input v-model="user.phoneNumber" placeholder="11位手机号" ></el-input>
        </el-form-item>
        <p></p>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱" ></el-input>
          <el-tooltip content="密码修改请到密码修改界面进行" placement="top-end">
            <i class="el-icon-info" style="margin-left: 100%"></i>
          </el-tooltip>
        </el-form-item>
        <div ref="tooltip"></div>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Person",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('useradmin1') || '{}')
    }
  },
  methods: {
    update() {
      // 保存当前的用户信息到数据库
      this.$request.put('/user/saveInfo', null,{
        params:{
          account:this.user.account,
          phoneNumber:this.user.phoneNumber,
          name:this.user.name,
          email:this.user.email
        }
      }).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('保存成功')
          // 更新浏览器缓存里的用户信息
          localStorage.setItem('useradmin1', JSON.stringify(this.user))
          // 触发父级的数据更新
          this.$emit('update:user', this.user)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>