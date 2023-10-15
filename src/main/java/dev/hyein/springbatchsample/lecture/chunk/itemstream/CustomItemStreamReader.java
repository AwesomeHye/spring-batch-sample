package dev.hyein.springbatchsample.lecture.chunk.itemstream;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;

public class CustomItemStreamReader implements ItemStreamReader<String> {

    private final List<String> items;
    private int index = -1;
    private boolean restart = false;

    public CustomItemStreamReader(List<String> items) {
        this.items = items;
        this.index = 0;
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String item = null;

        if(index < items.size()) {
            item = items.get(index);
            index++;
        }

        // 실패 조건
        if(this.index == 6 && !restart) {
            throw new RuntimeException("Restart is required");
        }

        return item;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if(executionContext.containsKey("index")) {
            // db 에 저장된 인덱스있으면 그 값으로 초기화 (재시작시 사용)
            index = executionContext.getInt("index");
            restart = true; // 이미 db 에 있는 값이면 재시작 맞음
        } else {
            // 처음 시작
            index = 0;
            executionContext.put("index", index);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // 실행된 인덱스 업데이트
        executionContext.put("index", index);
    }

    @Override
    public void close() throws ItemStreamException {
        // 예외 발생 혹은 잡 종료 시 리소스 닫기
        System.out.println("close CustomItemStreamReader");
    }
}
