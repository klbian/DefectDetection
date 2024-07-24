import base64
import json
import cv2
import numpy as np
from flask import Flask, request
from ultralytics import YOLOv10
app = Flask(__name__)


@app.route('/test',methods=['get'])
def test():
    return "200"

@app.route('/detect',methods=['post'])
def predict():
    img_base=request.form.get("imgurl")
    img_bt=base64.b64decode(img_base)
    image_np = np.frombuffer(img_bt, dtype=np.uint8)
    image_np2 = cv2.imdecode(image_np, cv2.IMREAD_COLOR)
    image = cv2.cvtColor(image_np2, cv2.COLOR_RGB2BGR)

    predicts = model(image,conf=0.4)

    x = predicts[0].plot()
    data = cv2.imencode('.jpg', x)[1]
    image_bytes = data.tobytes()
    image_base4 = base64.b64encode(image_bytes).decode('utf8')
    result_list=[]
    for box in predicts[0].boxes:
        result_list.append({"cls":box.cls.item(), "conf":box.conf.item(), "xywh":box.xywh.cpu().numpy().tolist()})

    # mes={}
    # mes['imgBase64']=image_base4
    # mes['defections']=result_list
    # print(result_list[0]['cls'])
    # return json.dumps(mes,ensure_ascii=False)
    mes={}
    defections=[]
    cate={0.0:"开裂",1.0:"夹杂",2.0:"点蚀",3.0:"划痕",4.0:"斑块",5.0:"氧化"}
    for i in range(len(result_list)):
        res={}

        res["l"]=int(result_list[i].get("xywh")[0][2]*1000)/1000
        res["h"]=int(result_list[i].get("xywh")[0][3]*1000)/1000
        res["x"] = int(result_list[i].get("xywh")[0][0]*1000)/1000
        res["y"] = int(result_list[i].get("xywh")[0][1]*1000)/1000
        res["score"] = result_list[i].get("conf")
        res["category"] = cate[result_list[i].get("cls")]
        defections.append(res)
    mes['imgBase64'] = image_base4
    mes['defections']= defections
    return json.dumps(mes, ensure_ascii=False)



if __name__ == '__main__' :
    model = YOLOv10('v10best.pt')
    app.run(port=8090,host='0.0.0.0')