package dev.hyein.springbatchsample.lecture.listener.multithreadstep;

import dev.hyein.springbatchsample.lecture.chunk.Customer;
import org.springframework.batch.core.ItemReadListener;

public class MultiThreadItemReadListener implements ItemReadListener<Customer> {

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(Customer item) {
        // 어떤 스레드가 읽었는지, 읽은 ID 출력
        System.out.println("Thread: " + Thread.currentThread().getName() + ", read item: " + item.getId());
    }

    @Override
    public void onReadError(Exception ex) {

    }
}
