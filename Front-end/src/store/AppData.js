import { defineStore } from 'pinia'
import { setSession, getSession } from '@/utils/storage'

export const useAppData = defineStore('APPDATA', {
    state() {
        return {
            userInfo: getSession('__user__', {}),
            testData: {}
        }
    },
    actions: {
        saveData(value) {
            this.testData = value
        }
    },
    getters: {}
})