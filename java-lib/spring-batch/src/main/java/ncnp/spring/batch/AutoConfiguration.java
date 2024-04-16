package ncnp.spring.batch;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ncnp.spring.batch.listener.ExecutionListenerLogger;

@Configuration
@ComponentScan(
  basePackageClasses = {
    ExecutionListenerLogger.class
  }
)
public class AutoConfiguration {
  
}
