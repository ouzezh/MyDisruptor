package com.ozz.disruptor;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.log.StaticLog;
import com.lmax.disruptor.ExceptionHandler;

public class MyErrorEventHandler implements ExceptionHandler<MyEvent> {
    @Override
    public void handleEventException(Throwable ex, long sequence, MyEvent event) {
        StaticLog.error(String.format("event: %s, sequence: %s", event.getData(), sequence), ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        ExceptionUtil.wrapAndThrow(ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        ExceptionUtil.wrapAndThrow(ex);
    }
}
