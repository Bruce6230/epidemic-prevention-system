<template>
	<view class="page">
		<image src="../../static/logo-3.jpg" mode="widthFix" class="logo"></image>
		<view class="add" v-if="checkPermission(['ROOT', 'MEETING:INSERT'])" 
		@tap="toMeetingPage(null,'insert')">
			<image src="../../static/icon-17.png" mode="widthFix" class="icon"></image>
			<text>创建会议</text>
		</view>
		<view v-for="one in list" :key="one.date">
			<view class="list-title">{{one.date}}</view>
			<view class="item" v-for="meeting in one.list" :key="meeting.id"
			@longpress="deleteById(meeting.id, meeting.date, meeting.start)">
				<view class="header">
					<view class="left">
						<image v-if="meeting.type=='线上会议'" src="../../static/icon-11.png" mode="widthFix" class="icon"></image>
						<image v-if="meeting.type=='线下会议'" src="../../static/icon-11.png" mode="widthFix" class="icon"></image>
						<text>{{meeting.type}}</text>
						<text :class="meeting.status=='未开始'?'blue':'red'">（{{meeting.status}}）</text>
					</view>
					<view class="right" @tap="toMeetingPage(meeting.id, 'edit')"
					v-if="checkPermission(['ROOT', 'MEETING:UPDATE']) && meeting.status == '未开始'">
						<text>编辑</text>
					</view>
				</view>
				<view class="content">
					<view class="title">{{meeting.title}}</view>
					<view class="attr">
						<view class="timer">
							<image src="../../static/icon-14.png" mode="widthFix" class="icon"></image>
							<text>{{meeting.start}} ~ {{meeting.end}}</text>
						</view>
						<view class="creator">
							<image src="../../static/icon-15.png" mode="widthFix" class="icon"></image>
							<text>{{meeting.name}}</text>
						</view>
						<view class="place" v-if="meeting.type!='线上会议'">
							<image src="../../static/icon-16.png" mode="widthFix" class="icon"></image>
							<text>{{meeting.place}}</text>
						</view>
					</view>
					<view class="desc">{{meeting.desc}}</view>
					<button class="btn" v-if="meeting.type=='线上会议'"
					@tap="enter(meeting.id,meeting.uuid,meeting.date,meeting.start)">进入</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			page: 1,
			length: 20,
			list: [
				{
					date: '2021年02月08日',
					list: [
						{ id: 1, date: '2021/02/08', start: '08:30', end: '10:30', type: '线上会议', name: '张爽', place: '网络会议室', status: '未开始',title:'年终大会', desc: '年终总结大会' },
						{ id: 2, date: '2021/02/08', start: '08:30', end: '10:30', type: '线上会议', name: '张爽', place: '网络会议室', status: '未开始',title:'年终大会', desc: '年终总结大会' }
					]
				},
				{
					date: '2021年02月09日',
					list: [{ id: 3, date: '2021/02/09', start: '08:30', end: '10:30', type: '线上会议', name: '张爽', place: '网络会议室', status: '未开始', title:'年终大会',desc: '年终总结大会' }]
				}
			],
			isLastPage: false
		};
	},
	onShow:function(){
		let that=this
		that.page=1
		that.isLastPage=false
		that.list=[]
		that.loadMeetingList(that)
	},
	onReachBottom:function(){
		let that=this
		if(that.isLastPage){
			return
		}
		that.page=that.page+1
		that.loadMeetingList(that)
	},
	methods: {
		loadMeetingList:function(ref){
			let data={
				page:ref.page,
				length:ref.length
			}
			ref.ajax(ref.url.searchMyMeetingListByPage,"POST",data,function(resp){
				let result=resp.data.result
				if(result==null||result.length==0){
					ref.isLastPage=true
					ref.page=ref.page-1
					if(ref.page>1){
						uni.showToast({
							icon:"none",
							title:"已经到底了"
						})
					}
				}
				else{
					for(let one of result){
						for(let meeting of one.list){
							if(meeting.type==1){
								meeting.type="线上会议"
							}
							else if(meeting.type==2){
								meeting.type="线下会议"
							}
							
							if(meeting.status==3){
								meeting.status="未开始"
							}
							else if(meeting.status==4){
								meeting.status="进行中"
							}
						}
						if(ref.list.length>0){
							let last=ref.list[ref.list.length-1]
							if(last.date==one.date){
								last.list=last.list.concat(one.list)
							}
							else{
								ref.list.push(one)
							}
						}
						else{
							ref.list.push(one)
						}
					}
				}
			})
		},
		toMeetingPage:function(id,opt){
			uni.navigateTo({
				url:"../meeting/meeting?id="+id+"&opt="+opt
			})
		},
		deleteById:function(id,date,start){
			let now=new Date()
			let meetingDate=new Date(date+" "+start+":00")
			if(now.getTime()>=meetingDate.getTime()-20*60*1000){
				uni.showToast({
					icon:"none",
					title:"该会议无法删除"
				})
				return
			}
			let that=this
			uni.vibrateShort({})
			uni.showModal({
				title:"提示信息",
				content:"是否删除这个会议？",
				success:function(resp){
					if(resp.confirm){
						let data={
							id:id
						}
						that.ajax(that.url.deleteMeetingById,"POST",data,function(resp){
							uni.showToast({
								icon:"success",
								title:"删除成功",
								complete:function(){
									setTimeout(function(){
										that.page=1
										that.isLastPage=false
										uni.pageScrollTo({
											scrollTop:"0"
										})
										that.list=[]
										that.loadMeetingList(that)
									},2000)
								}
							})
						})
					}
				}
			})
		},
		enter:function(id,uuid,date,start){
			date=date.replace("年","/").replace("月","/").replace("日","")
			let begin=new Date(date+" "+start+":00")
			let now=new Date()
			if(now.getTime()>=begin.getTime()-10*60*1000){
				this.ajax(this.url.searchRoomIdByUUID,"POST",{uuid,uuid},function(resp){
					let roomId=resp.data.result
					uni.navigateTo({
						url:"../video_meeting/video_meeting?id="+id+"&roomId="+roomId
					})
				})
			}
			else{
				uni.showToast({
					icon:"none",
					title:"会议开始前10分钟才能进入"
				})
			}
		}
	}
};
</script>

<style lang="less">
@import url('meeting_list.less');
</style>
