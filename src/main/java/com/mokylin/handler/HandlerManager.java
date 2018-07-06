package com.mokylin.handler;


import java.lang.instrument.IllegalClassFormatException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.handler.anno.Handler;
import com.mokylin.util.ClassUtil;

/**
 *
 * @author lip.li
 *
 */
public class HandlerManager {

    protected static final Logger LOGGER = LoggerFactory.getLogger(HandlerManager.class);

    private Map<Integer, RequestHandler> handlerMap;

    public HandlerManager() {
        handlerMap = new HashMap<Integer, RequestHandler>();

    }

    public void handle(Object session, ChannelBuffer request, int msgCode) throws Exception {
        RequestHandler handler = getHandler(msgCode);
        if (handler != null) {
            handler.handle(session, request);
        }
    }

    public RequestHandler getHandler(int msgCode) {
        RequestHandler requestMsgHandler = handlerMap.get(msgCode);
        if (requestMsgHandler == null) {
            LOGGER.error("can't find message handler :{}", msgCode);

        }
        return requestMsgHandler;
    }

    public Map<Integer, RequestHandler> getHandlerMap() {
        return handlerMap;
    }

    public void init(String handlerPack) throws Exception {

        List<Class<?>> classSet = ClassUtil.getClasses(handlerPack);
        if (CollectionUtils.isEmpty(classSet)) {
            LOGGER.info("Attention !!! no cmd path:{} " + handlerPack);
            return;
        }

        for (Class<?> clazz : classSet) {

            Handler handler = clazz.getAnnotation(Handler.class);
            if (null == handler) {
                continue;
            }

            RequestHandler requestHandler = initHandler(clazz, handler);
            if(handlerMap.containsKey(requestHandler.msgCode)){
                throw new IllegalClassFormatException(
                        "协议号已经被使用：code=" + requestHandler.msgCode + ",handler1=" + clazz.getName()+",hander2="+handlerMap.get(requestHandler.msgCode).getClass().getName());
            }else{
                handlerMap.put(requestHandler.msgCode, requestHandler);
            }

        }
    }

    private RequestHandler initHandler(Class<?> clazz, Handler handler) throws Exception {
        RequestHandler requestHandler = (RequestHandler) clazz.getConstructor().newInstance();
        requestHandler.singleThread = handler.singleThread();
        requestHandler.msgIntervalTime = handler.intervalTime();
        requestHandler.msgCode = handler.code();
        requestHandler.moduleId = handler.code() / 1000;
        requestHandler.needEnterScene=handler.needEnterScene();
        return requestHandler;
    }

    @Override
    public String toString() {
        return "RequestMsgHandlerAdapter [handlerMap=" + handlerMap + "]";
    }

    private static final HandlerManager ins = new HandlerManager();

    public static HandlerManager getInstance() {
        return ins;
    }

}
