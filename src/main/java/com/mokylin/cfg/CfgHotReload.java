package com.mokylin.cfg;

import com.mokylin.util.CollectionUtil;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 配置文件热加载
 * Created by liweiping on 2018/6/7.
 */
public class CfgHotReload implements  Runnable {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CfgHotReload.class);

    private ScheduledExecutorService scheduledExecutor= Executors.newSingleThreadScheduledExecutor();

    private BaseCfgProviders prvds ;


    public CfgHotReload(BaseCfgProviders prvds){
        this.prvds=prvds;
        scheduledExecutor.scheduleWithFixedDelay(this,1*60*1000l,60*1000l, TimeUnit.MILLISECONDS);
    }


    @Override
    public void run() {
        File hotList=new File("config/reload_list.txt");
        if(hotList.exists()){
            try {
                List<String> files= FileUtils.readLines(hotList,"utf8");
                LOGGER.info("hot reload cfg start! {}",files);
                if(!CollectionUtil.isEmpty(files)){
                    String[] fileArray=new String[files.size()];
                    int index=0;
                    for(String fileName:files){
                        fileArray[index++]="config/"+fileName.trim()+".txt";
                    }
                    prvds.loadData(fileArray);
                }
                LOGGER.info("hot reload cfg end! {}",files);
                hotList.delete();

            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
        }

    }
}
