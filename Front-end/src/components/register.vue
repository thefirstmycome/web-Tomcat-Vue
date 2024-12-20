<script setup>
import { ref } from 'vue'
import { ElMessage } from "element-plus";

import { usePost } from '@/api/DataAsk.js'
import { api } from '@/utils/request.js'
import { useThrottle } from '@/hooks/throttle.js'

const Emits = defineEmits(['change'])

const registerData = ref({
    UserName: '',
    Account: '',
    Password: '',
    ConfirmPassword: ''
})

const registerFN = useThrottle(() => {
    if (registerData.value.UserName == '') {
        ElMessage.error('请输入用户名')
        return
    }
    if (registerData.value.Account == '') {
        ElMessage.error('请输入账号')
        return
    }
    if (registerData.value.Password == '') {
        ElMessage.error('请输入密码')
        return
    }
    if (registerData.value.ConfirmPassword == '') {
        ElMessage.error('请输入确认密码')
        return
    }
    if (registerData.value.Password != registerData.value.ConfirmPassword) {
        ElMessage.error('两次密码不一致')
        return
    }

    let regAccount = /^[^\u4e00-\u9fa5 ]{6,20}$/
    let regPassword = /^@[a-zA-Z0-9]{7,15}$/
    if (!regAccount.test(registerData.value.Account)) {
        ElMessage.error('账号格式不正确')
        return
    }
    if (!regPassword.test(registerData.value.Password)) {
        ElMessage.error('密码格式不正确')
        return
    }

    usePost(api.register, registerData.value).then(res => {
        if (res.data.code == 0) {
            ElMessage.success(res.data.msg)
            registerData.value = {
                UserName: '',
                Account: '',
                Password: '',
                ConfirmPassword: ''
            }
            Emits('change')
        } else {
            ElMessage.error(res.data.msg)
        }
    }, rej => {
        console.log(rej)
        ElMessage.error('请求失败，请稍后重试')
    })
}, 1000)


</script>

<template>
    <div class="form">
        <p class="title">Register </p>
        <p class="message">Signup now and get full access to our app. </p>
        <label>
            <input v-model="registerData.UserName" class="input" type="text" placeholder="10位以内字符">
            <span>UserName</span>
        </label>

        <label>
            <input v-model="registerData.Account" class="input" type="text" placeholder="6-20位以内字符,不能使用中文字符及空格">
            <span>Account</span>
        </label>

        <label>
            <input v-model="registerData.Password" class="input" type="password" placeholder="7-15位以内字符,以@符号开头,后跟数字或字母">
            <span>Password</span>
        </label>
        <label>
            <input v-model="registerData.ConfirmPassword" class="input" type="password" placeholder="如上">
            <span>Confirm password</span>
        </label>
        <button class="submit" @click="registerFN">注册</button>
    </div>
</template>

<style scoped>
.form {
    display: flex;
    flex-direction: column;
    gap: 10px;
    max-width: 420px;
    padding: 20px 120px;
    padding-bottom: 48px;
    border-radius: 20px;
    position: relative;
    background-color: rgba(255, 255, 255, 0.98);
    color: #000;
    border: 1px solid #333;
}

.title {
    font-size: 28px;
    font-weight: 600;
    letter-spacing: -1px;
    position: relative;
    display: flex;
    align-items: center;
    padding-left: 30px;
    color: #00bfff;
}

.title::before {
    width: 18px;
    height: 18px;
}

.title::after {
    width: 18px;
    height: 18px;
    animation: pulse 1s linear infinite;
}

.title::before,
.title::after {
    position: absolute;
    content: "";
    height: 16px;
    width: 16px;
    border-radius: 50%;
    left: 0px;
    background-color: #00bfff;
}

.message {
    font-size: 18px;
    color: rgba(0, 0, 0, 0.7);
}

.form label {
    position: relative;
}

.form label .input {
    background-color: rgba(255, 255, 255, 0.8);
    color: #000;
    width: 95%;
    padding: 20px 05px 05px 10px;
    outline: 0;
    border: 1px solid rgba(105, 105, 105, 0.397);
    border-radius: 10px;
    text-align: center;
}

.form label .input+span {
    color: rgba(0, 0, 0, 0.5);
    position: absolute;
    left: 10px;
    top: 0px;
    font-size: 0.9em;
    cursor: text;
    transition: 0.3s ease;
}

.form label .input:placeholder-shown+span {
    top: 12.5px;
    font-size: 0.9em;
}

.form label .input:focus+span,
.form label .input:valid+span {
    color: #00bfff;
    top: 0px;
    font-size: 0.7em;
    font-weight: 600;
}

.input {
    font-size: medium;
}

.submit {
    border: none;
    outline: none;
    padding: 10px;
    border-radius: 10px;
    color: #fff;
    font-size: 16px;
    transform: .3s ease;
    background-color: #00bfff;
}

.submit:hover {
    background-color: #00bfff96;
}

@keyframes pulse {
    from {
        transform: scale(0.9);
        opacity: 1;
    }

    to {
        transform: scale(1.8);
        opacity: 0;
    }
}
</style>
