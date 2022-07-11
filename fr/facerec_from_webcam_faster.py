import face_recognition
import cv2
import numpy as np

# 每次检测一帧的1/4

# 捕捉摄像头对象
video_capture = cv2.VideoCapture(0)

# 加载示例图片并学习如何识别它
GentlemanLin_image = face_recognition.load_image_file("images/linjiahao.jpg")
GentlemanLin_face_encoding = face_recognition.face_encodings(GentlemanLin_image)[0]

# 创建一个已知人脸编码及其名称的数组
known_face_encodings = [
    GentlemanLin_face_encoding
]

known_face_names = [
    "GentlemanLin"
]

# 初始化一些变量
face_locations = []

face_encodings = []

face_names = []

process_this_frame = True

while True:
    # 捕捉画面一帧
    ret,frame = video_capture.read()

    # 只处理每一帧节省时间
    if process_this_frame:
        # 将视频帧大小调整为1/4大小，加快人脸识别处理
        small_frame = cv2.resize(frame,(0,0),fx=0.25,fy=0.25)

        # 将图像从BGR颜色(OpenCV)转换成RGB颜色(face_recognition)
        rgb_small_frame = small_frame[:,:,::-1]

        # 查找当前帧中所有的人脸和人脸编码
        face_locations = face_recognition.face_locations(rgb_small_frame)
        face_encodings = face_recognition.face_encodings(rgb_small_frame,face_locations)

        face_names = []

        for face_encoding in face_encodings:
            # 查看该人脸编码是否在已知人脸中存在匹配
            matches = face_recognition.compare_faces(known_face_encodings,face_encoding)
            name = "unknown"

            face_distances = face_recognition.face_distance(known_face_encodings,face_encoding)
            best_match_index = np.argmin(face_distances)
            if matches[best_match_index]:
                name = known_face_names[best_match_index]

            face_names.append(name)

    process_this_frame = not process_this_frame

    # 显示结果
    for (top,right,bottom,left),name in zip(face_locations,face_names):
        # 缩放人脸位置，因为检测到的帧被缩放到1/4大小
        top *= 4
        right *= 4
        bottom *= 4
        left *= 4

        # 在脸部周围画一个框
        cv2.rectangle(frame,(left,top),(right,bottom),(255,255,0),2)

        # 在人脸下方画一个带有名字的标签
        cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (255, 255, 0), cv2.FILLED)
        font = cv2.FONT_HERSHEY_PLAIN
        cv2.putText(frame, name, (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)

    # 显示生成的图像
    cv2.imshow('Video',frame)

    # 按键盘上的q退出
    if cv2.waitKey(1)&0xFF==ord('q'):
        break

# 释放摄像头并销毁
video_capture.release()
cv2.destroyAllWindows()