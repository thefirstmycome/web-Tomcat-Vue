<script setup>
import { ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useAppData } from '@/store/AppData'
import { ElMessage } from 'element-plus';


const useAppDataStore = useAppData()
const AllDataLyt = storeToRefs(useAppDataStore).testData

const ToDetailFN = (ID) => {
  ElMessage.info('前往数据分析界面')
  window.open(`http://localhost:5173/analysis?Account=${AllDataLyt.value.Account}&SurveyID=${ID}`, '_blank')
}
</script>

<template>
  <div class="PaperCollect" v-if="AllDataLyt.PaperCollect.length > 0">
    <div class="Navigation">
      <div class="Navigation-list" v-for="(item, index) in AllDataLyt.PaperCollect" :key="index">
        <div>
          <span>问卷名称：</span>
          <span>{{ item.SurveyName }} | </span>
          <span>发布时间：</span>
          <span>{{ item.SurveyPublishTime }}</span>
          <span style="display: inline-block;width: 12px;"></span>
          <span>截止时间：</span>
          <span>{{ item.SurveyStopCTime }}</span>
        </div>
        <div>
          <span style="color: aquamarine;">状态：已完成收集</span>
          <span style="display: inline-block;width: 24px;"></span>
          <el-button type="success" plain @click="ToDetailFN(item.SurveyID)" >
            查看数据
          </el-button>
        </div>
      </div>
    </div>
  </div>
  <el-empty style="opacity: 0.7;" v-else :image-size="400" description="您还没有收集的问卷" />
</template>

<style scoped>
.PaperCollect {
  width: 95%;
  height: 95%;
  margin-right: 42px;
  margin-bottom: 12px;

  overflow-x: hidden;
  overflow-y: auto;

  display: flex;
  flex-direction: column;

  .Navigation {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;

    .Navigation-list {
      width: 90%;
      height: 60px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-sizing: border-box;
      padding: 0 24px;
      border-radius: 8px;
      margin: 8px 0;

      background: linear-gradient(to right, #885cd398 0%, transparent 50%, #885cd398 100%);
      box-shadow:
        0px -5px 15px rgba(200, 200, 250, 0.5),
        0px 5px 15px rgba(200, 200, 250, 0.5),
        5px 0px 15px rgba(200, 200, 250, 0.5),
        -5px 0px 15px rgba(200, 200, 250, 0.5);
    }
  }
}

:deep(.el-empty__description) p {
  color: #000;
  font-size: 24px;
}
</style>
