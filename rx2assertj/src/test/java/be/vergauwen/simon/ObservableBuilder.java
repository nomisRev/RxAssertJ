package be.vergauwen.simon;


import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public final class ObservableBuilder {

    public Observable<Long> doSomeRxing() {
        return Observable.just(1L);
    }

    public Flowable<Integer> getFlowableIntegers() {
        Integer[] integers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        return Flowable.fromArray(integers);
    }

    public Maybe<Integer> getEmptyMaybe() {
        return Maybe.empty();
    }

    public Completable doSomeLongRxing() {
        return Completable.fromCallable(() -> {
            return null;
        });
    }

    public Single<Long> getSomeSingleValue(final int n) {
        return Single.fromCallable(() -> fibonacci(n));
    }

    public long fibonacci(final int n) throws InterruptedException {
        return n <= 1 ? n : fibonacci(n - 1) + fibonacci(n - 2);
    }

}