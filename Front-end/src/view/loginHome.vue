<script setup>
import { ref } from 'vue'

import login from '@/components/login.vue'
import register from '@/components/register.vue'

const switchvalue = ref(true)
const hideTime = ref(true)
const key = ref(0)

const changeFN = () => {
    switchvalue.value = !switchvalue.value
    key.value++
    if (hideTime.value) {
        hideTime.value = false
    }
}
</script>

<template>
    <div class="layoutBox">
        <css-doodle :key="key" style="position: absolute;z-index: -1;">
            @grid: 1 / 180%;

            @content: @svg(
            viewBox: 0 0 16 16 p 1;
            stroke: #aeacfb;
            stroke-width: .1;
            stroke-linecap: round;
            line*16x16 {
            draw: @r(2s);
            x1, y1, x2, y2: @p(
            @nx(-1) @ny(-1) @nx @ny,
            @nx @ny(-1) @nx(-1) @ny,
            @nx @ny(-1) @nx @ny
            );
            }
            );
        </css-doodle>

        <div class="changeBox" @click="changeFN">
            <svg xmlns="http://www.w3.org/2000/svg" width="42px" height="42px" viewBox="0 0 24 24">
                <path fill="currentColor" d="M12.984 4.993a1 1 0 0 1 1.848-.531L17.52 7.15a1 1 0 1 1-1.415 1.414l-1.12-1.121v7.55a1 1 0 0 1-2 0zm-1.969 14.014a1 1 0 0 1-1.848.531L6.48 16.85a1 1 0 0 1 1.415-1.414l1.12 1.121v-7.55a1 1 0 0 1 2 0z"></path>
            </svg>
            <div>切换至{{ switchvalue ? "注册" : "登录"}}</div>
        </div>

        <login style="position: absolute;" class="animate__animated animate__backInDown" :class="{animate__backOutLeft: !switchvalue}"></login>
        <register @change="changeFN" v-show="!hideTime" style="position: absolute;" class="animate__animated animate__backInUp" :class="{animate__backOutRight: switchvalue}"></register>

        <div class="logo-set">
            <img class="Zlogo" src="../image/killLOGO.png">
        </div>
    </div>
</template>

<style scoped>
.layoutBox {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: black;
    overflow: hidden;
    user-select: none;

    .changeBox {
        position: absolute;
        top: 12%;
        right: 16%;
        color: rgba(0, 0, 0, 0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        background-image: linear-gradient(to right, rgb(146, 224, 159), rgb(255, 255, 255));
        padding: 2px 8px;
        border-radius: 4px;
        cursor: pointer;

        div {
            font-size: 24px;
            font-weight: 600;
        }
    }
}

.logo-set {
    pointer-events: none;
    .Zlogo {
        position: absolute;
        top: 4%;
        left: 1%;
        width: 110px;
        height: 100px;
    }
}
</style>