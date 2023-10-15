package dev.hyein.springbatchsample.lecture.chunk;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;

public class CustomItemReader implements ItemReader<Customer> {
    private List<Customer> list;

    public CustomItemReader(List<Customer> list) {
        this.list = list;
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(!list.isEmpty()) {
            return list.remove(0); // 데이터 읽고 제거
        }
        return null;
    }
}
