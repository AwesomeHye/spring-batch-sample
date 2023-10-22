package dev.hyein.springbatchsample.lecture.chunk.itemReader;

import lombok.Setter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;

@Setter
public class DefaultLineMapper<T> implements LineMapper<T> {

    private LineTokenizer lineTokenizer;
    private FieldSetMapper<T> fieldSetMapper;

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        // line 을 fieldSet 으로 매핑해서 fieldSetMapper 에게 전달
        return fieldSetMapper.mapFieldSet(lineTokenizer.tokenize(line));
    }
}
