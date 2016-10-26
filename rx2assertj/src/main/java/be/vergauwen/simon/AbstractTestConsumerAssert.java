package be.vergauwen.simon;


import org.assertj.core.api.AbstractObjectAssert;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Predicate;
import io.reactivex.observers.BaseTestConsumer;

public abstract class AbstractTestConsumerAssert<T, P extends BaseTestConsumer<T, P>> extends AbstractObjectAssert<AbstractTestConsumerAssert<T, P>, P> {

    public AbstractTestConsumerAssert(final P actual) {
        super(actual, AbstractTestConsumerAssert.class);
    }

    /**
     * Assert that this TestObserver/TestSubscriber received exactly one onComplete event.
     * @return this;
     */
    public final AbstractTestConsumerAssert<T, P> isComplete() {
        actual.assertComplete();
        return this;
    }

    /**
     * Assert that this TestObserver/TestSubscriber has not received any onComplete event.
     * @return this;
     */
    public final AbstractTestConsumerAssert<T, P> isNotComplete() {
        actual.assertNotComplete();
        return this;
    }

    /**
     * Assert that this TestObserver/TestSubscriber has not received any onError event.
     * @return this;
     */
    public final AbstractTestConsumerAssert<T, P> hasNoErrors() {
        actual.assertNoErrors();
        return this;
    }

    /**
     * Assert that this TestObserver/TestSubscriber received exactly the specified onError event value. The comparison is performed via Objects.equals(); since most exceptions
     * don't implement equals(), this assertion may fail. Use the {@link #hasError(Class)} overload to test against the class of an error instead of an instance of an error or
     * {@link #hasError(Predicate)} to test with different condition.
     * @param error the error to check
     * @return this;
     * @see #hasError(Class)
     * @see #hasError(Predicate)
     */
    public final AbstractTestConsumerAssert<T, P> hasError(Throwable error) {
        actual.assertError(error);
        return this;
    }

    /**
     * Asserts that this TestObserver/TestSubscriber received exactly one onError event which is an instance of the specified errorClass class.
     * @param errorClass the error class to expect
     * @return this;
     */
    @SuppressWarnings({ "unchecked", "rawtypes", "cast" })
    public final AbstractTestConsumerAssert<T, P> hasError(Class<? extends Throwable> errorClass) {
        actual.assertError(errorClass);
        return this;
    }

    /**
     * Asserts that this TestObserver/TestSubscriber received exactly one onError event for which the provided predicate returns true.
     * @param errorPredicate the predicate that receives the error Throwable and should return true for expected errors.
     * @return this
     */
    public final AbstractTestConsumerAssert<T, P> hasError(Predicate<Throwable> errorPredicate) {
        actual.assertError(errorPredicate);
        return this;
    }

    /**
     * Assert that this TestObserver/TestSubscriber received exactly one onNext value which is equal to the given value with respect to Objects.equals.
     * @param value the value to expect
     * @return this;
     */
    public final AbstractTestConsumerAssert<T, P> hasValue(T value) {
        actual.assertValue(value);
        return this;
    }

    /**
     * Asserts that this TestObserver/TestSubscriber received exactly one onNext value for which the provided predicate returns true.
     * @param valuePredicate the predicate that receives the onNext value and should return true for the expected value.
     * @return this
     */
    public final AbstractTestConsumerAssert<T, P> hasValue(Predicate<T> valuePredicate) {
        actual.assertValue(valuePredicate);
        return this;
    }

    /**
     * Asserts that this TestObserver/TestSubscriber received an onNext value at the given index for the provided predicate returns true.
     * @param index the position to assert on
     * @param valuePredicate the predicate that receives the onNext value and should return true for the expected value.
     * @return this
     */
    public final AbstractTestConsumerAssert<T, P> hasValueAt(int index, Predicate<T> valuePredicate) {
        actual.assertValueAt(index, valuePredicate);
        return this;
    }

    /**
     * Assert that this TestObserver/TestSubscriber received the specified number onNext events.
     * @param count the expected number of onNext events
     * @return this;
     */
    public final AbstractTestConsumerAssert<T, P> hasValueCount(int count) {
        actual.assertValueCount(count);
        return this;
    }

    /**
     * Assert that this TestObserver/TestSubscriber has not received any onNext events.
     * @return this;
     */
    public final AbstractTestConsumerAssert<T, P> hasNoValues() {
        actual.assertValueCount(0);
        return this;
    }

    /**
     * Assert that the TestObserver/TestSubscriber received only the specified values in the specified order.
     * @param values the values expected
     * @return this;
     * @see #hasValueSet(Collection)
     */
    @SuppressWarnings("unchecked")
    public final AbstractTestConsumerAssert<T, P> hasValues(T... values) {
        actual.assertValues(values);
        return this;
    }

    /**
     * Assert that the TestObserver/TestSubscriber received only the specified values in any order. This helps asserting when the order of the values is not guaranteed, i.e.,
     * when merging asynchronous streams.
     *
     * @param expected the collection of values expected in any order
     * @return this;
     */
    @SuppressWarnings("unchecked")
    public final AbstractTestConsumerAssert<T, P> hasValueSet(Collection<? extends T> expected) {
        actual.assertValueSet(expected);
        return this;
    }

    /**
     * Assert that the TestObserver/TestSubscriber received only the specified sequence of values in the same order.
     * @param sequence the sequence of expected values in order
     * @return this;
     */
    public final AbstractTestConsumerAssert<T, P> hasValueSequence(Iterable<? extends T> sequence) {
        actual.assertValueSequence(sequence);
        return this;
    }

    /**
     * Assert that the TestObserver/TestSubscriber terminated (i.e., the terminal latch reached zero).
     * @return this;
     */
    public final AbstractTestConsumerAssert<T, P> isTerminated() {
        actual.assertTerminated();
        return this;
    }

    /**
     * Assert that the TestObserver/TestSubscriber has not terminated (i.e., the terminal latch is still non-zero).
     * @return this;
     */
    public final AbstractTestConsumerAssert<T, P> isNotTerminated() {
        actual.assertNotTerminated();
        return this;
    }

    /**
     * Assert that there is a single error and it has the given message.
     * @param message the message expected
     * @return this
     */
    public final AbstractTestConsumerAssert<T, P> hasErrorMessage(String message) {
        actual.assertErrorMessage(message);
        return this;
    }

    /**
     * Assert that the onSubscribe method was called exactly once.
     * @return this;
     */
    public abstract AbstractTestConsumerAssert<T, P> isSubscribed();

    /**
     * Assert that the onSubscribe method hasn't been called at all.
     * @return this;
     */
    public abstract AbstractTestConsumerAssert<T, P> isNotSubscribed();

    /**
     * Assert that the upstream signalled the specified values in order and
     * completed normally.
     * @param values the expected values, asserted in order
     * @return this
     * @see #hasFailure(Class, Object...)
     * @see #hasFailure(Predicate, Object...)
     * @see #hasFailureAndMessage(Class, String, Object...)
     */
    public final AbstractTestConsumerAssert<T, P> hasResult(T... values) {
        actual.assertSubscribed()
                .assertValues(values)
                .assertNoErrors()
                .assertComplete();
        return this;
    }

    /**
     * Assert that the upstream signalled the specified values in order
     * and then failed with a specific class or subclass of Throwable.
     * @param error the expected exception (parent) class
     * @param values the expected values, asserted in order
     * @return this
     */
    public final AbstractTestConsumerAssert<T, P> hasFailure(Class<? extends Throwable> error, T... values) {
        actual.assertSubscribed()
                .assertValues(values)
                .assertError(error)
                .assertNotComplete();
        return this;
    }

    /**
     * Assert that the upstream signalled the specified values in order and then failed
     * with a Throwable for which the provided predicate returns true.
     * @param errorPredicate
     *            the predicate that receives the error Throwable
     *            and should return true for expected errors.
     * @param values the expected values, asserted in order
     * @return this
     */
    public final AbstractTestConsumerAssert<T, P> hasFailure(Predicate<Throwable> errorPredicate, T... values) {
        actual.assertSubscribed()
                .assertValues(values)
                .assertError(errorPredicate)
                .assertNotComplete();
        return this;
    }

    /**
     * Assert that the upstream signalled the specified values in order,
     * then failed with a specific class or subclass of Throwable
     * and with the given exact error message.
     * @param error the expected exception (parent) class
     * @param message the expected failure message
     * @param values the expected values, asserted in order
     * @return this
     */
    public final AbstractTestConsumerAssert<T, P> hasFailureAndMessage(Class<? extends Throwable> error, String message, T... values) {
        actual.assertSubscribed()
                .assertValues(values)
                .assertError(error)
                .assertErrorMessage(message)
                .assertNotComplete();
        return this;
    }

    /**
     * Awaits until the internal latch is counted down.
     * <p>If the wait times out or gets interrupted, the TestObserver/TestSubscriber is cancelled.
     * @param time the waiting time
     * @param unit the time unit of the waiting time
     * @return this
     * @throws RuntimeException wrapping an InterruptedException if the wait is interrupted
     */
    public final AbstractTestConsumerAssert<T, P> awaitDone(long time, TimeUnit unit) {
        actual.awaitDone(time,unit);
        return this;
    }


    /**
     * Assert that the TestObserver/TestSubscriber/TestSubscriber has received a Disposable but no other events.
     * @return this
     */
    public final AbstractTestConsumerAssert<T, P> hasEmpty() {
        actual.assertSubscribed()
                .assertNoValues()
                .assertNoErrors()
                .assertNotComplete();
        return this;
    }
}