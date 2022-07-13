<template>
	<view>
		<camera device-position="front" flash="off" class="camera" @error="error" v-if="showCamera"></camera>
		<image mode="widthFix" class="image" :src="photoPath" v-if="showImage"></image>
		<view class="operate-container">
			<button type="primary" class="button" @tap="clickButton" :disabled="!canCheckin">{{buttonText}}</button>
			<button type="warn" class="button" @tap="aFresh" :disabled="!canChekin">重拍</button>
		</view>
		<view class="notice-container">
			<text class="notice">注意事项</text>
			<text class="describe">拍照时请拍摄正面照片</text>
		</view>
	</view>
</template>

<script>
	var TxMapWx=require('../../lib/qqmap-wx-jssdk.min.js');
	var txmapsdk;
	export default {
		data() {
			return {
				canCheckin: true,
				photoPath: '',
				buttonText: '拍照',
				showCamera: true,
				showImage: false
			}
		},
		onLoad:function(){
			txmapsdk = new TxMapWx({
				key:"OMSBZ-OVURD-YCN4O-HB6FZ-O6P66-JIFBR"
			})
		},
		onShow:function()
		{
			let that = this
			that.ajax(that.url.validCanCheckIn,"GET",null,function(response){
				let msg = response.data.msg
				if(msg!="可以考勤")
				{
					that.checkin=false
					setTimeout(function(){
						uni.showToast({
							title:msg,
							icon:"none"
						})
					},1000)
				}
			})
		},
		methods: {
			clickButton:function()
			{
				let that = this
				if(that.buttonText=='拍照')
				{
					let creatcameracontext = uni.createCameraContext();
					creatcameracontext.takePhoto({
						quality:"high",
						success:function(response){
							console.log(response.tempImagePath)
							that.photoPath=response.tempImagePath
							that.showCamera=false
							that.showImage=true
							that.buttonText='签到'
						}
					})
				}else{
					//执行签到功能
					uni.showLoading({
						title:"签到中请稍后"
					})
					setTimeout(function(){
						uni.hideLoading()
					},30000)
					
					uni.getLocation({
						type:"wgs84",
						success:function(response){
							let latitude=response.latitude
							let longitude=response.longitude
							// console.log(latitude)
							// console.log(longitude)
							txmapsdk.reverseGeocoder({
								location:{
									latitude:latitude,
									longitude:longitude
								},
								success:function(response){
									// console.log(response.result)
									let address=response.result.address
									let addressComponent=response.result.address_component
									let nation = addressComponent.nation;
									let province = addressComponent.province;
									let city = addressComponent.city;
									let district = addressComponent.district;
									uni.uploadFile({
										url:that.url.checkin,
										filePath:that.photoPath,
										name:"photo",
										header:{
											token:uni.getStorageSync("token")
										},
										formData:{
											address:address,
											country:nation,
											province:province,
											city:city,
											district:district
										},
										success:function(response){
											if(response.statusCode==500&&response.data=="不存在人脸模型"){
												uni.hideLoading()
												uni.showModal({
													title:"提示信息",
													content:"Eps系统中不存在你的人脸模型，是否选用当前照片作为人脸模型?",
													success:function(response){
														if(response.confirm){
															uni.uploadFile({
																url:that.url.createFaceModel,
																filePath:that.photoPath,
																name:"photo",
																header:{
																	token:uni.getStorageSync("token")
																},
																success:function(response){
																	if(response.statusCode==500){
																		uni.showToast({
																			title:response.data,
																			icon:"none"
																		})
																	}else if(response.statusCode==200){
																		uni.showToast({
																			title:"人脸建模成功",
																			icon:"none"
																		})
																	}
																},
															})
														}
													}
												})
											}else if(response.statusCode==200){
												// 获得字符串，解析为JSON
												let data = JSON.parse(response.data)
												let code = data.code
												let msg = data.msg
												if(code==200){
													uni.hideLoading()
													uni.showToast({
														title:"签到成功",
														complete:function(){
															
														}
													})
												}
											}else if(response.statusCode==500){
												uni.showToast({
													title:response.data,
													icon:"none"
												})
											}
										}
										
									})
								}
							})
						}
					})
					
				}
			},
			aFresh:function(){
				let that = this
				that.showCamera=true;
				that.showImage=false;
				that.buttonText='拍照'
			}
		}
	}
</script>

<style lang="less">
@import url("checkin.less");
</style>
