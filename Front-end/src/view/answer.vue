<script setup>
import { ref } from 'vue'
import router from '../router';

import { usePost } from '@/api/DataAsk.js'
import { api } from '@/utils/request.js'

import upsubmit from '@/ui/upsubmit.vue';
import { ElMessage } from 'element-plus';

const data = ref({})
const Profile = ref({})
const NeedFrom = router.currentRoute.value.query
let index = 0
usePost(api.GetServerMySQL, { Account: NeedFrom.Account }).then(res => {
    Profile.value = JSON.parse(res.data.Profile)
    let GetDataNeed = Profile.value.PaperPublish
    index = GetDataNeed.indexOf(GetDataNeed.filter((item) => item.SurveyID == NeedFrom.SurveyID)[0]);
    data.value = GetDataNeed[index]
}, rej => {
    console.log(rej)
})

const key = ref(0)

const SwaperDOM = ref(null)
const ActivePage = ref(0)
const PrevFN = () => {
    ActivePage.value--
    if (ActivePage.value < 0) {
        ActivePage.value = data.value.SurveyData.length - 1
    }
    SwaperDOM.value.style.left = `${-ActivePage.value * 100}vw`
    key.value++
}
const NextFN = () => {
    ActivePage.value++
    if (ActivePage.value == data.value.SurveyData.length) {
        ActivePage.value = 0
    }
    SwaperDOM.value.style.left = `${-ActivePage.value * 100}vw`
    key.value++
}

const UpFN = () => {
    let book = true
    Profile.value.PaperPublish[index].SurveyData.forEach(item => {
        if (item.Required == true && (item.Answer == 0 || item.Answers == [] || item.Answers == '')) {
            ElMessage({
                type: 'warning',
                message: '请完成所有必填项',
                grouping: true,
            })
            book = false
        }
    });
    if (!book) {
        return
    }
    for (let i = 0; i < Profile.value.PaperPublish[index].SurveyData.length; i++) {
        if (!Profile.value.PaperPublish[index].SurveyData[i].CollectAnswers) {
            Profile.value.PaperPublish[index].SurveyData[i].CollectAnswers = [];
        }

        if (data.value.SurveyData[i].Answers != '') {
            if (data.value.SurveyData[i].Type == 'sigle') {
                Profile.value.PaperPublish[index].SurveyData[i].CollectAnswers.push(data.value.SurveyData[i].Answer)
            } else {
                Profile.value.PaperPublish[index].SurveyData[i].CollectAnswers.push(data.value.SurveyData[i].Answers)
            }
        }
    }
    
    const PostData = {
        Account: NeedFrom.Account,
        Profile: JSON.stringify({
            PaperDesign: Profile.value.PaperDesign,
            PaperPublish: Profile.value.PaperPublish,
            PaperCollect: Profile.value.PaperCollect,
        })
    }
    usePost(api.UpdateServerMySQL, PostData).then(res => {
        ElMessage({
            type: 'success',
            message: '问卷提交成功，感谢您的参与！',
        })
    }, rej => {
        ElMessage({
            type: 'error',
            message: '提交失败，请稍后再尝试！',
        })
    })
}
</script>

<template>
    <div class="PageAnswer">
        <div class="BK" :key="key">
            <css-doodle>
                :doodle {
                @grid: 10 / 100%;
                @shape: circle;
                }

                transition: .2s @r(.6s);
                border-radius: @pick(100% 0, 0 100%);
                transform: scale(@r(.25, 1.25));

                background: hsla(
                calc(240 - 6 * @x * @y),
                70%, 68%, @r.8
                );
            </css-doodle>
        </div>
        <div class="Title">
            <h1>{{ data.SurveyName }}</h1>
        </div>
        <div class="Content">
            <div class="Swaper" ref="SwaperDOM">
                <div class="Content-Item" v-for="(item, index) in data.SurveyData" :key="index">
                    <div class="Detail" v-if="item.Type === 'sigle'">
                        <div class="head">
                            <div class="Detail-Question">{{ item.QuestionID + '. ' + item.Question }}</div>
                            <div class="Detail-Description">类型：单选 {{ item.Required ? '必填' : '选填' }}</div>
                        </div>
                        <div class="Detail-Options">
                            <el-radio-group v-model="item.Answer">
                                <div>{{ item.Answer }}</div>
                                <el-radio v-for="Entity in item.Options" :label="Entity.Option">{{ Entity.Option
                                    }}</el-radio>
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
            </div>
        </div>
        <div class="ButtonBox">
            <div class="LeftButton">
                <div class="PNButton" @click="PrevFN">
                    < Prev</div>
                        <div class="PNButton" @click="NextFN">Next ></div>
                </div>
                <div class="RightButton">
                    <upsubmit @click="UpFN"></upsubmit>
                </div>
            </div>
        </div>
</template>

<style scoped>
.PageAnswer {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;

    .Title {
        width: 100%;
        height: 24%;
        display: flex;
        justify-content: center;
        align-items: center;
        border-bottom: 8px dotted #6642c2;
    }

    .Content {
        width: 100%;
        height: 62%;
        border-bottom: 8px dotted #6642c2;

        .Swaper {
            display: flex;
            align-items: center;
            position: relative;
            left: 0vw;
            transition: ease-in-out 0.3s;

            width: 100vw;
            height: 100%;

            .Content-Item {
                width: 100vw;
                padding: 0 20vw;

                .Detail {
                    height: 40vh;
                    border-radius: 12px;
                    background: linear-gradient(to right, #885cd398 0%, #d456cc98 50%, #885cd398 100%);
                    box-shadow:
                        0px -5px 15px rgba(200, 200, 250, 0.5),
                        0px 5px 15px rgba(200, 200, 250, 0.5),
                        -5px 0px 15px rgba(200, 200, 250, 0.5),
                        5px 0px 15px rgba(200, 200, 250, 0.5);
                    box-sizing: border-box;
                    padding: 20px 80px;
                    width: 60vw;
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
                        margin-bottom: 42px;

                        .Detail-Question {
                            width: 85%;
                            text-align: start;
                            font-size: 24px;
                            font-weight: 400;
                            margin-bottom: 8px;
                        }
                    }
                }
            }
        }

    }

    .ButtonBox {
        width: 100%;
        height: 14%;
        display: flex;
        justify-content: space-around;
        align-items: center;

        .LeftButton {
            display: flex;
            width: 16%;
            justify-content: space-between;
            font-size: 24px;
            user-select: none;
            cursor: pointer;

            .PNButton {
                color: #fff;
                background-color: #393940;
                width: 100px;
                line-height: 50px;
                padding: 0 6px;
                border-radius: 8px;
            }
        }

        .RightButton {
            margin-left: 42%;
        }
    }

    .BK {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        display: flex;
        justify-content: center;
        align-items: center;

        css-doodle {
            width: 800px;
            height: 800px;
            transform: scale(2.4);
        }

        z-index: -1;
    }
}
</style>
