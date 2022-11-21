import os

import face_recognition
import numpy as np
from flask import Flask, jsonify, request, redirect, Response

ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif'}

app = Flask(__name__)


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route('/', methods=['GET', 'POST'])
def upload_image():
    # Check if a valid image file was uploaded
    if request.method == 'POST':
        if 'file' not in request.files:
            return redirect(request.url)

        file = request.files['file']

        if file.filename == '':
            return redirect(request.url)

        if file and allowed_file(file.filename):

            return detect_faces_in_image(file)


    return '''
    <!doctype html>
    <title>Is this a picture of Obama?</title>
    <h1>Upload a picture and see if it's a picture of Obama!</h1>
    <form method="POST" enctype="multipart/form-data">
      <input type="file" name="file">
      <input type="submit" value="Upload">
    </form>
    '''


def detect_faces_in_image(file_stream):
    # Pre-calculated face encoding of Obama generated with face_recognition.face_encodings(img)
    # 加载示例图片并学习如何识别它
    GentlemanLin_image = face_recognition.load_image_file("images/linjiahao.jpg")
    GentlemanLin_face_encoding = face_recognition.face_encodings(GentlemanLin_image)[0]

    # 创建一个已知人脸编码及其名称的数组
    known_face_encoding = [
        GentlemanLin_face_encoding
    ]
    print(known_face_encoding)
    # known_face_encoding = [-0.09634063,  0.12095481, -0.00436332, -0.07643753,  0.0080383,
    #                         0.01902981, -0.07184699, -0.09383309,  0.18518871, -0.09588896,
    #                         0.23951106,  0.0986533 , -0.22114635, -0.1363683 ,  0.04405268,
    #                         0.11574756, -0.19899382, -0.09597053, -0.11969153, -0.12277931,
    #                         0.03416885, -0.00267565,  0.09203379,  0.04713435, -0.12731361,
    #                        -0.35371891, -0.0503444 , -0.17841317, -0.00310897, -0.09844551,
    #                        -0.06910533, -0.00503746, -0.18466514, -0.09851682,  0.02903969,
    #                        -0.02174894,  0.02261871,  0.0032102 ,  0.20312519,  0.02999607,
    #                        -0.11646006,  0.09432904,  0.02774341,  0.22102901,  0.26725179,
    #                         0.06896867, -0.00490024, -0.09441824,  0.11115381, -0.22592428,
    #                         0.06230862,  0.16559327,  0.06232892,  0.03458837,  0.09459756,
    #                        -0.18777156,  0.00654241,  0.08582542, -0.13578284,  0.0150229 ,
    #                         0.00670836, -0.08195844, -0.04346499,  0.03347827,  0.20310158,
    #                         0.09987706, -0.12370517, -0.06683611,  0.12704916, -0.02160804,
    #                         0.00984683,  0.00766284, -0.18980607, -0.19641446, -0.22800779,
    #                         0.09010898,  0.39178532,  0.18818057, -0.20875394,  0.03097027,
    #                        -0.21300618,  0.02532415,  0.07938635,  0.01000703, -0.07719778,
    #                        -0.12651891, -0.04318593,  0.06219772,  0.09163868,  0.05039065,
    #                        -0.04922386,  0.21839413, -0.02394437,  0.06173781,  0.0292527 ,
    #                         0.06160797, -0.15553983, -0.02440624, -0.17509389, -0.0630486 ,
    #                         0.01428208, -0.03637431,  0.03971229,  0.13983178, -0.23006812,
    #                         0.04999552,  0.0108454 , -0.03970895,  0.02501768,  0.08157793,
    #                        -0.03224047, -0.04502571,  0.0556995 , -0.24374914,  0.25514284,
    #                         0.24795187,  0.04060191,  0.17597422,  0.07966681,  0.01920104,
    #                        -0.01194376, -0.02300822, -0.17204897, -0.0596558 ,  0.05307484,
    #                         0.07417042,  0.07126575,  0.00209804]

    # Load the uploaded image file
    img = face_recognition.load_image_file(file_stream)
    # Get face encodings for any faces in the uploaded image
    unknown_face_encodings = face_recognition.face_encodings(img)

    face_found = False
    is_obama = False

    print(len(unknown_face_encodings))

    if len(unknown_face_encodings) > 0:
        face_found = True
        # See if the first face in the uploaded image matches the known face of Obama
        match_results = face_recognition.compare_faces(known_face_encoding, unknown_face_encodings[0])
        if match_results[0]:
            is_obama = True

    # Return the result as json
    result = {
        "face_found_in_image": face_found,
        "is_picture_of_obama": is_obama
    }
    return jsonify(result)

# # 创建人脸识别模型
# @app.route("/createFaceModel",methods=['POST','GET'])
# def upload():
#     # 获取request中photo参数文件
#     f = request.files.get('photo')
#     upload_path = os.path.join("images/"+f.filename)
#     f.save(upload_path)
#     # 保存图片
#     Photo_image = face_recognition.load_image_file("images/"+f.filename)
#     Photo_face_encoding = face_recognition.face_encodings(Photo_image)[0]
#     face_encoding_str = ','.join(str(x) for x in Photo_face_encoding)
#     response = Response()
#
#     # 创建一个已知人脸编码及其名称的数组
#     known_face_encoding = [
#         Photo_face_encoding
#     ]
#     if(len(known_face_encoding)>1):
#         response.data = "存在多张人脸"
#     elif (len(known_face_encoding)==1):
#         response.data=face_encoding_str
#     else:
#         response.data = "无法识别人脸"
#     response.status_code=200
#     return response

# 创建人脸识别模型
@app.route("/create_face_model",methods=['POST','GET'])
def create_face_model():
    # 获取request中photo参数文件
    f = request.files.get('photo')
    upload_path = os.path.join("images/"+f.filename)
    # 保存图片
    f.save(upload_path)
    Photo_image = face_recognition.load_image_file("images/"+f.filename)
    Photo_face_encoding = face_recognition.face_encodings(Photo_image)[0]
    face_encoding_str = ','.join(str(x) for x in Photo_face_encoding)
    response = Response()

    # 创建一个已知人脸编码及其名称的数组
    known_face_encoding = [
        Photo_face_encoding
    ]
    if(len(known_face_encoding)>1):
        response.data = "存在多张人脸"
    elif (len(known_face_encoding)==1):
        response.data=face_encoding_str
    else:
        response.data = "无法识别人脸"
    response.status_code=200
    return response

# 人脸识别检测
@app.route("/checkin",methods=['POST','GET'])
def checkin():
    # 获取request中photo参数图片
    f = request.files.get('photo')
    upload_path = os.path.join("images/" + f.filename)
    # 保存图片
    f.save(upload_path)
    Photo_image = face_recognition.load_image_file("images/" + f.filename)
    Photo_face_encoding = face_recognition.face_encodings(Photo_image)[0]
    # 获取request中人脸模型128维向量
    value = request.form.get('targetModel')
    # 创建已知128维向量
    known_face_encodings = []
    # 创建未知128维向量
    unknown_face_encoding = [
        Photo_face_encoding
    ]
    # 进行类型转换，并存储到known_face_encodings中
    known_face_encodings.append(np.array([float(x) for x in value.split(',')]))
    response = Response()
    if len(unknown_face_encoding)>1:
        response.data="照片中存在多张人脸"
    elif len(unknown_face_encoding)==1:
        # 进行人脸比较
        match_results = face_recognition.compare_faces(known_face_encodings, Photo_face_encoding)
        if match_results[0]:
            response.data="True"
        else:
            response.data = "False"
    else:
        response.data = "无法识别出人脸"
    response.status_code = 200
    return response

if __name__ == "__main__":
    app.run(host="192.168.137.128", port=5001, debug=True)