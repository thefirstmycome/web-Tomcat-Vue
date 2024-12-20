<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from "element-plus";

import { usePost } from '@/api/DataAsk.js'
import { api } from '@/utils/request.js'
import { useThrottle } from '@/hooks/throttle.js'

import confirm from '@/ui/confirm.vue';

defineProps(['RouterData'])

const personForm = ref({
  UserName: '',
  Password: ''
})

const NewAvatarUrl = ref('')
const FormDataOBJ = new FormData()
const upload = (param) => {
  FormDataOBJ.append('file', param.file)
  NewAvatarUrl.value = URL.createObjectURL(param.file)
}

const updateFN = useThrottle(() => {
  if (NewAvatarUrl.value == '' && personForm.value.UserName == '' && personForm.value.Password == '') {
    ElMessage.error('您没有提交任何有效内容')
    return
  }

  FormDataOBJ.append('UserName', personForm.value.UserName)
  FormDataOBJ.append('Password', personForm.value.Password)

  ElMessageBox.confirm(
    '您确定修改您的个人信息么?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      usePost(api.update, FormDataOBJ).then(res => {
        console.log(res)
        ElMessage.success('修改成功')
      }, rej => {
        console.log(rej)
        ElMessage.error('请求后端不成功，修改失败')
        FormDataOBJ.delete('UserName')
        FormDataOBJ.delete('Password')
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消本次操作',
      })
    })
}, 1000)
</script>

<template>
  <div class="PageInformation">
    <el-form :model="personForm" :rules="rules" label-width="120px">
      <el-form-item label="修改头像">
        <el-upload class="avatar-uploader" :http-request="upload" :show-file-list="false">
          <img v-if="NewAvatarUrl" :src="NewAvatarUrl" class="avatar" />
          <el-icon class="avatar-uploader-icon" v-else>
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item class="PageInformationItem" prop="username" label="修改昵称">
        <el-input v-model="personForm.UserName" :placeholder="RouterData.UserName" />
      </el-form-item>
      <el-form-item class="PageInformationItem" prop="account" label="修改密码">
        <el-input v-model="personForm.Password" type="password" placeholder="输入新密码" />
      </el-form-item>
    </el-form>
    <confirm class="conBtn" @click="updateFN"></confirm>
  </div>
</template>

<style scoped>
.PageInformation {
  width: 420px;
  margin-bottom: 120px;
}

.avatar-uploader {
  background-color: #00000065;
  border-radius: 8px;
  overflow: hidden;
  box-shadow:
    0 4px 6px rgba(50, 50, 93, 0.11),
    0 1px 3px rgba(0, 0, 0, 0.08);
  /* 双层阴影，增加层次感 */
}

.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.PageInformationItem {
  margin-top: 32px;
}

.conBtn {
  margin: auto;
  margin-top: 60px;
  outline: none;
}

.conBtn:focus {
  outline: none;
  border: 2px solid palegreen;
}
</style>

<style>
.el-icon.avatar-uploader-icon {
  font-size: 32px;
  color: #532fca75;
  width: 120px;
  height: 120px;
  text-align: center;
}

.el-form-item {
  align-items: center;
}

.el-form-item__label {
  font-size: 24px;
}

.el-input__wrapper {
  background-color: transparent;
}

.el-input__wrapper.is-focus {
  box-shadow:
    0 0 15px rgba(255, 193, 7, 0.6),
    0 4px 6px rgba(50, 50, 93, 0.11),
    0 1px 3px rgba(0, 0, 0, 0.08);
}
</style>
