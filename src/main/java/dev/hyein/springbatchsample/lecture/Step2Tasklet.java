package dev.hyein.springbatchsample.lecture;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class Step2Tasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Step2 executed");

        ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        if(jobExecutionContext.get("jobNam") == null) {
            // 한번도 이 이름으로 잡 실행한적 없는 경우
            jobExecutionContext.put("jobName", jobName);
        }

        ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();
        String stepName = chunkContext.getStepContext().getStepExecution().getStepName();
        if(stepExecutionContext.get("stepName") == null) {
            // 한번도 이 이름으로 스텝 실행한적 없는 경우
            stepExecutionContext.put("stepName", stepName);
        }

        return RepeatStatus.FINISHED;
    }
}
