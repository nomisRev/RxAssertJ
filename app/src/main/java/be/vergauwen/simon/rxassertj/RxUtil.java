package be.vergauwen.simon.rxassertj;


import java.util.concurrent.Callable;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class RxUtil {

    public static Observable<Long> doSomeRxing() {
        return Observable.just(1L).observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable doSomeLongRxing() {
        return Completable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(2500);
                return null;
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Single<Long> getSomeSingleValue(final int n) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return RxUtil.fibonacci(n);
            }
        });
    }

    public static long fibonacci(final int n) throws InterruptedException {
        return n <= 1 ? n : fibonacci(n - 1) + fibonacci(n - 2);
    }

}
