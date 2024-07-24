<template>
  <div>
  <div class="container">
    <label class="upload-container">
      <input type="file" @change="handleFileChange" accept="image/*" />
      <div class="upload-icon" v-if="!isUploaded">
        <i class="fas fa-plus"></i>
      </div>
      <span class="icon iconfont" style="position:relative;top: 300px ;margin-left: 35px" v-if="!isUploaded">&#xf0175;</span>
      <div class="image-container" :style="{ backgroundImage: 'url(' + imageUrl + ')' }"></div>
    </label>

    <el-button type="primary" :loading="loading.listloading" @click="Test" style="height:80px;position:relative;top:30vh;margin-left: 20px" round>进行检测</el-button>
    <label class="show-container" style="top: 15px;margin-left: 20px">
      <div class="backend-image" :style="{ backgroundImage: 'url(' + backendImageUrl + ')' }"></div>
    </label>
  </div>
  <div>
    <el-table
        :data="tableData"
        style="width: 100%;position: relative;top:5vh">
      <el-table-column
          prop="number"
          label="序号"
          width="180">
      </el-table-column>
      <el-table-column
          prop="score"
          label="分数"
          width="180">
      </el-table-column>
      <el-table-column
          prop="categoryId"
          label="类别编号"
          width="180">
      </el-table-column>
      <el-table-column
          prop="bbox[0]"
          label="左上点x"
          width="180">
      </el-table-column>
      <el-table-column
          prop="bbox[1]"
          label="左上点y"
          width="180">
      </el-table-column>
      <el-table-column
          prop="bbox[2]"
          label="宽度"
          width="180">
      </el-table-column>
      <el-table-column
          prop="bbox[3]"
          label="高度"
          width="180">
      </el-table-column>
      <el-table-column
          prop="category"
          label="类别"
          width="180">
      </el-table-column>
    </el-table>
  </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 获取1vh对应的像素值
      vhInPixels: window.innerHeight * 0.01,
      isUploaded: false, // 记录上传状态
      imageUrl: '', // 用来存放上传后的图片地址
      backendImageUrl: '', // 用来存放后端传输过来的图片地址
      imageContainerStyle: {
        width: '600px',
        height: '600px'
      },
      image1:null,
      imagebase64:null,
      loading:{
        listloading:false
      },
      formData1:null,
      tableData: [
        {
          number: null,
          score: null,
          categoryId: null,
          bbox:[],
          category: null
        }
      ]
    };
  },
  methods: {
    handleFileChange(event) {
      const file = event.target.files[0];
      // 使用 FileReader 读取图片
      const reader = new FileReader();
      reader.readAsDataURL(file);             //转化成base64    url     返回dataurl很明显
      this.image1=file;
      reader.onload = ( e ) => {
        const imageSrc = reader.result;         //返回文件的内容。只有在读取操作完成后，此属性才有效，返回的数据的格式取决于是使用哪种读取方法来执行读取操作的。
        this.imagebase64=e.target.result;
        // 创建新的图片对象
        const image = new Image();
        image.src = imageSrc;
        // 异步加载图片完成后进行大小设置和绘制
        image.onload = () => {
          const canvas = document.createElement('canvas');     //html5的canvas，document即dom对象的canvas创建方法，（html和js的一个交互接口）
          canvas.width = 600;
          canvas.height = 600;
          const ctx = canvas.getContext('2d');

          // 设置背景颜色为白色
          ctx.fillStyle = "#ffffff"; // 白色
          ctx.fillRect(0, 0, canvas.width, canvas.height);

          // 等比例绘制图片（限制宽度）
          const ratio = canvas.width / image.width;
          const width = image.width * ratio;
          const height = image.height * ratio;
          const x = 0;
          const y = (canvas.height - height) / 2;
          ctx.drawImage(image, x, y, width, height);

          // 导出图片
          const newImageSrc = canvas.toDataURL('image/jpeg', 0.8);//图片质量0.8，较高
          this.imageUrl = newImageSrc;
          this.isUploaded = true; // 设置上传状态为已上传
          // 重置 input 的值，以便下一次选择同名文件可以触发 change 事件
          event.target.value = '';
        };
      };

    },
    Test() {
      this.fetchBackendImage()
      this.loading.listloading = true
    },
    fetchBackendImage() {
      const formData = new FormData();
      formData.append('image', this.image1);

      fetch('http://192.168.17.222:8080/detect', {         //
        method: 'POST',
        body: formData,
      })
          .then((response) => {
            if (response.code === 200) {
              console.log("已经接到图片");
              this.$message({
                type:"success",
                message: "检测完成"
              });
              this.loading.listloading = false;
              return response.json(); // 获取 JSON 格式的响应数据
            } else {
              throw new Error('未能获取后端图片');
            }
          })
          .then((responseData) => {
            console.log(responseData);
            this.displayBase64Image(responseData.data.imgBase64);
            this.tableData=responseData.data.defections;
            this.tableData.forEach((item, index) => {
              item.number = index + 1;
            });
            console.log("数字："+this.tableData.number);
          })
          .catch((error) => {
            console.error('未能获取后端图片', error);
          });
    },
    displayBase64Image(base64String) {
      console.log("nihao"+base64String)
      this.backendImageUrl = `data:image/jpeg;base64, ${base64String}`;
      console.log(this.backendImageUrl)
      // 获取图片容器元素
      const imageContainer = document.querySelector('.backend-image');
      // 创建新的图片对象
      const image = new Image();
      image.src = this.backendImageUrl;
      console.log(this.backendImageUrl)
      // 异步加载图片
      image.onload = () => {
        const canvas = document.createElement('canvas');
        canvas.width = image.width;
        canvas.height = image.height;
        const ctx = canvas.getContext('2d');
        canvas.width = 600;
        canvas.height = 600;
        // 设置背景颜色为白色
        ctx.fillStyle = "#ffffff"; // 白色
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        // 绘制图片
        const ratio = canvas.width / image.width;
        const width = image.width * ratio;
        const height = image.height * ratio;
        const x = 0;
        const y = (canvas.height - height) / 2;
        ctx.drawImage(image, x, y, width, height);



        // 将绘制结果赋值给图片容器作为背景图
        imageContainer.style.backgroundImage = `url(${canvas.toDataURL('image/jpeg', 0.8)})`;
      };
    }
  },
};
</script>

<style>

.container {
  display: flex;
}

.upload-container {
  position: relative;
  display: inline-block;
}

.show-container {
  position: relative;
  display: inline-block;
}

.upload-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 60px;
  height: 60px;
  border: 2px dashed #ccc;
  border-radius: 50%;
  text-align: center;
  cursor: pointer;
}

.upload-icon i {
  font-size: 30px;
  line-height: 60px;
  color: #666;
}

.image-container {
  width: 550px;
  height: 550px;
  border: 2px dashed #ccc;
  padding: 20px;
  text-align: center;
  background-color: white;
}

.image-container p {
  margin-top: 50%;
  transform: translateY(-50%);
}

.backend-image {
  width: 600px;
  height: 600px;
  border: 2px dashed #ccc;
  background-size: cover;
  background-position: center;
}
.iconfont{
  color: #0086b3;
}

</style>