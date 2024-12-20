<script setup>
import { onMounted, onUnmounted, ref } from 'vue'

import router from '../router'

import startBtn from '../ui/startBtn.vue';

let h1Text = '问卷调查发布与收集平台'
const HT = ref('')
let time = setInterval(() => {
    HT.value = h1Text.substring(0, HT.value.length + 1)
    if (HT.value == h1Text) {
        clearInterval(time)
    }
}, 200)

const startFN = () => {
    router.push('/login')
}
</script>

<template>
    <div class="Title-Box">
        <div class="title-bkg">
            <css-doodle>
                @grid: 1 / 100%;
                background-size: 100%;
                background-image: @doodle(
                @grid: 8 / 100vmax;
                @shape: clover 5;
                background: hsla(
                calc(100 - @i * 4), 70%, 68%, @r.8
                );
                transform:
                scale(@r(.8, 1.6))
                translate(@m2.@r(±50%));
                );
            </css-doodle>
        </div>
        <h1>{{ HT }}</h1>
    </div>
    <div class="content-box">
        <div class="content-bkg">
            <div class="left"></div>
            <div class="right"></div>
        </div>
        <div class="person">
            <div class="linear"></div>
            <div class="group">
                <div>项目组号：第八组</div>
            </div>
            <div class="member">
                <div>项目成员：张小恒 严乐成 周圆智</div>
            </div>
        </div>
        <div class="intro">
            <p>项目简介：<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用户（已完成注册）可以自行设计所需要调查的信息，也可以选择现有模板，问卷的问题以及选项或是开放性答案都可，完成设计后即可获得问卷的URL，分享此URL（无需注册），收集你的回答，在问卷的截止时间到达后，即可查看收集的回答数据。
            </p>
            <p>主要功能：1.登录注册 2.表单设计 3.数据收集</p>
        </div>
        <div class="name">web安全程序设计大作业</div>
        <div class="HideToShow"></div>
    </div>
    <div class="start-box">
        <startBtn @click="startFN"></startBtn>
    </div>
    <div class="logo-set">
        <img class="Zlogo" src="../image/killLOGO.png">
    </div>
</template>

<style scoped>
.Title-Box {
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 70%;
    color: #fff;
    user-select: none;

    .title-bkg {
        position: absolute;
        background-color: blueviolet;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: -10;
    }
}

.content-box {
    position: fixed;
    top: 30%;
    left: 0;
    right: 0;
    bottom: 0;
    overflow: hidden;
    min-height: 600px;
    min-width: 1200px;

    .content-bkg {
        position: absolute;
        width: 100%;
        height: 100%;
        display: flex;
        background-image: url('../image/n3.jpg');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        opacity: 0.5;

        .left {
            width: 50%;
            height: 100%;
            background-image: linear-gradient(to right, blueviolet, transparent);
        }

        .right {
            width: 50%;
            height: 100%;
            background-image: linear-gradient(to left, blueviolet, transparent);
        }
    }

    .person {
        position: absolute;
        bottom: 2%;
        left: 18%;
        transform: translate(-50%);
        color: #000;
        font-size: 40px;
        font-weight: 600;
        user-select: none;

        .linear {
            position: absolute;
            bottom: 130%;
            left: 32%;
            width: 4px;
            height: 420px;
            background: linear-gradient(to bottom, #FF4136, #FF851B, #FFDC00);
        }

        .group {
            position: relative;
            padding: 0 24px;
            border-bottom: 5px solid #5732de;
        }

        .member {
            position: relative;
        }
    }

    .name {
        position: absolute;
        color: #000;
        font-size: 40px;
        font-weight: 600;
        user-select: none;
        right: 2%;
        bottom: 2%;
    }

    .intro {
        position: absolute;
        width: 70%;
        top: 42%;
        left: 50%;
        color: #000;
        font-size: 40px;
        font-weight: 600;
        user-select: none;
        transform: translate(-50%, -50%);
        text-align: left;
    }

    .HideToShow {
        position: absolute;
        bottom: 17%;
        right: 6%;
        width: 100px;
        height: 100px;
        border-radius: 50%;
        background-color: #000;
        opacity: 0.9;
        transform: scale(42);
        animation: HTS 1.8s linear 1s forwards;
    }
}

@keyframes HTS {
    to {
        opacity: 0;
        transform: scale(1);
    }
}

.start-box {
    position: fixed;
    bottom: 12%;
    right: 6%;
}

.logo-set {
    pointer-events: none;
    .Zlogo {
        position: absolute;
        top: 42%;
        left: 2%;
        width: 110px;
        height: 100px;
    }
}
</style>
