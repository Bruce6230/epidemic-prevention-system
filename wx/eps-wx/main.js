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

//修改ip
let BaseUrl = "http://192.168.31.152:8080/epidemic-prevention-system"
//工作流地址
let workflow="http://CentOS的IP:9090/workflow"

Vue.prototype.url={
	register: BaseUrl+"/user/register",
	login: BaseUrl+"/user/login",
	checkin: BaseUrl+"/checkin/checkin",
	createFaceModel: BaseUrl+"/checkin/createFaceModel",
	validCanCheckIn: BaseUrl+"/checkin/validCanCheckIn",
	searchTodayCheckin: BaseUrl + "/checkin/searchTodayCheckin",
	searchUserSummary: BaseUrl + "/user/searchUserSummary",
	searchMonthCheckin: BaseUrl + "/checkin/searchMonthCheckin",
	refreshMessage: BaseUrl + "/message/refreshMessage",
	searchMessageByPage: BaseUrl + "/message/searchMessageByPage",
	searchMessageById: BaseUrl + "/message/searchMessageById",
	updateUnreadMessage: BaseUrl + "/message/updateUnreadMessage",
	deleteMessageRefById: BaseUrl + "/message/deleteMessageRefById",
	searchMyMeetingListByPage: BaseUrl + "/meeting/searchMyMeetingListByPage",
	searchUserGroupByDept: BaseUrl + "/user/searchUserGroupByDept",
	insertMeeting: BaseUrl + "/meeting/insertMeeting",
	updateMeetingInfo: BaseUrl + "/meeting/updateMeetingInfo",
	deleteMeetingById:BaseUrl+"/meeting/deleteMeetingById",
	searchUserTaskListByPage:workflow+"/workflow/searchUserTaskListByPage",
	approvalMeeting:workflow+"/workflow/approvalMeeting",
	selectUserPhotoAndName:BaseUrl+"/user/selectUserPhotoAndName",
	genUserSig: BaseUrl + "/user/genUserSig",
	searchRoomIdByUUID: BaseUrl + "/meeting/searchRoomIdByUUID",
	searchUserMeetingInMonth:BaseUrl+"/meeting/searchUserMeetingInMonth"
}

Vue.prototype.checkPermission = function(perms) {
	let permission = uni.getStorageSync("permission")
	let result = false
	for (let one of perms) {
		if (permission.indexOf(one) != -1) {
			result = true
			break
		}
	}
	return result
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

Vue.prototype.checkNull = function(data, name) {
	if (data == null) {
		uni.showToast({
			icon: "none",
			title: name + "不能为空"
		})
		return true
	}
	return false
}
Vue.prototype.checkBlank = function(data, name) {
	if (data == null || data == "") {
		uni.showToast({
			icon: "none",
			title: name + "不能为空"
		})
		return true
	}
	return false
}
Vue.prototype.checkValidName = function(data, name) {
	if (data == null || data == "") {
		uni.showToast({
			icon: "none",
			title: name + "不能为空"
		})
		return true
	} else if (!/^[\u4e00-\u9fa5]{2,15}$/.test(data)) {
		uni.showToast({
			icon: "none",
			title: name + "内容不正确"
		})
		return true
	}
	return false
}
Vue.prototype.checkValidTel = function(data, name) {
	if (data == null || data == "") {
		uni.showToast({
			icon: "none",
			title: name + "不能为空"
		})
		return true
	} else if (!/^1[0-9]{10}$/.test(data)) {
		uni.showToast({
			icon: "none",
			title: name + "内容不正确"
		})
		return true
	}
	return false
}
Vue.prototype.checkValidEmail = function(data, name) {
	if (data == null || data == "") {
		uni.showToast({
			icon: "none",
			title: name + "不能为空"
		})
		return true
	} else if (!/^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(data)) {
		uni.showToast({
			icon: "none",
			title: name + "内容不正确"
		})
		return true
	}
	return false
}
Vue.prototype.checkValidStartAndEnd = function(start, end) {
	let d1 = new Date("2000/01/01 " + start + ":00");
	let d2 = new Date("2000/01/01 " + end + ":00");
	if (d2.getTime() <= d1.getTime()) {
		uni.showToast({
			icon: "none",
			title: "结束时间必须大于开始时间"
		})
		return true
	}
	return false
}