package com.mokylin.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lip.li
 *
 */
public abstract class RequestHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

    /** 是否能启用 **/
    public boolean enable;

    /**是否单线程执行**/
    public boolean singleThread;
    /**消息间隔*/
    public int msgIntervalTime;

    public int msgCode;
    /**模块ID*/
    public int moduleId;

    protected RequestHandler() {


    }

    public abstract void handle(Object session, ChannelBuffer buff) throws Exception;


    @Override
    public String toString() {
        return "RequestMsgHandler [ singleThread=" + singleThread + ",HandlerName=" +
            this.getClass().getName() + "]";
    }
}
