import { storeToRefs } from 'pinia'

import { useAppData } from '@/store/AppData'
import { usePost } from '@/api/DataAsk.js'
import { api } from '@/utils/request.js'

const useAppDataStore = useAppData()

const AllDataLyt = storeToRefs(useAppDataStore).testData

export const UpdateMySQL = () => {
    usePost(api.UpdateServerMySQL, AllDataLyt.value).then(res => {
        console.log(res)
        return true
    }, rej => {
        console.log(rej)
        return false
    })
}