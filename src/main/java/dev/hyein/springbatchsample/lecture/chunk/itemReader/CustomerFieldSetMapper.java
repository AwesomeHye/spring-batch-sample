package dev.hyein.springbatchsample.lecture.chunk.itemReader;

import dev.hyein.springbatchsample.lecture.chunk.Customer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {

    @Override
    public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
        // lineMapper 에서 전달해준 fieldSet 을 Customer 객체로 매핑
        if(fieldSet == null) {
            return null;
        }

        return new Customer(0L, fieldSet.readString("name"), fieldSet.readInt("age"), fieldSet.readString("year"));
    }
}
