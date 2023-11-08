package dev.hyein.springbatchsample.lecture.chunk.itemprocessor.retry;

import dev.hyein.springbatchsample.lecture.chunk.Customer;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.classify.Classifier;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryTemplateItemProcessor implements ItemProcessor<String, Customer> {
    private final RetryTemplate retryTemplate;

    private int cnt = 0;


    @Override
    public Customer process(String item) throws Exception {

        // 예외애 따라 롤백 할건지 말건지 설정
        Classifier<Throwable, Boolean> rollbackClassifier = new BinaryExceptionClassifier(true); // 무조건 true 반환해서 롤백 실행

        Customer customer = retryTemplate.execute(new RetryCallback<Customer, RuntimeException>() {
            @Override
            public Customer doWithRetry(RetryContext context) throws RuntimeException {

                if(item.equals("1") || item.equals("2")) {
                    cnt++;
                    throw new RetryableException("failed cnt: " + cnt++);
                }
                return new Customer(item);
            }
        }, new RecoveryCallback<Customer>() {
            @Override
            public Customer recover(RetryContext context) throws Exception {
                return new Customer(item);
            }
        }, new DefaultRetryState(item, rollbackClassifier));

        return customer;
    }

}
