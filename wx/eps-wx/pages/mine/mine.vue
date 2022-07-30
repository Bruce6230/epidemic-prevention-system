<template>
	<view class="page">
		<view class="user-info">
			<view class="user">
				<image src="../../static/user.png" mode="widthFix" class="photo"></image>
				<view class="row">
					<text class="username">{{name}}</text>
				</view>
			</view>
			<view class="summary">
				<view>
					<text class="title">部门</text>
					<text class="value">{{deptName}}</text>
				</view>
				<view>
					<text class="title">状态</text>
					<text class="value">
						在职
					</text>
				</view>
			</view>
		</view>
		<view class="list-title">用户信息</view>
		<uni-list>
			<uni-list-item title="个人资料" thumb="../../static/icon-21.png" thumbSize="sm" link to=""></uni-list-item>
			<uni-list-item title="考勤记录" thumb="../../static/icon-22.png" thumbSize="sm" link to="/pages/mine_checkin/mine_checkin"></uni-list-item>
			<uni-list-item title="罚款记录" thumb="../../static/icon-23.png" thumbSize="sm" link to=""></uni-list-item>
		</uni-list>
		<view class="list-title">系统管理</view>
		<uni-list>
			<uni-list-item title="员工管理" thumb="../../static/icon-24.png" thumbSize="sm" v-show="checkPermission(['ROOT','EMPLOYEE:SELECT'])" link to=""></uni-list-item>
			<uni-list-item title="部门管理" thumb="../../static/icon-25.png" thumbSize="sm" v-show="checkPermission(['ROOT','DEPT:SELECT'])" link to=""></uni-list-item>
			<uni-list-item title="权限管理" thumb="../../static/icon-26.png" thumbSize="sm" v-show="checkPermission(['ROOT','ROLE:SELECT'])" link to=""></uni-list-item>
		</uni-list>
	</view>
</template>

<script>
	import uniList from '@/components/uni-list/uni-list.vue';
	import uniListItem from '@/components/uni-list-item/uni-list-item.vue';
	export default {
		components:{
			uniList,
			uniListItem
		},
		data() {
			return {
				name:"",
				deptName:"",
				photo:""
			}
		},
		onShow:function(){
			let that=this
			that.ajax(that.url.searchUserSummary,"GET",null,function(response){
				let result=response.data.result
				that.name=result.name
				that.deptName=result.deptName
				that.photo=result.photo
			})
		},
		methods: {
			
		}
	}
</script>

<style lang="less">
	@import url("mine.less");
</style>
