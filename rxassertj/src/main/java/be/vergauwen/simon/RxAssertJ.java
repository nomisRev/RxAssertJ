package be.vergauwen.simon;

import org.assertj.core.api.AbstractObjectAssert;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.observables.BlockingObservable;
import rx.observers.TestSubscriber;

public final class RxAssertJ {
    private RxAssertJ(){ }


    public static <T> TestSubscriberAssert<T> assertThat(final TestSubscriber<T> subscriber) {
        return new TestSubscriberAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThatASubscriberTo(final Observable<T> observable) {
        TestSubscriber<T> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);
        return new TestSubscriberAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThatASubscriberTo(final BlockingObservable<T> observable) {
        TestSubscriber<T> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);
        return new TestSubscriberAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThatASubscriberTo(final Completable completable) {
        TestSubscriber<T> subscriber = new TestSubscriber<>();
        completable.subscribe(subscriber);
        return new TestSubscriberAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThatASubscriberTo(final Single<T> single) {
        TestSubscriber<T> subscriber = new TestSubscriber<>();
        single.subscribe(subscriber);
        TestSubscriberAssert<T> testSubscriberAssert = new TestSubscriberAssert<>(subscriber);
        testSubscriberAssert.hasReceivedCount(1);
        return testSubscriberAssert;
    }


    static class TestSubscriberAssert<T> extends AbstractObjectAssert<TestSubscriberAssert<T>, TestSubscriber<T>> {

        public TestSubscriberAssert(final TestSubscriber<T> actual) {
            super(actual, TestSubscriberAssert.class);
        }

        /**
         * Asserts that this {@code Subscriber} is unsubscribed.
         *
         * @throws AssertionError if this {@code Subscriber} is not unsubscribed
         */
        public TestSubscriberAssert<T> isUnsubscribed() {
            actual.assertUnsubscribed();
            return this;
        }

        /**
         * Asserts that the received onNext events, in order, are the specified items.
         *
         * @param values the items to check
         * @throws AssertionError if the items emitted do not exactly match those specified by {@code values}
         */
        public TestSubscriberAssert<T> hasReceived(final T... values) {
            actual.assertValues(values);
            return this;
        }

        public TestSubscriberAssert<T> hasReceivedCount(final int count) {
            actual.assertValueCount(count);
            return this;
        }

        /**
         * Asserts that there are no onNext events received.
         *
         * @throws AssertionError if there were any onNext events
         */
        public TestSubscriberAssert<T> receivedNothing() {
            actual.assertNoValues();
            return this;
        }

        /**
         * Asserts that there is exactly one completion event.
         *
         * @throws AssertionError if there were zero, or more than one, onCompleted events
         */
        public TestSubscriberAssert<T> isCompleted() {
            actual.assertCompleted();
            return this;
        }

        /**
         * Asserts that there is no completion event.
         *
         * @throws AssertionError if there were one or more than one onCompleted events
         */
        public TestSubscriberAssert<T> isNotCompleted() {
            actual.assertNotCompleted();
            return this;
        }

        /**
         * Asserts that this {@code Subscriber} has received no {@code onError} notifications.
         *
         * @throws AssertionError if this {@code Subscriber} has received one or more {@code onError} notifications
         */
        public TestSubscriberAssert<T> hasNoErrors() {
            actual.assertNoErrors();
            return this;
        }

        /**
         * Asserts that there is exactly one error event which is a subclass of the given class.
         *
         * @param clazz the class to check the error against.
         * @throws AssertionError if there were zero, or more than one, onError events, or if the single onError event did not carry an error of a subclass of the given class
         */
        public TestSubscriberAssert<T> hasError(final Class<? extends Throwable> clazz) {
            actual.assertError(clazz);
            return this;
        }
    }
}