import cv2
from PIL import Image
from ultralytics import YOLO
from cv2 import getTickCount, getTickFrequency

model=YOLO('best.pt')
# cap=cv2.VideoCapture(0)
# while cap.isOpened():
#     loop_start = getTickCount()
#     success, frame = cap.read()
#     if success:
#         results=model(source=frame,conf=0.25)
#     video=results[0].plot()
#
#     loop_time = getTickCount() - loop_start
#     total_time = loop_time / (getTickFrequency())
#     FPS = int(1 / total_time)
#     fps_text = f"FPS: {FPS:.2f}"
#     font = cv2.FONT_HERSHEY_SIMPLEX
#     font_scale = 1
#     font_thickness = 2
#     text_color = (0, 0, 255)  # 红色
#     text_position = (10, 30)
#
#     cv2.putText(video, fps_text, text_position, font, font_scale, text_color, font_thickness)
#     cv2.imshow('img', video)
#
#     if cv2.waitKey(1) & 0xFF == ord('q'):
#         break
# cap.release()  # 释放摄像头资源
# cv2.destroyAllWindows()  # 关闭OpenCV窗口
if __name__ == '__main__':
    validation_results = model.val(data="D:\paddledetection\PaddleDetection\dataset\my_yolov8/data.yml", imgsz=640, batch=1, conf=0.25, iou=0.6, device="cpu")