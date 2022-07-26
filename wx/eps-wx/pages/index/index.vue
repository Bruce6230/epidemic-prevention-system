<template>
	<view class="page">
		<!-- content -->
		<!-- <image class="logo" src="/static/logo.png"></image>
		<view class="text-area">
			<text class="title">{{title}}</text>
		</view> -->
		<!-- 轮播图设计 -->
		<swiper circular="true" interval="8000" duration="1000" class="swiper">
			<swiper-item>
				<image mode="widthFix" src="https://lin171820-1306546927.cos.ap-beijing.myqcloud.com/makiyo/banner/swiper-1.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix" src="https://lin171820-1306546927.cos.ap-beijing.myqcloud.com/makiyo/banner/swiper-2.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix" src="https://lin171820-1306546927.cos.ap-beijing.myqcloud.com/makiyo/banner/swiper-3.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix" src="https://lin171820-1306546927.cos.ap-beijing.myqcloud.com/makiyo/banner/swiper-4.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix" src="https://lin171820-1306546927.cos.ap-beijing.myqcloud.com/makiyo/banner/swiper-5.jpg"></image>
			</swiper-item>
		</swiper>
		<!-- 通知栏设计 -->
		<view class="notify-container" @tap="toPage('消息提醒','/pages/message_list/message_list')">
			<view class="notify-title">
				<image src="../../static/icon-1.png" mode="widthFix" class="notify-icon"></image>
				消息提醒
			</view>
			<view class="notify-content">
				你有{{unreadRows}}条未读信息
			</view>
			<image src="../../static/icon-2.png" mode="widthFix" class="more-icon"></image>
		</view>
		<view class="nav-container">
			<view class="nav-row">
				<view class="nav" @tap="toPage('在线签到','../checkin/checkin')">
					<image mode="widthFix" src="../../static/nav-1.png" class="icon"></image>
					<text class="name">在线签到</text>
				</view>
				<view class="nav">
					<image src="../../static/nav-2.png" mode="widthFix" class="icon"></image>
					<text class="name">疲劳驾驶</text>
				</view>
				<view class="nav">
					<image src="../../static/nav-3.png" mode="widthFix" class="icon"></image>
					<text class="name">在线请假</text>
				</view>
				<view class="nav">
					<image src="../../static/nav-4.png" mode="widthFix" class="icon"></image>
					<text class="name">公务出差</text>
				</view>
			</view>
			<view class="nav-row">
				<view class="nav">
					<image src="../../static/nav-5.png" mode="widthFix" class="icon"></image>
					<text class="name">员工日报</text>
				</view>
				<view class="nav">
					<image src="../../static/nav-6.png" mode="widthFix" class="icon"></image>
					<text class="name">我的加班</text>
				</view>
				<view class="nav">
					<image src="../../static/nav-7.png" mode="widthFix" class="icon"></image>
					<text class="name">付款申请</text>
				</view>
				<view class="nav">
					<image src="../../static/nav-8.png" mode="widthFix" class="icon"></image>
					<text class="name">费用报销</text>
				</view>
			</view>
			<view class="nav-row">
				<view class="nav">
					<image src="../../static/nav-9.png" mode="widthFix" class="icon"></image>
					<text class="name">公告通知</text>
				</view>
				<view class="nav" @tap="toPage('在线审批', '../approval/approval')">
					<image src="../../static/nav-10.png" mode="widthFix" class="icon"></image>
					<text class="name">在线审批</text>
				</view>
				<view class="nav">
					<image src="../../static/nav-11.png" mode="widthFix" class="icon"></image>
					<text class="name">物品领用</text>
				</view>
				<view class="nav">
					<image src="../../static/nav-12.png" mode="widthFix" class="icon"></image>
					<text class="name">采购申请</text>
				</view>
			</view>
		</view>
		<uni-popup ref="popupMsg" type="top">
			<uni-popup-message type="success" :message="'接收到' + lastRows + '条消息'" :duration="2000"></uni-popup-message>
		</uni-popup>
	</view>
</template>

<script>
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue';
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue';
	export default {
		components:{
			uniPopup,
			uniPopupMessage,
			uniPopupDialog,
		},
		data() {
			return {
				// title: 'Hello'
				unreadRows:0,
				lastRows:0,
				timer:null,
			}
		},
		onLoad:function() {
			let that=this
			uni.$on("showMessage",function(){
				that.$refs.popupMsg.open()
			})
			that.ajax(that.url.refreshMessage,"GET",null,function(resp){
				that.unreadRows=resp.data.unreadRows
				that.lastRows=resp.data.lastRows
				if(that.lastRows>0){
					uni.$emit("showMessage")
				}
			})
		},
		onUnload:function(){
			uni.$off("showMessage")
		},
		onShow:function(){
			let that=this
			that.timer=setInterval(function(){
				that.ajax(that.url.refreshMessage,"GET",null,function(resp){
					that.unreadRows=resp.data.unreadRows
					that.lastRows=resp.data.lastRows
					if(that.lastRows>0){
						uni.$emit("showMessage")
					}
				})
			},5000)
			// that.meetingPage=1
			// that.isMeetingLastPage=false
			// that.meetingList=[]
			// that.loadMeetingList(that)
			// let date=new Date()
			// that.loadMeetingInMonth(that,date.getFullYear(),date.getMonth()+1)
		},
		onHide:function(){
			let that=this
			clearInterval(that.timer)
		},
		methods: {
			toPage:function(name,url){
				//验证用户权限
				uni.navigateTo({
					url:url
				})
			}
		}
	}
</script>

<style lang="less">
	/* .content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	} */
	@import url("index.less");
</style>
