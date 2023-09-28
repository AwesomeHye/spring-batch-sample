package dev.hyein.springbatchsample.lecture.incrementer;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomJobParametersIncrementer implements
    org.springframework.batch.core.JobParametersIncrementer {

    @Override
    public JobParameters getNext(JobParameters parameters) {
        Double weight = parameters.getDouble("weight");
        String id = LocalDateTime.now().toString();
        return new JobParametersBuilder().addString("run.id", id).toJobParameters();
    }
}
