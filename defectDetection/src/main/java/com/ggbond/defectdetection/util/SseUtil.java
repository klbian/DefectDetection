package com.ggbond.defectdetection.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: 19461
 * Date: 2024/2/23
 */

@Slf4j
@Component
public class SseUtil {

    private static final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 创建连接
     */
    public SseEmitter connect(Long userId) {
        if (sseEmitterMap.containsKey(userId)) {
            SseEmitter sseEmitter =sseEmitterMap.get(userId);
            sseEmitterMap.remove(userId);
            sseEmitter.complete();
        }
        try {
            UUID uuid = UUID.randomUUID();
            String str = uuid.toString();
            String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);

            // 设置超时时间，0表示不过期。默认30秒
            SseEmitter sseEmitter = new SseEmitter(1500*1000L);
//            reconnectTime(10*1000L)
            // 注册回调
            sseEmitter.onCompletion(completionCallBack(userId));
//            sseEmitter.completeWithError(errorCallBack(userId));
            sseEmitter.onTimeout(timeoutCallBack(userId));
            sseEmitterMap.put(userId, sseEmitter);
            log.info("创建sse连接完成，当前用户：{}", userId);
            return sseEmitter;
        } catch (Exception e) {
            log.info("创建sse连接异常，当前用户：{}", userId);
        }
        return null;
    }

    /**
     * 给指定用户发送消息
     *
     */
    public boolean sendMessage(Long userId,String messageId, Object message) {
        if (sseEmitterMap.containsKey(userId)) {
            SseEmitter sseEmitter = sseEmitterMap.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().id(messageId).data(message));
//                reconnectTime(10*1000L)
//                log.info("用户{},消息id:{},推送成功:{}", userId,messageId, message.toString().substring(0,256));
                return true;
            }catch (Exception e) {
                sseEmitterMap.remove(userId);
//                log.info("用户{},消息id:{},推送异常:{}", userId,messageId, e.getMessage());
                sseEmitter.complete();
                return false;
            }
        }else {
            log.info("用户{}未上线", userId);
        }
        return false;
    }

    //给所有用户发送数据
    public Runnable sendMessageToAll(String messageId, Object message){
        return () -> {
            for(Long id:sseEmitterMap.keySet()){
                sendMessage(id,messageId,message);
            }
        };
    }


    /**
     * 删除连接
     * @param userId
     */
    public void deleteUser(Long userId){
        removeUser(userId);
    }

    private static Runnable completionCallBack(Long userId) {
        return () -> {
            log.info("结束sse用户连接：{}", userId);
            removeUser(userId);
        };
    }

    private static Throwable errorCallBack(Long userId) {
        log.info("sse用户连接异常：{}", userId);
        removeUser(userId);
        return new Throwable();
    }

    private static Runnable timeoutCallBack(Long userId) {
        return () -> {
            log.info("连接sse用户超时：{}", userId);
            removeUser(userId);
        };
    }

    /**
     * 断开
     * @param userId
     */
    public static void removeUser(Long userId){
        if (sseEmitterMap.containsKey(userId)) {
            SseEmitter sseEmitter = sseEmitterMap.get(userId);
            sseEmitterMap.remove(userId);
            sseEmitter.complete();
        }else {
            log.info("用户{} 连接已关闭",userId);
        }
    }

    public Map<Long, SseEmitter> listSseConnect(){
        return sseEmitterMap;
    }

    public boolean isEmpty(){
        return sseEmitterMap.isEmpty();
    }
}
