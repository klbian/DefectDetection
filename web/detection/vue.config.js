module.exports={
  devServer:{
    port:"8080", // 设置端口 默认8080
    open:true, // 项目运行自动打开浏览器 偷懒一波
    // 在与port open 设置服务代理
    proxy:{
      // /api 自定义服务代理名字
      "/api":{
        target:"http://localhost:8081", //代理帮助你请求的具体服务http://localhost:8081
        changeOrign:true, // 开启代理
        pathRewrite:{  // 格式化path
          "^/api":""
        }
      }
    }
  }
}
