import face_recognition
import cv2

# 用cnn模型找出一个列表中全部人脸

# 打开视频文件
video_capture = cv2.VideoCapture("video/short_hamilton_clip.mp4")

# 帧数
frames = []
frame_count = 0

while video_capture.isOpened():
    # 从视频中捕获一帧
    ret,frame = video_capture.read()

    # 视频文件结束时退出
    if not ret:
        break

    # 将图像从BGR(opencv使用）转换为RGB颜色（face_recognition使用)
    frame = frame[:,:,::-1]

    # 将视频中的每一帧保存到列表
    frame_count += 1
    frames.append(frame)

    # 每128帧，批量处理帧列表以查找人脸
    if len(frames) == 128:
        face_locations = face_recognition.batch_face_locations(frames,number_of_times_to_upsample=0)

        # 列出所有128帧中找到的所有面孔
        for frame_number_in_batch,face_location in enumerate(face_locations):
            number_of_faces_in_frame = len(face_locations)

            frame_number = frame_count-128+frame_number_in_batch
            print("I found {} face(s) in frame #{}.".format(number_of_faces_in_frame, frame_number))

            for location in face_location:
                # 打印每张脸在图片中的位置
                top,right,bottom,left=face_location
                print(" - A face is located at pixel location Top: {}, Left: {}, Bottom: {}, Right: {}".format(top, left,bottom,right))

        # 清空frames数组为下一组
        frames=[]