<template>
  <div>
    <el-container>
      <!--    侧边栏  -->
      <el-aside :width="asideWidth" style="min-height: 100vh; background-color: #009999">
        <div style="height: 60px; color: white; display: flex; align-items: center; justify-content: center">
          <img src="../assets/软件学院院徽.png" height="40" width="40"/>
          <span class="logo-title" v-show="!isCollapse,!isCollapseInfo,!isCollapseSys " v-bind="admin">{{ this.title}}</span>
        </div>

        <el-menu :default-openeds="['']" :collapse="isCollapse"  :collapse-transition="false" router background-color="#CCFFFF" text-color="#66CCCC"
                 active-text-color="#66CCCC" style="border: none" :default-active="$route.path">
          <el-menu-item index="/dashboard" >
            <i class="el-icon-monitor"></i>
            <span slot="title">概要信息</span>
          </el-menu-item>
          <el-submenu  index="info"  @click.stop.prevent>
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>监测信息</span>
            </template>
            <el-menu-item index="/info">历史检测</el-menu-item>
            <el-menu-item index="/charts">监测报表</el-menu-item>
            <el-menu-item index="/warning">异常管理</el-menu-item>
          </el-submenu>
          <el-menu-item index="/log" >
            <i class="el-icon-monitor"></i>
            <span slot="title">日志管理</span>
          </el-menu-item>
          <el-submenu index="sysmanager"   >
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/apimanager">api管理</el-menu-item>
            <el-menu-item index="/pwdmanager">密匙管理</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <!--        头部区域-->
        <el-header>
          <i :class="collapseIcon" style="font-size: 26px" @click="handleCollapse"></i>
          <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 20px">
            <el-breadcrumb-item :to="{ path: '/' }">主页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div style="flex: 1; width: 0; display: flex; align-items: center; justify-content: flex-end">
            <i class="el-icon-quanping" style="font-size: 26px" @click="handleFull"></i>
            <el-dropdown placement="bottom">
              <div style="display: flex; align-items: center; cursor: default">
                <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="" style="width: 40px; height: 40px; border-radius: 50%; margin: 0 5px">
                <span>{{ user.name }}</span>
              </div>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="$router.push('/person')">个人信息</el-dropdown-item>
                <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>

        </el-header>

        <!--        主体区域-->
        <el-main>
          <router-view @update:user="updateUser" />
        </el-main>

      </el-container>


    </el-container>
  </div>
</template>

<script>

import picture_detection from "@/components/picture_detection.vue";

export default {
  name: 'Home',
  data() {
    return {
      isCollapse: false,  // 不收缩
      isCollapseInfo:false,
      isCollapseSys:false,
      asideWidth: '200px',
      collapseIcon: 'el-icon-s-fold',
      user: JSON.parse(localStorage.getItem('useradmin') || '{}'),
      title : ''
    }
  },
  mounted(){
    this.admin(this.user)
  },
  methods: {
    updateUser(user) {   // 获取子组件传过来的数据  更新当前页面的数据
      this.user = JSON.parse(JSON.stringify(user))  // 让父级的对象跟子级的对象毫无关联
    },
    logout() {
      this.$router.push('/login')
    },
    handleFull() {
      document.documentElement.requestFullscreen()
    },
    handleCollapse() {
      this.isCollapse = !this.isCollapse
      this.isCollapseInfo =!this.isCollapseInfo
      this.isCollapseSys=!this.isCollapseSys
      this.asideWidth = this.isCollapse ? '64px' : '200px'
      this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    },
    handleInfo(index){
    },
    handleSys(e){
      e.stopPropagation();
      this.isCollapseSys = !this.isCollapseSys
    }
  }
}
</script>

<style>
.el-menu--inline {
  background-color: #FFFFCC !important;
}
.el-menu--inline .el-menu-item {
  background-color: #FFFFCC !important;
  padding-left: 49px !important;
}
.el-menu-item:hover, .el-submenu__title:hover {
  color: #FFFFCC !important;
}
.el-submenu__title:hover i {
  color: #FFFFCC !important;
}
.el-menu-item:hover i {
  color: #FFFFCC !important;
}
.el-menu-item.is-active {
  background-color: #336666 !important;
  border-radius: 5px !important;
  width: calc(100% - 8px);
  margin-left: 4px;
}
.el-menu.el-menu-item.is-active{
  background-color: #9999CC !important;
  border-radius: 5px !important;
  width: calc(100% - 8px);
  margin-left: 4px;
}
.el-menu-item.is-active i, .el-menu-item.is-active .el-tooltip{
  margin-left: -4px;
}
.el-menu-item {
  height: 40px !important;
  line-height: 40px !important;
}
.el-submenu__title {
  height: 40px !important;
  line-height: 40px !important;
}
.el-submenu .el-menu-item {
  min-width: 0 !important;
}
.el-menu--inline .el-menu-item.is-active {
  padding-left: 45px !important;
}
/*.el-submenu__icon-arrow {*/
/*  margin-top: -5px;*/
/*}*/

.el-aside {
  transition: width .3s;
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
}
.logo-title {
  margin-left: 5px;
  font-size: 20px;
  transition: all .3s;   /* 0.3s */
}
.el-header {
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
  display: flex;
  align-items: center;
}
</style>