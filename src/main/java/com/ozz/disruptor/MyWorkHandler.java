package com.ozz.disruptor;

import cn.hutool.log.StaticLog;
import com.lmax.disruptor.WorkHandler;

public class MyWorkHandler implements WorkHandler<MyEvent> {
    /**
     * 消费处理，每个事件只会被池中的一个任务处理器进行处理
     */
    @Override
    public void onEvent(MyEvent event) {
        StaticLog.info(String.format("event: %s", event.getData()));
    }
}
