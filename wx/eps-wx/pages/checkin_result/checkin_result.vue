<template>
	<view class="page">
		<view class="summary-container">
			<view class="user-info">
				<image :src="photo" mode="widthFIx" class="photo"></image>
				<view class="info">
					<text class="username">{{name}}</text>
					<text class="dept">隶属部门：{{deptName==null?"":deptName}}</text>
				</view>
			</view>
			<view class="date">{{date}}</view>
		</view>
		<view class="result-container">
			<view class="left">
				<image src="../../static/icon-6.png" mode="widthFix" class="icon-timer"></image>
				<image src="../../static/icon-6.png" mode="widthFix" class="icon-timer"></image>
				<view class="line"></view>
			</view>
			<view></view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				name: "GentlemanLin",
				photo: "../../static/user.png",
				deptName: "管理部",
				address: "重庆市",
				status: "正常",
				risk: "中风险",
				checkinTime: "8:25",
				date: "2022年7月14日",
				attendanceTime: "8:30",
				closingTime: "17:30",
				checkinDays: 201,
				weekCheckin: [
					{type:"工作日",day:"周一",status:"正常"},
					{type:"工作日",day:"周二",status:"正常"},
					{type:"工作日",day:"周三",status:"正常"},
					{type:"工作日",day:"周四",status:"正常"},
					{type:"工作日",day:"周五",status:"正常"},
					{type:"休息日",day:"周六",status:""},
					{type:"休息日",day:"周日",status:""}
				]
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="less">
	@import url("checkin_result.less");
</style>
