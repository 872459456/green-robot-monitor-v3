<template>
  <div class="iterations-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>📋 迭代日志</span>
          <el-button type="primary" @click="showAddDialog = true">
            <el-icon><Plus /></el-icon> 新增迭代
          </el-button>
        </div>
      </template>

      <!-- 筛选 -->
      <el-row :gutter="20" class="filters">
        <el-col :span="6">
          <el-select v-model="filterStatus" placeholder="状态" clearable>
            <el-option label="全部" value="" />
            <el-option label="规划中" value="PLANNING" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="filterVersion" placeholder="版本号" clearable />
        </el-col>
      </el-row>

      <!-- 迭代列表 -->
      <el-timeline>
        <el-timeline-item
          v-for="log in filteredLogs"
          :key="log.id"
          :timestamp="formatDate(log.iterationDate)"
          :type="getStatusType(log.status)"
          placement="top"
        >
          <el-card>
            <h3>
              {{ log.version }} - {{ log.phase }}
              <el-tag :type="getStatusTagType(log.status)" size="small">
                {{ getStatusText(log.status) }}
              </el-tag>
            </h3>
            <p v-if="log.requirement"><strong>需求:</strong> {{ log.requirement }}</p>
            <p v-if="log.changes"><strong>修改:</strong></p>
            <div v-if="log.changes" v-html="formatChanges(log.changes)"></div>
            <p v-if="log.decisionBasis"><strong>决策依据:</strong> {{ log.decisionBasis }}</p>
            <p v-if="log.owner"><strong>负责人:</strong> {{ log.owner }}</p>
            <div class="actions">
              <el-button size="small" @click="editLog(log)">编辑</el-button>
              <el-button size="small" type="danger" @click="deleteLog(log.id)">删除</el-button>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingLog ? '编辑迭代' : '新增迭代'"
      width="600px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="版本号">
          <el-input v-model="form.version" placeholder="如: v3.0.0" />
        </el-form-item>
        <el-form-item label="阶段">
          <el-input v-model="form.phase" placeholder="如: 基础设施" />
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="form.iterationDate" type="datetime" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="规划中" value="PLANNING" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item label="需求描述">
          <el-input v-model="form.requirement" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="修改内容">
          <el-input v-model="form.changes" type="textarea" rows="4" placeholder="JSON数组格式，如: [&#34;- 新增功能A&#34;, &#34;- 修复bug&#34;]" />
        </el-form-item>
        <el-form-item label="决策依据">
          <el-input v-model="form.decisionBasis" type="textarea" rows="2" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.owner" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.notes" type="textarea" rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveLog">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const API_BASE = 'http://localhost:8080/api'

const logs = ref([])
const showAddDialog = ref(false)
const editingLog = ref(null)
const filterStatus = ref('')
const filterVersion = ref('')

const form = ref({
  version: '',
  phase: '',
  iterationDate: new Date(),
  requirement: '',
  changes: '',
  decisionBasis: '',
  owner: '狼群',
  status: 'PLANNING',
  notes: ''
})

const filteredLogs = computed(() => {
  return logs.value.filter(log => {
    if (filterStatus.value && log.status !== filterStatus.value) return false
    if (filterVersion.value && !log.version.includes(filterVersion.value)) return false
    return true
  })
})

const formatDate = (date) => {
  return new Date(date).toLocaleString('zh-CN')
}

const getStatusType = (status) => {
  const map = {
    'PLANNING': 'info',
    'IN_PROGRESS': 'primary',
    'COMPLETED': 'success'
  }
  return map[status] || 'info'
}

const getStatusTagType = (status) => {
  const map = {
    'PLANNING': 'info',
    'IN_PROGRESS': 'warning',
    'COMPLETED': 'success'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'PLANNING': '规划中',
    'IN_PROGRESS': '进行中',
    'COMPLETED': '已完成'
  }
  return map[status] || status
}

const formatChanges = (changes) => {
  try {
    const arr = JSON.parse(changes)
    return '<ul>' + arr.map(c => `<li>${c}</li>`).join('') + '</ul>'
  } catch {
    return changes
  }
}

const fetchLogs = async () => {
  try {
    const res = await axios.get(`${API_BASE}/iterations`)
    logs.value = res.data
  } catch (err) {
    console.error('Failed to fetch logs:', err)
  }
}

const saveLog = async () => {
  try {
    if (editingLog.value) {
      await axios.put(`${API_BASE}/iterations/${editingLog.value.id}`, form.value)
      ElMessage.success('更新成功')
    } else {
      await axios.post(`${API_BASE}/iterations`, form.value)
      ElMessage.success('创建成功')
    }
    showAddDialog.value = false
    editingLog.value = null
    fetchLogs()
  } catch (err) {
    ElMessage.error('保存失败')
  }
}

const editLog = (log) => {
  editingLog.value = log
  form.value = { ...log }
  showAddDialog.value = true
}

const deleteLog = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除此迭代记录?', '警告', {
      type: 'warning'
    })
    await axios.delete(`${API_BASE}/iterations/${id}`)
    ElMessage.success('删除成功')
    fetchLogs()
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  fetchLogs()
})
</script>

<style scoped>
.iterations-container {
  max-width: 1200px;
  margin: 0 auto;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.filters {
  margin-bottom: 20px;
}
.actions {
  margin-top: 10px;
  text-align: right;
}
</style>
