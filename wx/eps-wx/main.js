import App from './App'

// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif

let BaseUrl = "http://localhost:8080/epidemic-prevention-system"

Vue.prototype.url={
	register:BaseUrl+"/user/register"
}

Vue.prototype.ajax=function(url , method, data, fun){
	uni.request({
		"url":url,
		"method":method,
		"header":{
			token:uni.getStorageSync("token")
		},
		"data":data,
		success:function(response){
			if(response.statusCode==401)
			{
				uni.redirectTo({
					url:"/pages/login/login.vue"
				})
			}else if(response.statusCode==200&&response.data.code==200){
				let data = response.data
				if(data.hasOwnProperty("token")){
					let token = data.token
					console.log(token)
					uni.setStorageSync("token",token)
				}
				fun(response)
			}else{
				uni.showToast({
					icon:"none",
					title:response.data
				})
			}
		}
	})
}