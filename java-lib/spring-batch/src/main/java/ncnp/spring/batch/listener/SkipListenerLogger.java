package ncnp.spring.batch.listener;

import org.springframework.batch.core.SkipListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkipListenerLogger<I,O> implements SkipListener<I, O> {

  @Override
  public void onSkipInProcess(I value, Throwable error) {
    log.warn(String.format("Skiping %s because of:", value), error);
  }

  @Override
  public void onSkipInRead(Throwable error) {
    log.warn("Skip the reading because of " + error.getMessage(), error);
  }

  @Override
  public void onSkipInWrite(O value, Throwable error) {
    log.warn(String.format("Skip the writing of %s because of:", value), error);
  }
}
