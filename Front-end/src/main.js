import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import './style.css'
import 'css-doodle'
import 'animate.css'
import 'element-plus/dist/index.css'

import App from './App.vue'
import router from './router'

const pinia = createPinia()
const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app
    .use(pinia)
    .use(router)
    .use(ElementPlus)
    .mount('#app')
