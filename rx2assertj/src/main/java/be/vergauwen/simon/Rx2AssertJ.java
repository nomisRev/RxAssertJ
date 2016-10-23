package be.vergauwen.simon;

import org.assertj.core.api.AbstractObjectAssert;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

public final class Rx2AssertJ {
    private Rx2AssertJ(){ }


    public static <T> TestObserverAssert<T> assertThat(final TestObserver<T> subscriber) {
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThat(final TestSubscriber<T> observer) {
        return new TestSubscriberAssert<>(observer);
    }

    public static <T> TestObserverAssert<T> assertThatASubscriberTo(final Observable<T> observable) {
        TestObserver<T> subscriber = new TestObserver<>();
        observable.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThatASubscriberTo(final Completable completable) {
        TestObserver<T> subscriber = new TestObserver<>();
        completable.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThatASubscriberTo(final Single<T> single) {
        TestObserver<T> subscriber = new TestObserver<>();
        single.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestObserverAssert<T> assertThatASubscriberTo(final Maybe<T> maybe) {
        TestObserver<T> subscriber = new TestObserver<>();
        maybe.subscribe(subscriber);
        return new TestObserverAssert<>(subscriber);
    }

    public static <T> TestSubscriberAssert<T> assertThatASubscriberTo(final Flowable<T> flowable) {
        TestSubscriber<T> subscriber = new TestSubscriber<>();
        flowable.subscribe(subscriber);
        TestSubscriberAssert<T> testSubscriberAssert = new TestSubscriberAssert<>(subscriber);
        return testSubscriberAssert;
    }

    static class TestObserverAssert<T> extends AbstractObjectAssert<TestObserverAssert<T>, TestObserver<T>> {

        public TestObserverAssert(final TestObserver<T> actual) {
            super(actual, TestObserverAssert.class);
        }


        /**
         * Assert that the onSubscribe method was called exactly once.
         * @throws AssertionError if this {@code Subscriber} is not unsubscribed
         */
        public TestObserverAssert<T> isSubscribed() {
            actual.assertSubscribed();
            return this;
        }

        /**
         * Asserts that this {@code Subscriber} is unsubscribed.
         * @throws AssertionError if this {@code Subscriber} is not unsubscribed
         */
        public TestObserverAssert<T> isNotUnsubscribed() {
            actual.assertNotSubscribed();
            return this;
        }

        /**
         * Asserts that the received onNext events, in order, are the specified items.
         * @param values the items to check
         * @throws AssertionError if the items emitted do not exactly match those specified by {@code values}
         */
        @SafeVarargs
        public final TestObserverAssert<T> hasReceived(final T... values) {
            actual.assertValues(values);
            return this;
        }

        public TestObserverAssert<T> hasReceivedCount(final int count) {
            actual.assertValueCount(count);
            return this;
        }

        /**
         * Asserts that there are no onNext events received.
         * @throws AssertionError if there were any onNext events
         */
        public TestObserverAssert<T> receivedNothing() {
            actual.assertNoValues();
            return this;
        }

        /**
         * Asserts that there is exactly one completion event.
         * @throws AssertionError if there were zero, or more than one, onCompleted events
         */
        public TestObserverAssert<T> isComplete() {
            actual.assertComplete();
            return this;
        }

        /**
         * Asserts that there is no completion event.
         * @throws AssertionError if there were one or more than one onCompleted events
         */
        public TestObserverAssert<T> isNotComplete() {
            actual.assertNotComplete();
            return this;
        }

        /**
         * Assert that the TestObserver/TestSubscriber terminated (i.e., the terminal latch reached zero).
         * @throws AssertionError if there were one or more than one onCompleted events
         */
        public TestObserverAssert<T> isTerminated() {
            actual.assertTerminated();
            return this;
        }

        /**
         * Assert that the TestObserver/TestSubscriber has not terminated (i.e., the terminal latch is still non-zero).
         * @throws AssertionError if there were one or more than one onCompleted events
         */
        public TestObserverAssert<T> isNotTerminated() {
            actual.assertNotTerminated();
            return this;
        }

        /**
         * Asserts that this {@code Subscriber} has received no {@code onError} notifications.
         * @throws AssertionError if this {@code Subscriber} has received one or more {@code onError} notifications
         */
        public TestObserverAssert<T> hasNoErrors() {
            actual.assertNoErrors();
            return this;
        }

        /**
         * Asserts that there is exactly one error event which is a subclass of the given class.
         * @param clazz the class to check the error against.
         * @throws AssertionError if there were zero, or more than one, onError events, or if the single onError event did not carry an error of a subclass of the given class
         */
        public TestObserverAssert<T> hasError(final Class<? extends Throwable> clazz) {
            actual.assertError(clazz);
            return this;
        }
    }
    static class TestSubscriberAssert<T> extends AbstractObjectAssert<TestSubscriberAssert<T>, TestSubscriber<T>> {

        public TestSubscriberAssert(final TestSubscriber<T> actual) {
            super(actual, TestSubscriberAssert.class);
        }

        /**
         * Asserts that this {@code Subscriber} is unsubscribed.
         * @throws AssertionError if this {@code Subscriber} is not unsubscribed
         */
        public TestSubscriberAssert<T> isNotUnsubscribed() {
            actual.assertNotSubscribed();
            return this;
        }

        /**
         * Asserts that the received onNext events, in order, are the specified items.
         * @param values the items to check
         * @throws AssertionError if the items emitted do not exactly match those specified by {@code values}
         */
        @SafeVarargs
        public final TestSubscriberAssert<T> hasReceived(final T... values) {
            actual.assertValues(values);
            return this;
        }

        public TestSubscriberAssert<T> hasReceivedCount(final int count) {
            actual.assertValueCount(count);
            return this;
        }

        /**
         * Asserts that there are no onNext events received.
         * @throws AssertionError if there were any onNext events
         */
        public TestSubscriberAssert<T> receivedNothing() {
            actual.assertNoValues();
            return this;
        }

        /**
         * Asserts that there is exactly one completion event.
         * @throws AssertionError if there were zero, or more than one, onCompleted events
         */
        public TestSubscriberAssert<T> isComplete() {
            actual.assertComplete();
            return this;
        }

        /**
         * Asserts that there is no completion event.
         * @throws AssertionError if there were one or more than one onCompleted events
         */
        public TestSubscriberAssert<T> isNotComplete() {
            actual.assertNotComplete();
            return this;
        }

        /**
         * Assert that the TestObserver/TestSubscriber terminated (i.e., the terminal latch reached zero).
         * @throws AssertionError if there were one or more than one onCompleted events
         */
        public TestSubscriberAssert<T> isTerminated() {
            actual.assertTerminated();
            return this;
        }

        /**
         * Assert that the TestObserver/TestSubscriber has not terminated (i.e., the terminal latch is still non-zero).
         * @throws AssertionError if there were one or more than one onCompleted events
         */
        public TestSubscriberAssert<T> isNotTerminated() {
            actual.assertNotTerminated();
            return this;
        }

        /**
         * Asserts that this {@code Subscriber} has received no {@code onError} notifications.
         * @throws AssertionError if this {@code Subscriber} has received one or more {@code onError} notifications
         */
        public TestSubscriberAssert<T> hasNoErrors() {
            actual.assertNoErrors();
            return this;
        }

        /**
         * Asserts that there is exactly one error event which is a subclass of the given class.
         * @param clazz the class to check the error against.
         * @throws AssertionError if there were zero, or more than one, onError events, or if the single onError event did not carry an error of a subclass of the given class
         */
        public TestSubscriberAssert<T> hasError(final Class<? extends Throwable> clazz) {
            actual.assertError(clazz);
            return this;
        }
    }
}