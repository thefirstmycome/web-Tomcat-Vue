<script setup>
import { ref } from 'vue'
import { storeToRefs } from 'pinia'
import { ElMessage, ElMessageBox } from "element-plus";

import { usePost } from '@/api/DataAsk.js'
import { api } from '@/utils/request.js'

import { useAppData } from '@/store/AppData'


const useAppDataStore = useAppData()
const AllDataLyt = storeToRefs(useAppDataStore).testData

const PaperData = AllDataLyt.value.PaperPublish

const StopToCollectFN = (ID) => {
  ElMessageBox.confirm(
    '请确定是否提前停止此问卷的收集?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info',
    }
  )
    .then(() => {
      let index = AllDataLyt.value.PaperPublish.indexOf(AllDataLyt.value.PaperPublish.filter((item) => item.SurveyID == ID)[0]);

      const newData = JSON.parse(JSON.stringify(AllDataLyt.value.PaperPublish[index]));
      AllDataLyt.value.PaperCollect.push(newData);
      AllDataLyt.value.PaperPublish.splice(index, 1)

      const PostData = {
        Account: AllDataLyt.value.Account,
        Profile: JSON.stringify({
          PaperDesign: AllDataLyt.value.PaperDesign,
          PaperPublish: AllDataLyt.value.PaperPublish,
          PaperCollect: AllDataLyt.value.PaperCollect,
        })
      }
      usePost(api.UpdateServerMySQL, PostData).then(res => {
        ElMessage({
          type: 'success',
          message: '问卷提前收集成功',
        })
      }, rej => {
        ElMessage({
          type: 'error',
          message: '操作失败，请稍后再尝试！',
        })
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消本次操作',
      })
    })
}

</script>

<template>
  <div class="PaperPublish" v-if="PaperData.length > 0">
    <div class="Navigation">
      <div class="Navigation-list" v-for="(item, index) in PaperData" :key="index">
        <div>
          <span>问卷名称：</span>
          <span>{{ item.SurveyName }}</span>
          <span style="display: inline-block;width: 24px;"></span>
          <span>截止时间：</span>
          <span>{{ item.SurveyStopCTime }}</span>
        </div>
        <div>
          <span>URL: </span>
          <a :href="'/answer?Account=' + AllDataLyt.Account + '&SurveyID=' + item.SurveyID">{{ '/answer?Account=' +
            AllDataLyt.Account + '&SurveyID=' + item.SurveyID }}</a>
        </div>
        <div>
          <span style="color: green;">状态：正在收集</span>
          <span style="display: inline-block;width: 24px;"></span>
          <el-button @click="StopToCollectFN(item.SurveyID)" type="danger" plain>
            停止收集
          </el-button>
        </div>
      </div>
    </div>
  </div>
  <el-empty style="opacity: 0.7;" v-else :image-size="400" description="您没有发布的问卷" />
</template>

<style scoped>
.PaperPublish {
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