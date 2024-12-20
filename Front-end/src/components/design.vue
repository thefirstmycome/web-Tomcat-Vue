<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from "element-plus";
import { storeToRefs } from 'pinia'

import { Delete, View, Position } from '@element-plus/icons-vue'

import { useAppData } from '@/store/AppData'
import { usePost } from '@/api/DataAsk.js'
import { api } from '@/utils/request.js'
import { useThrottle } from '@/hooks/throttle.js'
import { UpdateMySQL } from '@/utils/updateSQL.js'

import closeBtn from '@/ui/closeBtn.vue';
import newBtn from '@/ui/newBtn.vue';
import MainCore from '@/components/MainCore.vue';
import loading from '@/ui/loading.vue'

const useAppDataStore = useAppData()
const AllDataLyt = storeToRefs(useAppDataStore).testData

const PaperData = AllDataLyt.value.PaperDesign


const ShowPaperDetail = ref(false)
const ShowDetailID = ref(0)
const ShowDetailFN = (ID) => {
  let index = AllDataLyt.value.PaperDesign.indexOf(AllDataLyt.value.PaperDesign.filter((item) => item.SurveyID == ID)[0]);
  ShowDetailID.value = index
  ShowPaperDetail.value = true
}

const SendToPublishFN = (ID) => {
  if (ID == 1) {
    ElMessage({
      type: 'error',
      message: '该问卷为系统默认样式案例问卷，不可发布！',
    })
    return
  }
  let book = false
  AllDataLyt.value.PaperPublish.forEach((item, i) => {
    if (item.SurveyID == ID) {
      ElMessage({
        type: 'error',
        message: '问卷已经处于发布状态！',
      })
      book = true
    }
  });
  if (book) {
    return
  }
  ElMessageBox.prompt('请输入问卷的截止时间：形如2024/02/12 23:02:00', '提示', {
    confirmButtonText: '发布',
    cancelButtonText: '取消',
    inputPattern:
      /^\d{4}\/\d{2}\/\d{2} \d{2}:\d{2}:\d{2}$/,
    inputErrorMessage: '请按照格式输入截止时间',
  })
    .then(({ value }) => {
      ShowLoading.value = true
      let index = AllDataLyt.value.PaperDesign.indexOf(AllDataLyt.value.PaperDesign.filter((item) => item.SurveyID == ID)[0]);
      PaperData[index].SurveyPublishTime = new Date().toLocaleString()
      PaperData[index].SurveyStopCTime = value
      AllDataLyt.value.PaperPublish.push(PaperData[index])

      const PostData = {
        Account: AllDataLyt.value.Account,
        Profile: JSON.stringify({
          PaperDesign: AllDataLyt.value.PaperDesign,
          PaperPublish: AllDataLyt.value.PaperPublish,
          PaperCollect: AllDataLyt.value.PaperCollect,
        })
      }
      usePost(api.UpdateServerMySQL, PostData).then(res => {
        ShowLoading.value = false
        ElMessage({
          type: 'success',
          message: '问卷发布成功',
        })
      }, rej => {
        ShowLoading.value = false
        ElMessage({
          type: 'error',
          message: '问卷发布失败，请稍后再尝试！',
        })
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消本次操作或输入格式错误',
      })
    })
}

const DeletePaperFN = (ID) => {
  ElMessageBox.confirm(
    '您确定删除此问卷么?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info',
    }
  )
    .then(() => {
      let index = AllDataLyt.value.PaperDesign.indexOf(AllDataLyt.value.PaperDesign.filter((item) => item.SurveyID == ID)[0]);
      AllDataLyt.value.PaperDesign.splice(index, 1)
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
          message: '删除模板成功！',
        })
      }, rej => {
        ElMessage({
          type: 'error',
          message: '删除模板失败，请稍后再尝试！',
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

const ShowMainCore = ref(false)
const ShowMainCoreFN = () => {
  ShowMainCore.value = true
}

const CloseShowMainCoreFN = () => {
  ShowMainCore.value = false
}

const ShowLoading = ref(false)
</script>

<template>
  <div class="PageDesign">
    <div class="Navigation" v-if="!ShowPaperDetail">
      <div class="Navigation-list" v-for="(item, index) in PaperData" :key="index">
        <div>
          <span>问卷名称：</span>
          <span>{{ item.SurveyName }}</span>
          <span style="display: inline-block;width: 24px;"></span>
          <span>日期：</span>
          <span>{{ item.SurveyCreateTime }}</span>
        </div>
        <div>
          <el-tooltip class="box-item" effect="dark" content="发布问卷" placement="bottom">
            <el-button @click="SendToPublishFN(item.SurveyID)" type="warning" :icon="Position" circle />
          </el-tooltip>
          <span style="display: inline-block;width: 24px;"></span>
          <el-tooltip class="box-item" effect="dark" content="查看问卷" placement="bottom">
            <el-button @click="ShowDetailFN(item.SurveyID)" type="success" :icon="View" circle />
          </el-tooltip>
          <span style="display: inline-block;width: 24px;"></span>
          <el-tooltip class="box-item" effect="dark" content="删除问卷" placement="bottom">
            <el-button @click="DeletePaperFN(item.SurveyID)" type="danger" :disabled="item.SurveyID == 1" :icon="Delete"
              circle />
          </el-tooltip>
        </div>
      </div>
      <newBtn class="NewPaper" @click="ShowMainCoreFN"></newBtn>
    </div>
    <div class="PaperContent" v-else>
      <div class="Content-Title">{{ PaperData[ShowDetailID].SurveyName }}</div>
      <div class="Content-Detail" v-for="(item, index) in PaperData[ShowDetailID].SurveyData" :key="index">
        <div class="Detail" v-if="item.Type === 'sigle'">
          <div class="head">
            <div class="Detail-Question">{{ item.QuestionID + '. ' + item.Question }}</div>
            <div class="Detail-Description">类型：单选 {{ item.Required ? '必填' : '选填' }}</div>
          </div>
          <div class="Detail-Options">
            <el-radio-group v-model="item.Answer">
              <div>{{ item.Answer }}</div>
              <el-radio v-for="Entity in item.Options" :label="Entity.Value">{{ Entity.Option }}</el-radio>
            </el-radio-group>
          </div>
        </div>
        <div class="Detail" v-if="item.Type === 'multiple'">
          <div class="head">
            <div class="Detail-Question">{{ item.QuestionID + '. ' + item.Question }}</div>
            <div class="Detail-Description">类型：多选 {{ item.Required ? '必填' : '选填' }}</div>
          </div>
          <div class="Detail-Options">
            <el-checkbox-group v-model="item.Answers">
              <el-checkbox v-for="Entity in item.Options" :label="Entity.Option" />
            </el-checkbox-group>
          </div>
        </div>
        <div class="Detail" v-if="item.Type === 'text'">
          <div class="head">
            <div class="Detail-Question">{{ item.QuestionID + '. ' + item.Question }}</div>
            <div class="Detail-Description">类型：文本 {{ item.Required ? '必填' : '选填' }}</div>
          </div>
          <el-input v-model="item.Answers" class="TxetArea" type="textarea" rows="4" resize="none"
            placeholder="请输入内容" />
        </div>
      </div>
      <closeBtn class="BackToUp" @click="ShowPaperDetail = false"></closeBtn>
    </div>
  </div>
  <MainCore @change="CloseShowMainCoreFN" class="MainCore" v-show="ShowMainCore"></MainCore>
  <div v-show="ShowLoading" class="Loading">
    <loading></loading>
  </div>
</template>

<style scoped>
.Loading {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.75);
  display: flex;
  justify-content: center;
  align-items: center;
}

.PageDesign {
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

    .NewPaper {
      position: fixed;
      bottom: 2%;
      right: 2%;
    }

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

  .PaperContent {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;

    .BackToUp {
      position: fixed;
      right: 2%;
      opacity: 0.85;
    }

    .Content-Title {
      width: 90%;
      line-height: 60px;
      font-size: 38px;
      font-weight: 600;
      margin-bottom: 24px;
    }

    .Content-Detail {
      width: 90%;
      background: linear-gradient(to right, #885cd398 0%, transparent 50%, #885cd398 100%);
      box-shadow:
        0px -5px 15px rgba(200, 200, 250, 0.5),
        0px 5px 15px rgba(200, 200, 250, 0.5),
        5px 0px 15px rgba(200, 200, 250, 0.5),
        -5px 0px 15px rgba(200, 200, 250, 0.5);
      padding: 8px 16px;
      border-radius: 8px;
      margin-bottom: 24px;

      .Detail {
        width: 100%;
        margin-bottom: 24px;
        display: flex;
        flex-direction: column;
        align-items: flex-start;


        .head {
          width: 100%;
          height: 52px;
          display: flex;
          justify-content: space-between;
          align-items: center;

          .Detail-Question {
            width: 85%;
            text-align: start;
            font-size: 24px;
            font-weight: 400;
            margin-bottom: 8px;
          }
        }

        .TxetArea {
          width: 95%;
          font-size: 18px;
        }

        .Detail-Options {
          margin-left: 18px;
        }
      }
    }
  }
}

.MainCore {
  position: fixed;
  top: 4%;
  bottom: 4%;
  left: 2%;
  right: 2%;
  display: flex;
  justify-content: center;
  border-radius: 12px;
}
</style>

<style>
.el-textarea__inner {
  background-color: transparent;
  color: #000;
}

.el-textarea__inner::placeholder {
  color: #fff;
}

.tooltip-base-box {
  width: 600px;
}

.tooltip-base-box .row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.tooltip-base-box .center {
  justify-content: center;
}

.tooltip-base-box .box-item {
  width: 110px;
  margin-top: 10px;
}
</style>
