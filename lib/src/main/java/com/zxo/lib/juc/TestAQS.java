package com.zxo.lib.juc;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class TestAQS extends AbstractQueuedSynchronizer{


  @Override
  protected boolean tryAcquire(int arg) {
    return super.tryAcquire(arg);
  }

  @Override
  protected boolean tryRelease(int arg) {
    return super.tryRelease(arg);
  }

  @Override
  protected boolean isHeldExclusively() {
    return super.isHeldExclusively();
  }


}
