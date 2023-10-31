package dev.hyein.springbatchsample.lecture.chunk.itemprocessor.retry;

public class RetryableException extends Exception {

    public RetryableException(String msg) {
        super(msg);
    }
}
