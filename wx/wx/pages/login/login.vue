<template>
	<view>
		<image src="../../static/logo-1.png" class="logo" mode="widthFix"></image>
		<view class="logo-title">EPS线上防疫办公系统</view>
		<view class="logo-subtitle">version 1.0</view>
		<button class="login-button" open-type="getUserInfo" @tap="login()">登陆</button>
		<view class="register-text">
			没有账号?
			<text class="register" @tap="toRegister()">立即注册</text>
		</view>
		<image src="../../static/logo-4.png" class="sublogo" mode="widthFix"></image>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="less">
@import url('login.less');
</style>
