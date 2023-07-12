package dev.hyein.springbatchsample.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TutorialScheduler {
    private final Job job;
    private final JobLauncher jobLauncher;

    @Scheduled(fixedDelay = 5000L)
    public void runJob() throws Exception {
        jobLauncher.run(job, new JobParametersBuilder().addString("datetime", LocalDateTime.now().toString()).toJobParameters());// jobParameter : job ID
    }
}
