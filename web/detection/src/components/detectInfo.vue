<template>

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
          prop="picture"
          label="图片"
          width="180">
      </el-table-column>
      <el-table-column
          prop="categoryId"
          label="检测时间"
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
</template>

<script>
export default {
  name: "detectInfo"
}
</script>







<style scoped>

</style>