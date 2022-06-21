<template>
	<view>
		<image src="../../static/logo-1.png" class="logo" mode="widthFix"></image>
		<view class="logo-title">Eps简疫办公系统</view>
		<view class="logo-subtitle">Version 1.0</view>
		<button class="login-button" open-type="getUserInfo" @tap="login()">登陆系统</button>
		<view class="register-container">
			没有账号？
			<text class="register" @tap="toRegister()">立即注册</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			toRegister:function(){
				uni.navigateTo({
					url:"../register/register"
				})
			},
			login:function(){
				let that=this
				uni.login({
					provider:"weixin",
					success:function(response){
						let code=response.code
						that.ajax(that.url.login,"POST",{"code":code},function(response){
							let permission=response.data.permission
							uni.setStorageSync("permission",permission)
						})
						console.log("success")
						//跳转到登陆页面
						uni.switchTab({
							url:"../index/index"
						})
					},
					fail:function(e){
						console.log(e)
						uni.showToast({
							icon:"none",
							title:"执行异常"
						})
					}
				})
			}
		}
	}
</script>

<style lang="less">
	@import url("login.less");
</style>
