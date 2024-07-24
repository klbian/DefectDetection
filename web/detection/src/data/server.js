// server.js
const express = require('express');
const fs = require('fs');
const path = require('path');

const app = express();

app.get('/sse', (req, res) => {
    res.setHeader('Content-Type', 'text/event-stream');
    res.setHeader('Cache-Control', 'no-cache');
    res.setHeader('Connection', 'keep-alive');

    let imageCounter = 0;
    const imageDir = "../src/data/images"; // 图片存储目录

    const interval = setInterval(() => {
        if (imageCounter < 3) { // 假设发送5张图片
            const imageFileName = `image${imageCounter + 1}.jpg`;
            const imagePath = path.join(__dirname, imageDir, imageFileName);
            const imageData = fs.readFileSync(imagePath, 'base64'); // 读取并以Base64编码返回图片数据
            const imageEvent = `data:image/jpeg;base64,${imageData}`;
            res.write(`data: ${imageEvent}\n\n`); // 发送图片数据
            imageCounter++;
        } else {
            clearInterval(interval); // 发送完毕后停止发送
            res.end();
        }
    }, 2000);

    // 当客户端断开连接时，停止发送事件
    req.on('close', () => {
        console.log('Client connection closed');
        clearInterval(interval);
        res.end();
    });
});
app.listen(3001, () => {
    console.log('SSE server is running on http://localhost:3001');
});