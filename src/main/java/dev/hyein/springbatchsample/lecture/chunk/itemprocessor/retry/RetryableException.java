package dev.hyein.springbatchsample.lecture.chunk.itemprocessor.retry;

public class RetryableException extends RuntimeException {

    public RetryableException(String msg) {
        super(msg);
    }
}
