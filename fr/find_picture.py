from PIL import Image
import face_recognition

#加载一个图片文件到一个numpy数组
image = face_recognition.load_image_file("images/biden.jpg")

#找到所有的脸在image用默认HOG模型
face_locations = face_recognition.face_locations(image)

print('i found {} face(s) in this photograph'.format(len(face_locations)))

for face_location in face_locations:

    # 打印每张脸的位置
    top,right,bottom,left = face_location
    print(
        "A face is located at pixel location Top: {}, Left: {}, Bottom: {}, Right: {}".format(top, left, bottom, right))

    face_image = image[top:bottom,left:right]
    pil_image = Image.fromarray(face_image)
    pil_image.show()