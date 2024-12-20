<script setup>
import { ref } from 'vue'
import { storeToRefs } from 'pinia'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAppData } from '@/store/AppData'
import { removeCookie, removeALLItem } from '@/utils/storage.js'
import router from '../router';

import quitBtn from '@/ui/quikBtn.vue'
import LogoWord from '@/components/wordlogo.vue'

const useAppDataStore = useAppData()
const AllDataLyt = storeToRefs(useAppDataStore).testData

const keycount = ref(0)

const QuitFN = () => {
  ElMessageBox.confirm(
    '您确定退出登录么?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      removeCookie('user')
      removeALLItem()
      router.push('/login')
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消本次操作',
      })
    })
}

// console.log(AllDataLyt)
const Guide1 = ref()
const Guide2 = ref()
const Guide3 = ref()
const Guide4 = ref()

const OpenTour = ref(false)
setTimeout(() => {
  OpenTour.value = AllDataLyt.value.NewUser
}, 1000)

const deActive = ref('1-1')
const handleChange = (currentStep) => {
  if (currentStep <= 2) {
    deActive.value = '1-' + (currentStep + 1)
  } 
  if (currentStep == 2) {
    setTimeout(() => {
      deActive.value = '2-' + (currentStep - 1)
    }, 500)
  }
}
</script>

<template>
  <div class="layoutBox">
    <div class="layout-bgk">
      <css-doodle :key="keycount">
        @grid: 10 / 180%;
        background: rgba(96, 86, 158, @rand(.9));
        transition: .2s ease @rand(200ms);
        transform: rotate(@rand(360deg));
        clip-path: polygon(
        @rand(100%) 0, 100% @rand(100%), 0 @rand(100%)
        );
      </css-doodle>
    </div>
    <div class="layout-left">
      <div class="layout-left-UNA">
        <el-avatar :size="120" :src="AllDataLyt.Avatar == '' ? AllDataLyt.DefaultAvatar : AllDataLyt.Avatar" />
        <div class="layout-left-UNA-name">{{ AllDataLyt.UserName }}</div>
      </div>
      <div class="collapse">
        <el-col>
          <el-menu :default-active="deActive">
            <el-sub-menu index="1">
              <template #title>
                <el-icon>
                  <House />
                </el-icon>
                <span>个人中心</span>
              </template>
              <el-menu-item ref="Guide1" index="1-1" @click="{ router.push('/home/information'); keycount++ }">
                <el-icon>
                  <Avatar />
                </el-icon>
                <span>信息修改</span>
              </el-menu-item>
              <el-menu-item ref="Guide2" index="1-2" @click="{ router.push('/home/publish'); keycount++ }">
                <el-icon>
                  <Document />
                </el-icon>
                <span>我发布的问卷</span>
              </el-menu-item>
              <el-menu-item ref="Guide3" index="1-3" @click="{ router.push('/home/collect'); keycount++ }">
                <el-icon>
                  <DataLine />
                </el-icon>
                <span>我收集的问卷</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title>
                <el-icon>
                  <Edit />
                </el-icon>
                <span>问卷设计</span>
              </template>
              <el-menu-item ref="Guide4" index="2-1" @click="{ router.push('/home/design'); keycount++ }">
                <el-icon>
                  <CollectionTag />
                </el-icon>
                <span>模板</span>
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-col>
      </div>
    </div>
    <div class="layout-right">
      <div class="layout-right-top">
        <quitBtn class="quitBtn" @click="QuitFN"></quitBtn>
        <LogoWord class="logoWord"></LogoWord>
      </div>
      <div class="layout-right-window">
        <router-view :RouterData="AllDataLyt"></router-view>
      </div>
    </div>
  </div>
  <el-tour v-model="OpenTour" @change="handleChange">
    <el-tour-step :target="Guide1?.$el" title="引导步骤 1" description="这里可以修改一些个人信息" />
    <el-tour-step :target="Guide2?.$el" title="引导步骤 2" description="这里可以查看正在发布的问卷，可以提前截止收集" />
    <el-tour-step :target="Guide3?.$el" title="引导步骤 3" description="这里可以查看已经收集的问卷，查看收集到的详情数据" />
    <el-tour-step :target="Guide4?.$el" title="引导步骤 4" description="这里是模板设计，请在这里设计您的调查问卷内容" />
  </el-tour>
</template>

<style scoped>
.layoutBox {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;

  .layout-bgk {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    /* background-image: url("../image/n5.jpg");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center; */
    z-index: -1;
    /* filter: blur(10px); */
  }

  .layout-left {
    width: 20%;
    height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;

    .layout-left-UNA {
      width: 100%;
      height: 200px;
      padding-top: 24px;

      .layout-left-UNA-name {
        margin-top: 8px;
        font-size: 28px;
      }
    }

    .collapse {
      width: 78%;
      background-color: transparent;
    }

  }

  .layout-right {
    width: 80%;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .layout-right-top {
      width: 100%;
      height: 8%;
      min-height: 72px;
      border-bottom: 1px solid rgba(0, 0, 0, 0.1);
      position: relative;

      .quitBtn {
        position: absolute;
        right: -4%;
        top: 50%;
        transform: translate(-50%, -50%);
      }

      .logoWord {
        position: absolute;
        left: 12%;
        top: 50%;
        transform: translate(-50%, -50%);
      }
    }

    .layout-right-window {
      width: 100%;
      height: 90%;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
}
</style>
