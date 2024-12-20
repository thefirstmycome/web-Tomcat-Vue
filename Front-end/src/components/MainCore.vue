<script setup>
import { ref } from 'vue'
import { storeToRefs } from 'pinia'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAppData } from '@/store/AppData'

import { usePost } from '@/api/DataAsk.js'
import { api } from '@/utils/request.js'

import quitnewBtn from '@/ui/quitnewBtn.vue';
import submitBtn from '@/ui/submitBtn.vue';

const Emits = defineEmits(['change']);

const useAppDataStore = useAppData()
const AllDataLyt = storeToRefs(useAppDataStore).testData

const DataTemp = ref({
    SurveyID: null,
    SurveyName: '',
    SurveyTime: '',
    SurveyData: [
        {
            Type: 'sigle',
            Question: '',
            QuestionID: 1,
            Required: true,
            Options: [
                {
                    Option: '',
                    Value: 1
                },
                {
                    Option: '',
                    Value: 2
                }
            ],
            Answer: 0,
        },
        {
            Type: 'multiple',
            Question: '',
            QuestionID: 2,
            Required: true,
            Options: [
                {
                    Option: '',
                    Value: 1
                },
                {
                    Option: '',
                    Value: 2
                }
            ],
            Answers: []
        },
        {
            Type: 'text',
            Question: '',
            QuestionID: 3,
            Required: false,
            Answers: ''
        }
    ]
})


const AddOptionFN = (index) => {
    DataTemp.value.SurveyData[index - 1].Options.push({
        Option: '',
        Value: DataTemp.value.SurveyData[index - 1].Options.length + 1
    })
}

const DeleteQuestion = (index) => {
    // 有BUG 先这样 有需要在改
    DataTemp.value.SurveyData.splice(index - 1, 1)
}

const AddSigleFN = () => {
    DataTemp.value.SurveyData.push({
        Type: 'sigle',
        Question: '',
        QuestionID: DataTemp.value.SurveyData.length + 1,
        Required: true,
        Options: [
            {
                Option: '',
                Value: 1
            },
            {
                Option: '',
                Value: 2
            }
        ],
        Answer: 0,
        CollectAnswers: []
    })
}

const AddMutipleFN = () => {
    DataTemp.value.SurveyData.push({
        Type: 'multiple',
        Question: '',
        QuestionID: DataTemp.value.SurveyData.length + 1,
        Required: true,
        Options: [
            {
                Option: '',
                Value: 1
            },
            {
                Option: '',
                Value: 2
            },
            {
                Option: '',
                Value: 3
            },
            {
                Option: '',
                Value: 4
            }
        ],
        Answers: [],
        CollectAnswers: []
    })
}

const AddTextFN = () => {
    DataTemp.value.SurveyData.push({
        Type: 'text',
        Question: '',
        QuestionID: DataTemp.value.SurveyData.length + 1,
        Required: false,
        Answers: '',
        CollectAnswers: []
    })
}

const submitToSaveFN = () => {
    ElMessageBox.confirm(
        '模板一但保存将不可更改！请确定是否保存模板? ',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        DataTemp.value.SurveyID = AllDataLyt.value.PaperDesign[AllDataLyt.value.PaperDesign.length - 1].SurveyID + 1
        DataTemp.value.SurveyCreateTime = new Date().toLocaleString()

        // 深拷贝
        const newData = JSON.parse(JSON.stringify(DataTemp.value));
        AllDataLyt.value.PaperDesign.push(newData);

        const PostData = {
            Account: AllDataLyt.value.Account,
            Profile: JSON.stringify({
                PaperDesign: AllDataLyt.value.PaperDesign,
                PaperPublish: AllDataLyt.value.PaperPublish,
                PaperCollect: AllDataLyt.value.PaperCollect,
            })
        }

        usePost(api.UpdateServerMySQL, PostData).then(res => {
            Emits('change')
            ElMessage({
                type: 'success',
                message: '成功保存模板！',
            })
        }, rej => {
            ElMessage({
                type: 'error',
                message: '保存模板失败，请稍后再尝试！',
            })
        })

    }).catch(() => {
        ElMessage({
            type: 'info',
            message: '已取消本次操作',
            grouping: true
        })
    })
}

const QuitCoreFN = () => {
    ElMessageBox.confirm(
        '编辑的内容将不会保存！确定退出模板么? ',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            Emits('change')
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '已取消本次操作',
                grouping: true
            })
        })
}
</script>

<template>
    <div class="MainCoreBox">
        <div class="ToHiddenBK"></div>
        <quitnewBtn style="position: absolute;right: 1%;" @click="QuitCoreFN"></quitnewBtn>
        <div class="MainCoreBoxContent">
            <div class="Paper-Title">
                <span>问卷标题：</span>
                <input class="Paper-Title-Input" type="text" v-model="DataTemp.SurveyName" placeholder="请输入问卷标题">
            </div>

            <div class="Paper-Options" v-for="item in DataTemp.SurveyData">
                <div class="Detail" v-if="item.Type === 'sigle'">
                    <div class="head">
                        <div class="Detail-Question">
                            <span>第 {{ item.QuestionID }} 问：</span>
                            <input class="Example-Title-input" type="text" v-model="item.Question" placeholder="请输入问题">
                        </div>
                        <div class="Detail-Description">
                            <span>类型：单选 </span>
                            <input type="checkbox" value="true" checked="checked" v-model="item.Required">
                            <span>必填</span>
                        </div>
                    </div>
                    <div class="Detail-Options">
                        <div v-for="Option in item.Options">
                            <span>第 {{ Option.Value }} 选项：</span>
                            <input class="Example-Option-input" type="text" v-model="Option.Option" placeholder="请输入选项">
                        </div>
                    </div>
                    <div class="option-button">
                        <button @click="AddOptionFN(item.QuestionID)">添加选项</button>
                        <span style="display: inline-block;width: 12px;"></span>
                        <button @click="DeleteQuestion(item.QuestionID)">删除</button>
                    </div>
                </div>

                <div class="Detail" v-if="item.Type === 'multiple'">
                    <div class="head">
                        <div class="Detail-Question">
                            <span>第 {{ item.QuestionID }} 问：</span>
                            <input class="Example-Title-input" type="text" v-model="item.Question" placeholder="请输入问题">
                        </div>
                        <div class="Detail-Description">
                            <span>类型：多选 </span>
                            <input type="checkbox" value="true" checked="checked" v-model="item.Required">
                            <span>必填</span>
                        </div>
                    </div>
                    <div class="Detail-Options">
                        <div v-for="Option in item.Options">
                            <span>第 {{ Option.Value }} 选项：</span>
                            <input class="Example-Option-input" type="text" v-model="Option.Option" placeholder="请输入选项">
                        </div>
                    </div>
                    <div class="option-button">
                        <button @click="AddOptionFN(item.QuestionID)">添加选项</button>
                        <span style="display: inline-block;width: 12px;"></span>
                        <button @click="DeleteQuestion(item.QuestionID)">删除</button>
                    </div>
                </div>

                <div class="Detail" v-if="item.Type === 'text'">
                    <div class="head">
                        <div class="Detail-Question">
                            <span>第 {{ item.QuestionID }} 问：</span>
                            <input class="Example-Title-input" type="text" v-model="item.Question" placeholder="请输入问题">
                        </div>
                        <div class="Detail-Description">
                            <span>类型：文本 </span>
                            <input type="checkbox" value="true" checked="checked" v-model="item.Required">
                            <span>必填</span>
                        </div>
                    </div>
                    <div class="option-button">
                        <button @click="DeleteQuestion(item.QuestionID)">删除</button>
                    </div>
                </div>
            </div>

            <div class="New-Paper-Options">
                <div class="New-Paper-Options-Item" @click="AddSigleFN">添加单选模板</div>
                <div class="New-Paper-Options-Item" @click="AddMutipleFN">添加多选模板</div>
                <div class="New-Paper-Options-Item" @click="AddTextFN">添加文本模板</div>
            </div>
        </div>
        <submitBtn class="submitToSave" @click="submitToSaveFN"></submitBtn>
    </div>
</template>

<style scoped>
.MainCoreBox {

    .ToHiddenBK {
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background-color: #000000c0;
        z-index: -1;
    }

    .MainCoreBoxContent {
        width: 100%;
        height: 95%;
        color: #fff;

        overflow-x: hidden;
        overflow-y: auto;

        .Paper-Title {
            font-size: 28px;
            display: flex;
            justify-content: center;
            margin-bottom: 52px;

            .Paper-Title-Input {
                width: 380px;
                height: 38px;
                text-align: center;
                font-size: 24px;
                outline: none;
                transition: linear 0.3s;
            }

            .Paper-Title-Input:focus {
                width: 420px;
            }
        }

        .Paper-Options {
            width: 80%;
            margin: auto;
            background: linear-gradient(to right, #885cd398 0%, transparent 50%, #885cd398 100%);
            box-shadow:
                0px -5px 15px rgba(200, 200, 250, 0.5),
                0px 5px 15px rgba(200, 200, 250, 0.5),
                5px 0px 15px rgba(200, 200, 250, 0.5),
                -5px 0px 15px rgba(200, 200, 250, 0.5);
            padding: 8px 16px;
            border-radius: 8px;
            margin-bottom: 24px;

            position: relative;

            .option-button {
                position: absolute;
                bottom: 3%;
                right: 2%;

                button {
                    font-size: 16px;
                    outline: none;
                    width: 100px;
                    line-height: 30px;
                    text-align: center;
                    box-sizing: border-box;
                    padding: 0;
                }
            }

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
                        display: flex;
                        align-items: center;

                        .Example-Title-input {
                            outline: none;
                            width: 800px;
                            height: 28px;
                            text-align: center;
                            font-size: 18px;
                        }
                    }
                }

                .TxetArea {
                    width: 95%;
                    font-size: 18px;
                }

                .Detail-Options {
                    margin-left: 18px;

                    .Example-Option-input {
                        margin-bottom: 2px;
                        outline: none;
                        width: 300px;
                        text-align: center;
                    }
                }
            }

        }

        .New-Paper-Options {
            width: 80%;
            margin: auto;
            background: linear-gradient(to right, #885cd398 0%, transparent 50%, #885cd398 100%);
            box-shadow:
                0px -5px 15px rgba(200, 200, 250, 0.5),
                0px 5px 15px rgba(200, 200, 250, 0.5),
                5px 0px 15px rgba(200, 200, 250, 0.5),
                -5px 0px 15px rgba(200, 200, 250, 0.5);
            padding: 8px 16px;
            border-radius: 8px;
            margin-bottom: 24px;

            display: flex;
            justify-content: space-evenly;

            .New-Paper-Options-Item {
                width: 200px;
                line-height: 120px;
                text-align: center;
                cursor: pointer;
            }

        }
    }

    .submitToSave {
        position: absolute;
        bottom: -24px;
        left: 0;
    }
}
</style>
