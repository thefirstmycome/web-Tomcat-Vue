<script setup>
import { ref } from 'vue'
import { ElMessage } from "element-plus";

import router from '../router';
import { useAppData } from '@/store/AppData'
import { usePost } from '@/api/DataAsk.js'
import { api } from '@/utils/request.js'
import { useThrottle } from '@/hooks/throttle.js'
import { setCookie } from '@/utils/storage.js'
import { setSession } from '@/utils/storage.js'

const useAppDataStore = useAppData()

import loading from '@/ui/loading.vue'

const loginData = ref({
    Account: '',
    Password: ''
})

const rememberME = ref(false)

const ShowLoading = ref(false)
const loginFN = useThrottle(() => {
    let regAccount = /^[^\u4e00-\u9fa5 ]{6,20}$/
    let regPassword = /^@[a-zA-Z0-9]{7,15}$/
    if (loginData.value.Account == '' || loginData.value.Password == '') {
        ElMessage.error('账号或密码不能为空')
        return
    }
    if (!regAccount.test(loginData.value.Account)) {
        ElMessage.error('账号格式不正确')
        return
    }
    if (!regPassword.test(loginData.value.Password)) {
        ElMessage.error('密码格式不正确')
        return
    }

    ShowLoading.value = true
    usePost(api.login, loginData.value).then(res => {
        const resp = res.data
        if (resp.code == 0) {
            // 设置Cookie
            if (rememberME.value) {
                setCookie('user', loginData.value.Account, 1000 * 60 * 60 * 24 * 1)
            }
            // 拉取用户信息
            usePost(api.GetServerMySQL, { Account: loginData.value.Account }).then(res => {
                const resp = res.data
                useAppDataStore.saveData({
                    UserName: resp.UserName,
                    NewUser: resp.NewUser,
                    Avatar: resp.DefaultAvatar,
                    Account: resp.Account,
                    DefaultAvatar: resp.DefaultAvatar,
                    TryCount: resp.TryCount,
                    Disabled: resp.Disabled,
                    PaperPublish: JSON.parse(resp.Profile).PaperPublish,
                    PaperCollect: JSON.parse(resp.Profile).PaperCollect,
                    PaperDesign: JSON.parse(resp.Profile).PaperDesign
                })

                if (resp.NewUser){
                    usePost(api.changeStutes, { Account: loginData.value.Account }).then(res => {
                        
                    }, rej => {
                        
                    })
                }
                setTimeout(() => {
                    ElMessage.success('拉取用户信息成功')
                    ShowLoading.value = false
                    setSession('__user__', resp.Account)
                    router.push('/home')
                }, 3000)
            }, rej => {
                ElMessage.error('拉取用户信息失败')
                ShowLoading.value = false
            })
        } else {
            setTimeout(() => {
                ElMessage.error(resp.msg)
                ShowLoading.value = false
            }, 3000)
        }
    }, rej => {
        console.log(rej)
        setTimeout(() => {
            ElMessage.error('请求失败，请稍后再试')
            ShowLoading.value = false
        }, 3000)
    })
}, 1000)

</script>

<template>
    <loading v-if="ShowLoading"></loading>
    <div v-else class="content">
        <div class="text">
            Login
        </div>
        <div>
            <div class="field">
                <input v-model="loginData.Account" type="text" class="input" placeholder="Account">
                <span class="span"><svg class="" xml:space="preserve" style="enable-background:new 0 0 512 512"
                        viewBox="0 0 512 512" y="0" x="0" height="20" width="50"
                        xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" xmlns="http://www.w3.org/2000/svg">
                        <g>
                            <path class="" data-original="#000000" fill="#595959"
                                d="M256 0c-74.439 0-135 60.561-135 135s60.561 135 135 135 135-60.561 135-135S330.439 0 256 0zM423.966 358.195C387.006 320.667 338.009 300 286 300h-60c-52.008 0-101.006 20.667-137.966 58.195C51.255 395.539 31 444.833 31 497c0 8.284 6.716 15 15 15h420c8.284 0 15-6.716 15-15 0-52.167-20.255-101.461-57.034-138.805z">
                            </path>
                        </g>
                    </svg>
                </span>
            </div>
            <div class="field">
                <input v-model="loginData.Password" type="password" class="input" placeholder="Password">
                <span class="span"><svg class="" xml:space="preserve" style="enable-background:new 0 0 512 512"
                        viewBox="0 0 512 512" y="0" x="0" height="20" width="50"
                        xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" xmlns="http://www.w3.org/2000/svg">
                        <g>
                            <path class="" data-original="#000000" fill="#595959"
                                d="M336 192h-16v-64C320 57.406 262.594 0 192 0S64 57.406 64 128v64H48c-26.453 0-48 21.523-48 48v224c0 26.477 21.547 48 48 48h288c26.453 0 48-21.523 48-48V240c0-26.477-21.547-48-48-48zm-229.332-64c0-47.063 38.27-85.332 85.332-85.332s85.332 38.27 85.332 85.332v64H106.668zm0 0">
                            </path>
                        </g>
                    </svg>
                </span>
            </div>
            <div class="forgot-pass">
                <input type="checkbox" value="true" v-model="rememberME">
                <span>记住密码(24小时内免密登录)</span>
            </div>
            <button class="button" @click="loginFN">登录</button>
            <div class="sign-up">
                Not a member?
                <span style="pointer-events: none;">Look at your top-right</span>
            </div>
        </div>
    </div>
</template>

<style scoped>
.content {
    width: 340px;
    padding: 40px 120px;
    background: #dde1e7;
    border-radius: 10px;
    user-select: none;
    box-shadow: -3px -3px 7px #ffffff73,
        2px 2px 5px rgba(94, 104, 121, 0.288);
}

.content .text {
    font-size: 33px;
    font-weight: 600;
    margin-bottom: 35px;
    color: #595959;
}

.field {
    height: 50px;
    width: 100%;
    display: flex;
    position: relative;
}

.field:nth-child(2) {
    margin-top: 20px;
}

.field .input {
    height: 100%;
    width: 100%;
    padding-left: 45px;
    outline: none;
    border: none;
    font-size: 18px;
    background: #dde1e7;
    color: #595959;
    border-radius: 25px;
    box-shadow: inset 2px 2px 5px #BABECC,
        inset -5px -5px 10px #ffffff73;
}

.field .input:focus {
    box-shadow: inset 1px 1px 2px #BABECC,
        inset -1px -1px 2px #ffffff73;
}

.field .span {
    position: absolute;
    color: #595959;
    width: 50px;
    line-height: 55px;
}

.field .label {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    left: 45px;
    pointer-events: none;
    color: #666666;
}

.field .input:valid~label {
    opacity: 0;
}

.forgot-pass {
    text-align: left;
    margin: 10px 0 10px 5px;
}

.forgot-pass a {
    font-size: 16px;
    color: #666666;
    text-decoration: none;
}

.forgot-pass:hover a {
    text-decoration: underline;
}

.button {
    margin: 15px 0;
    width: 100%;
    height: 62px;
    font-size: 18px;
    line-height: 50px;
    font-weight: 600;
    background: #dde1e7;
    border-radius: 25px;
    border: none;
    outline: none;
    cursor: pointer;
    color: #595959;
    box-shadow: 2px 2px 5px #BABECC,
        -5px -5px 10px #ffffff73;
}

.button:hover {
    color: #3498db;
    box-shadow: inset 2px 2px 5px #BABECC,
        inset -5px -5px 10px #ffffff73;
}

.sign-up {
    margin: 10px 0;
    color: #595959;
    font-size: 16px;
}

.sign-up a {
    color: #3498db;
    text-decoration: none;
}

.sign-up a:hover {
    text-decoration: underline;
}
</style>
