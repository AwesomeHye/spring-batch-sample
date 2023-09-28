package dev.hyein.springbatchsample.lecture.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

public class CustomJobParameterValidator implements org.springframework.batch.core.JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if(parameters.getLong("seq") == null) {
            throw new JobParametersInvalidException("seq parameter is missing");
        }
    }
}
