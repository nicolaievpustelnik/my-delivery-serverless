package ncnp.spring.batch.listener;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemListenerSupport;

@Slf4j
public class FailureListenerLogger<I, O> extends ItemListenerSupport<I, O> {

  @Override
  public void onReadError(Exception cause) {
    log.error("Encountered error on read", cause);
  }

  @Override
  public void onWriteError(Exception cause, List<? extends O> items) {
    log.error("Encountered error on write", cause);
  }
}
