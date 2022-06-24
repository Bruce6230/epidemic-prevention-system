<template>
	<view>
		<camera device-position="front" flash="off" class="camera" @error="error" v-if="showCamera"></camera>
		<image mode="widthFix" class="image" :src="photoPath" v-if="showImage"></image>
		<view class="operate-container">
			<button type="primary" class="button" @tap="clickButton" :disabled="canCheckin">{{buttonText}}</button>
			<button type="warn" class="button" @tap="aFresh" :disabled="canChekin">重拍</button>
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
				canCheckin: false,
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
									console.log(response.result)
									let address=response.result.address
									let addressComponent=response.result.address_component
									let nation = addressComponent.nation;
									let province = addressComponent.province;
									let city = addressComponent.city;
									let district = addressComponent.district;
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
