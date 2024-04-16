package ncnp.spring.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ExecutionListenerLogger implements JobExecutionListener {

  private static final Logger log = LoggerFactory.getLogger(
    ExecutionListenerLogger.class
  );

  @Override
  public void afterJob(JobExecution execution) {
    log.debug(
      "After the execution of job {}",
      execution.getJobInstance().getJobName()
    );
  }

  @Override
  public void beforeJob(JobExecution execution) {
    log.debug(
      "Before the execution of job {}",
      execution.getJobInstance().getJobName()
    );
  }
}
