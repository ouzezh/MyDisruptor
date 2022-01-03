package com.ozz.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

@Slf4j
public class MyErrorEventHandler implements ExceptionHandler<MyEvent> {
    @Override
    public void handleEventException(Throwable ex, long sequence, MyEvent event) {
        log.error(String.format("event: %s, sequence: %s", event.getData(), sequence), ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        ReflectionUtils.rethrowRuntimeException(ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        ReflectionUtils.rethrowRuntimeException(ex);
    }
}
