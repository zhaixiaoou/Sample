package com.zxo.lib.rxjava;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class TestRxJava {
  @SuppressWarnings("CheckResult")
  public static void main(String[] args) {
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < 10; i++) {
      list.add("Hello" + i);
    }
    Observable<String> observable = Observable.fromIterable(list);

    observable = observable.map(new Function<String, String>() {
      @Override
      public String apply(@NonNull String s) throws Exception {
        if (s.equals("Hello4")) {
          throw new Exception("转换错误");
        }
        return s;
      }
    });

    observable = observable.retryWhen(new RetryObserver(1));

    Observer<String> observer = new Observer<String>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        System.out.println("onSubscribe ");
      }

      @Override
      public void onNext(@NonNull String s) {
        System.out.println("onNext s=" + s);
      }

      @Override
      public void onError(@NonNull Throwable e) {
        System.out.println("onError ");
        e.printStackTrace();
      }

      @Override
      public void onComplete() {
        System.out.println("onComplete ");
      }
    };

    observable.subscribe(observer);
  }

  static class RetryObserver implements Function<Observable<Throwable>, ObservableSource<?>> {
    private int maxCount = 1;
    private int retryCount ;
    public RetryObserver(int count){
      this.maxCount = count;
    }

    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> attempts) throws Exception {

      return attempts.flatMap((Throwable throwable) -> {
        retryCount++;
        if (retryCount <= maxCount){
          System.out.println("重试了");
          return Observable.just("");
        }
        return Observable.error(throwable);

      });

    }
  }

}
