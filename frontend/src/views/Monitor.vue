<template>
  <div class="monitor">
    <el-card>
      <template #header>📹 实时监测</template>
      <el-result
        icon="info"
        title="开发中"
        sub-title="监测功能正在开发中..."
      >
        <template #extra>
          <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
        </template>
      </el-result>
    </el-card>
  </div>
</template>

<script setup>
</script>
