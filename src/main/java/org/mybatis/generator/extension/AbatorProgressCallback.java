package org.mybatis.generator.extension;

import org.mybatis.generator.api.ProgressCallback;

/**
 * @author xiebiao
 */
public class AbatorProgressCallback implements ProgressCallback {
  private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(this.getClass());

  @Override
  public void introspectionStarted(int totalTasks) {
    LOG.debug("introspectionStarted call");
  }

  @Override
  public void generationStarted(int totalTasks) {
    LOG.debug("generationStarted call");
  }

  @Override
  public void saveStarted(int totalTasks) {
    LOG.debug("saveStarted call");
  }

  @Override
  public void startTask(String taskName) {
    LOG.debug("startTask call");
  }

  @Override
  public void done() {
    LOG.debug("done call");
  }

  @Override
  public void checkCancel() throws InterruptedException {
    LOG.debug("checkCancel call");
  }
}
