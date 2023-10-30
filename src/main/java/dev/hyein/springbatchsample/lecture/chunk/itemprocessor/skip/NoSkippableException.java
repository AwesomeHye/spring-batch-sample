package dev.hyein.springbatchsample.lecture.chunk.itemprocessor.skip;

public class NoSkippableException extends Exception {

    public NoSkippableException(String msg) {
        super(msg);

    }
}
