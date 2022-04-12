package com.ozz.disruptor;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyErrorEventHandler implements ExceptionHandler<MyEvent> {
    @Override
    public void handleEventException(Throwable ex, long sequence, MyEvent event) {
        log.error(String.format("event: %s, sequence: %s", event.getData(), sequence), ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        ExceptionUtil.wrapRuntime(ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        ExceptionUtil.wrapRuntime(ex);
    }
}
