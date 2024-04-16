package com.lib.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@ExtendWith(MockitoExtension.class)
class StopWatchTest {

  private static final String ID = "myId";
  private static final String name1 = "Task 1";
  private static final String name2 = "Task 2";
  private static final long duration1 = 200;
  private static final long duration2 = 100;
  private static final long fudgeFactor = 100;

  private final StopWatch stopWatch = new StopWatch(ID);

  @Test
  void failureToStartBeforeGettingLastTaskName() {
    assertThatIllegalStateException().isThrownBy(stopWatch::getLastTaskName);
  }

  @Test
  void failureToStartBeforeGettingLastInfo() {
    assertThatIllegalStateException().isThrownBy(stopWatch::getLastTaskInfo);
  }

  @Test
  void failureToStartBeforeGettingTimings() {
    assertThatIllegalStateException().isThrownBy(stopWatch::getLastTaskTimeMillis);
  }

  @Test
  void failureToStartBeforeGettingTimingsNanos() {
    assertThatIllegalStateException().isThrownBy(stopWatch::getLastTaskTimeNanos);
  }

  @Test
  void failureToStartBeforeStop() {
    assertThatIllegalStateException().isThrownBy(stopWatch::stop);
  }

  @Test
  void rejectsStartTwice() {
    stopWatch.start();
    assertThat(stopWatch.isRunning()).isTrue();
    stopWatch.stop();
    assertThat(stopWatch.isRunning()).isFalse();

    stopWatch.start();
    assertThat(stopWatch.isRunning()).isTrue();
    assertThatIllegalStateException().isThrownBy(stopWatch::start);
  }

  @Test
  void validUsage() {
    assertThat(stopWatch.isRunning()).isFalse();

    stopWatch.start(name1);
    await().pollDelay(duration1, TimeUnit.MILLISECONDS).until(() -> true);
    assertThat(stopWatch.isRunning()).isTrue();
    assertThat(stopWatch.currentTaskName()).isEqualTo(name1);
    stopWatch.stop();
    assertThat(stopWatch.isRunning()).isFalse();


    assertThat(stopWatch.getLastTaskTimeNanos())
      .as("last task time in nanoseconds for task #1")
      .isGreaterThanOrEqualTo(TimeUnit.MILLISECONDS.toNanos(duration1 - fudgeFactor))
      .isLessThanOrEqualTo(TimeUnit.MILLISECONDS.toNanos(duration1 + fudgeFactor));
    assertThat(stopWatch.getTotalTimeMillis())
      .as("total time in milliseconds for task #1")
      .isGreaterThanOrEqualTo(duration1 - fudgeFactor)
      .isLessThanOrEqualTo(duration1 + fudgeFactor);
    assertThat(stopWatch.getTotalTimeSeconds())
      .as("total time in seconds for task #1")
      .isGreaterThanOrEqualTo((duration1 - fudgeFactor) / 1000.0)
      .isLessThanOrEqualTo((duration1 + fudgeFactor) / 1000.0);

    assertThat(stopWatch.getLastTaskInfo()).isEqualTo(stopWatch.getTaskInfo()[0]);
    assertThat(stopWatch.getLastTaskName()).isEqualTo(name1);
    assertThat(stopWatch.getLastTaskTimeMillis()).isNotNegative();


    stopWatch.start(name2);
    await().pollDelay(duration2, TimeUnit.MILLISECONDS).until(() -> true);
    assertThat(stopWatch.isRunning()).isTrue();
    assertThat(stopWatch.currentTaskName()).isEqualTo(name2);
    stopWatch.stop();
    assertThat(stopWatch.isRunning()).isFalse();

    assertThat(stopWatch.getLastTaskTimeNanos())
      .as("last task time in nanoseconds for task #2")
      .isGreaterThanOrEqualTo(TimeUnit.MILLISECONDS.toNanos(duration2))
      .isLessThanOrEqualTo(TimeUnit.MILLISECONDS.toNanos(duration2 + fudgeFactor));
    assertThat(stopWatch.getTotalTimeMillis())
      .as("total time in milliseconds for tasks #1 and #2")
      .isGreaterThanOrEqualTo(duration1 + duration2 - fudgeFactor)
      .isLessThanOrEqualTo(duration1 + duration2 + fudgeFactor);
    assertThat(stopWatch.getTotalTimeSeconds())
      .as("total time in seconds for task #2")
      .isGreaterThanOrEqualTo((duration1 + duration2 - fudgeFactor) / 1000.0)
      .isLessThanOrEqualTo((duration1 + duration2 + fudgeFactor) / 1000.0);

    assertThat(stopWatch.getTaskCount()).isEqualTo(2);
    assertThat(stopWatch.prettyPrint()).contains(name1, name2);
    assertThat(stopWatch.getTaskInfo()).extracting(StopWatch.TaskInfo::getTaskName).containsExactly(name1, name2);
    assertThat(stopWatch.getTaskInfo().length).isEqualTo(2);

    assertThat(stopWatch.getTaskInfo().length).isEqualTo(2);
    assertThat(stopWatch.toString()).contains(ID, name1, name2);
    assertThat(stopWatch.getId()).isEqualTo(ID);
  }

  @Test
  void validUsageDoesNotKeepTaskList() {
    stopWatch.setKeepTaskList(false);

    stopWatch.start(name1);
    await().pollDelay(duration2, TimeUnit.MILLISECONDS).until(() -> true);
    assertThat(stopWatch.currentTaskName()).isEqualTo(name1);
    stopWatch.stop();

    stopWatch.start(name2);
    await().pollDelay(duration2, TimeUnit.MILLISECONDS).until(() -> true);
    assertThat(stopWatch.currentTaskName()).isEqualTo(name2);
    stopWatch.stop();

    assertThat(stopWatch.getTaskCount()).isEqualTo(2);
    assertThat(stopWatch.prettyPrint()).contains("No task info kept");
    assertThat(stopWatch.toString()).doesNotContain(name1, name2);
    assertThatExceptionOfType(UnsupportedOperationException.class)
      .isThrownBy(stopWatch::getTaskInfo)
      .withMessage("Task info is not being kept!");
  }

}
