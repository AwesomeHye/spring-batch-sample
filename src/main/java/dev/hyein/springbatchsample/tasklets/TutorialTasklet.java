package dev.hyein.springbatchsample.tasklets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class TutorialTasklet implements Tasklet, StepExecutionListener {

    @Override // for Tasklet
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("tasklet 너 뭐야! {}, {}", contribution, chunkContext);
        return RepeatStatus.FINISHED;
    }

    @Override // for StepExecutionListener
    public void beforeStep(StepExecution stepExecution) {
        log.info("tasklet 시작 전");
    }

    @Override // for StepExecutionListener
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("tasklet 끝나고");
        return ExitStatus.COMPLETED;
    }
}
