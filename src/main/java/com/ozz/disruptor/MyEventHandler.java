package com.ozz.disruptor;

import cn.hutool.log.StaticLog;
import com.lmax.disruptor.EventHandler;

public class MyEventHandler implements EventHandler<MyEvent> {
    /**
     * 消息处理，多个Event订阅会重复消费数据
     * @param event published to the RingBuffer
     * @param sequence of the event being processed
     * @param endOfBatch flag to indicate if this is the last event in a batch from the RingBuffer
     */
    @Override
    public void onEvent(MyEvent event, long sequence, boolean endOfBatch) {
        StaticLog.info(String.format("event: %s, sequence: %s, endOfBatch: %s", event.getData(), sequence, endOfBatch));
    }
}
