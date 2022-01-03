package com.ozz.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class MyDisruptorTest {

    @Test
    void testDisruptor() {
        int ringBufferSize = 1024;
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ProducerType producerType = ProducerType.MULTI;
        WaitStrategy waitStrategy = new BlockingWaitStrategy();

        // 创建队列
        Disruptor<MyEvent> disruptor = new Disruptor<>(MyEvent::new, ringBufferSize, threadFactory, producerType, waitStrategy);

        // 消费者（每个消费者都是独立线程）
        MyEventHandler myEventHandler = new MyEventHandler();
        disruptor.handleEventsWith(myEventHandler, myEventHandler) // 启动两个线程，重复消费
                .thenHandleEventsWithWorkerPool(new MyWorkHandler(), new MyWorkHandler());// 启动两个线程，唯一消费

        // 消费者 异常处理
        disruptor.handleExceptionsFor(myEventHandler).with(new MyErrorEventHandler());

        // 启动队列
        disruptor.start();

        // 获取 ring buffer 用于生产消息
        RingBuffer<MyEvent> ringBuffer = disruptor.getRingBuffer();

        // 生产消息 方式一
        publish1(ringBuffer, 1L);
        publish1(ringBuffer, 2L);

        // 生产消息 方式二 可使用lambda写法生产消息
        publish2(ringBuffer, 3L);
        publish2(ringBuffer, 4L);

        // 生产消息 方式三 与方式二相比EventTranslatorOneArg可复用只需new一次即可
        EventTranslatorOneArg<MyEvent, Long> et = (event, seq, data1) -> event.setData(data1);
        publish3(ringBuffer, et, 5L);
        publish3(ringBuffer, et, 6L);

        Assertions.assertTrue(disruptor.getCursor() > 0);
        disruptor.shutdown();
    }

    private void publish1(RingBuffer<MyEvent> ringBuffer, Long data) {
        // Increment and return the next sequence for the ring buffer.
        // Calls of this method should ensure that they always publish the sequence afterward.
        long sequence = ringBuffer.next();
        try {
            MyEvent event = ringBuffer.get(sequence);
            event.setData(data);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    private void publish2(RingBuffer<MyEvent> ringBuffer, Long data) {
        EventTranslator<MyEvent> et = (event, seq) -> event.setData(data);
        ringBuffer.publishEvent(et);
    }

    private void publish3(RingBuffer<MyEvent> ringBuffer, EventTranslatorOneArg<MyEvent, Long> et, Long data) {
        ringBuffer.publishEvent(et, data);
    }

}