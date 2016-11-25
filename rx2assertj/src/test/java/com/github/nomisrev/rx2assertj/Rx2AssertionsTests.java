package com.github.nomisrev.rx2assertj;


import io.reactivex.*;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.assertj.core.api.Condition;
import org.assertj.core.api.exception.RuntimeIOException;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.condition.AllOf.allOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

public class Rx2AssertionsTests {

    private TestSubscriber<Integer> testSubscriber;
    private TestObserver<Long> testObserver;
    private Subscription testSubscription;

    @Before
    public void setUp() {
        testSubscriber = new TestSubscriber<>();
        testObserver = new TestObserver<>();
        testSubscription = new Subscription() {
            @Override
            public void request(long n) {
            }

            @Override
            public void cancel() {
            }
        };
    }

    @Test(expected = AssertionError.class)
    public void testIsNotCompleteFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1))
                .isNotComplete();
    }

    @Test
    public void testIsNotComplete() {
        Rx2Assertions.assertThatSubscriberTo(Observable.never())
                .isNotComplete();
    }

    @Test
    public void testIsNotTerminated() {
        Rx2Assertions.assertThatSubscriberTo(Observable.never())
                .isNotTerminated();
    }

    @Test(expected = AssertionError.class)
    public void testIsNotTerminatedFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1))
                .isNotTerminated();
    }

    @Test
    public void testIsComplete() {
        Rx2Assertions.assertThatSubscriberTo(Completable.complete())
                .isComplete();
    }

    @Test(expected = AssertionError.class)
    public void testIsCompleteFails() {
        Rx2Assertions.assertThatSubscriberTo(Completable.never())
                .isComplete();
    }

    @Test
    public void testIsTerminated() {
        Rx2Assertions.assertThatSubscriberTo(Maybe.error(testException)).isTerminated();
        Rx2Assertions.assertThatSubscriberTo(Maybe.just(1)).isTerminated();
    }

    @Test(expected = AssertionError.class)
    public void testIsTerminatedWithFails() {
        Rx2Assertions.assertThatSubscriberTo(Maybe.never()).isTerminated();
    }

    @Test
    public void testHasErrorInstance() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(testException);
    }

    @Test(expected = AssertionError.class)
    public void testHasErrorInstanceFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(otherTestException))
                .hasError(testException);
    }

    @Test
    public void testHasError() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(IllegalStateException.class);
    }

    @Test(expected = AssertionError.class)
    public void testHasErrorFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1))
                .hasError(IllegalStateException.class);
    }

    @Test
    public void testHasErrorWithPredicate() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(throwable -> throwable.equals(testException));
    }

    @Test(expected = AssertionError.class)
    public void testHasErrorWithPredicateFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasError(throwable -> throwable.equals(otherTestException));
    }

    @Test
    public void testHasValuePredicate() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(ObservableBuilder.JEDIS))
                .hasSingleValue(value -> value.equals(ObservableBuilder.JEDIS));
    }

    @Test(expected = AssertionError.class)
    public void testHasValuePredicateFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(ObservableBuilder.JEDIS))
                .hasSingleValue(value -> value.equals(newLinkedHashSet("something")));
    }

    @Test
    public void testHasValueAtPredicate() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromArray(1, 2, 3))
                .hasValueAt(1, value -> value.equals(2));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueAtPredicateFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromArray(1, 2, 3))
                .hasValueAt(1, value -> value.equals(3));
    }

    @Test
    public void testHasValues() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromArray(1, 2, 3))
                .hasValues(1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void testHasValuesFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromArray(1, 2, 3))
                .hasValues(1, 3, 2);
    }

    @Test
    public void testHasValuesSet() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromIterable(ObservableBuilder.JEDIS))
                .hasValueSet(ObservableBuilder.JEDIS);
    }

    @Test(expected = AssertionError.class)
    public void testHasValuesSetFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromIterable(ObservableBuilder.JEDIS))
                .hasValueSet(newLinkedHashSet());
    }

    @Test
    public void testHasValuesSequence() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromIterable(ObservableBuilder.JEDIS))
                .hasValueSequence(ObservableBuilder.JEDIS);
    }


    @Test(expected = AssertionError.class)
    public void testHasValuesSequenceFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.fromIterable(ObservableBuilder.JEDIS))
                .hasValueSequence(newLinkedHashSet());
    }

    @Test
    public void testHasErrorMessage() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasErrorMessage(testExceptionMessage);
    }

    @Test(expected = AssertionError.class)
    public void testHasErrorMessageFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.error(testException))
                .hasErrorMessage("Some other message");
    }

    @Test
    public void testHasResults() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1, 2, 3, 4))
                .hasResult(1, 2, 3, 4);
    }

    @Test(expected = AssertionError.class)
    public void testHasResultsFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1, 2, 3, 4))
                .hasResult(1, 2, 3);
    }

    @Test
    public void testHasFailure() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(IllegalStateException.class, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void testHasFailureFailsWithDifferentException() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(IOException.class, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void testHasFailureFailsWithDifferentValues() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(IllegalStateException.class, 4, 5, 6);
    }

    @Test
    public void testHasFailurePredicate() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(error -> error.equals(testException), 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void testHasFailurePredicateFailsWithDifferentException() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(error -> error.equals(otherTestException), 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void testHasFailurePredicateFailsWithDifferentValues() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailure(error -> error.equals(testException), 4, 5, 6);
    }

    @Test
    public void testHasFailureWithMessage() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailureAndMessage(IllegalStateException.class, testExceptionMessage, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void testHasFailureWithMessageFailsWithDifferentException() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailureAndMessage(IOException.class, testExceptionMessage, 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void testHasFailureWithMessageFailsWithDifferentMessage() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailureAndMessage(IllegalStateException.class, "Some other message", 1, 2, 3);
    }

    @Test(expected = AssertionError.class)
    public void testHasFailureWithMessageFailsWithDifferentValues() {
        Rx2Assertions.assertThatSubscriberTo(ObservableBuilder.getObservableWithTestException(testException, 1, 2, 3))
                .hasFailureAndMessage(IOException.class, testExceptionMessage, 4, 5, 6);
    }

    @Test
    public void streamShouldContain() {
        Rx2Assertions.assertThatSubscriberTo(Flowable.just(1, 2, 3)).contains(1, 2);
    }

    @Test(expected = AssertionError.class)
    public void streamShouldContainShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Flowable.just(1, 2, 3)).contains(4, 5);
    }

    @Test
    public void streamShouldNotContain() {
        Rx2Assertions.assertThatSubscriberTo(Flowable.just(1, 2, 3)).doesNotContain(4, 5);
    }

    @Test(expected = AssertionError.class)
    public void streamShouldNotContainShouldFail() {
        Rx2Assertions.assertThatSubscriberTo(Flowable.just(1, 2, 3)).doesNotContain(1, 2);
    }

    @Test
    public void testAwaitDone() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1).delay(250, TimeUnit.MILLISECONDS))
                .awaitDone(500, TimeUnit.MILLISECONDS)
                .isComplete();
    }

    @Test(expected = AssertionError.class)
    public void testAwaitDoneFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1).delay(500, TimeUnit.MILLISECONDS))
                .awaitDone(250, TimeUnit.MILLISECONDS)
                .isComplete();
    }

    @Test
    public void testHasValue() {
        Rx2Assertions.assertThatSubscriberTo(Single.just(1))
                .hasSingleValue(1);
    }

    @Test(expected = AssertionError.class)
    public void testHasValueFailsMulitpleValues() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1, 2))
                .hasSingleValue(1);
    }

    @Test(expected = AssertionError.class)
    public void testHasValueFailsDifferentValue() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(2))
                .hasSingleValue(1);
    }

    @Test
    public void testTestObserverNotSubscribed() {
        Rx2Assertions.assertThat(testObserver).isNotSubscribed();
    }

    @Test(expected = AssertionError.class)
    public void testTestObserverNotSubscribedFails() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just(1)).isNotSubscribed();
    }

    @Test
    public void testTestObserverIsSubscribed() {
        Rx2Assertions.assertThat(testObserver).isNotSubscribed();
        testObserver.onSubscribe(testSubscriber);
        Rx2Assertions.assertThat(testObserver).isSubscribed();
    }

    @Test(expected = AssertionError.class)
    public void testTestObserverIsSubscribedFails() {
        Rx2Assertions.assertThat(testObserver).isSubscribed();
    }

    @Test
    public void testTestSubscriberNotSubscribed() {
        Rx2Assertions.assertThat(testSubscriber).isNotSubscribed();
    }

    @Test(expected = AssertionError.class)
    public void testTestSubscriberNotSubscribedFails() {
        Rx2Assertions.assertThatSubscriberTo(Flowable.just(1)).isNotSubscribed();
    }

    @Test
    public void testTestSubscriberIsSubscribed() {
        Rx2Assertions.assertThat(testSubscriber).isNotSubscribed();
        testSubscriber.onSubscribe(testSubscription);
        Rx2Assertions.assertThat(testSubscriber).isSubscribed();
    }

    @Test(expected = AssertionError.class)
    public void testTestSubscriberIsSubscribedFails() {
        Rx2Assertions.assertThat(testSubscriber).isSubscribed();
    }

    @Test
    public void emptyObservableShouldEmitNothing() {
        Rx2Assertions.assertThatSubscriberTo(Observable.empty())
                .emitsNothing()
                .completes()
                .withoutErrors();
    }

    @Test
    public void singleItemObservableShouldEmitOneValue() {
        Rx2Assertions.assertThatSubscriberTo(Observable.just("one"))
                .hasValueCount(1)
                .completes()
                .withoutErrors();
    }

    @Test
    public void allItemsShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isNotNullOrEmpty = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value != null && !value.isEmpty();
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .eachItemMatches(isNotNullOrEmpty);
    }

    @Test(expected = AssertionError.class)
    public void allItemsConditionMatcherShouldFail() {
        Observable<Integer> observable = Observable.just(1);

        Rx2Assertions.assertThatSubscriberTo(observable)
                .eachItemMatches(FAILING_CONDITION);
    }

    @Test
    public void allItemsShouldMeetCombinedConditions() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isNotNullOrEmpty = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value != null && !value.isEmpty();
            }
        };
        Condition<String> isJedi = new Condition<String>("jedi") {
            @Override
            public boolean matches(String value) {
                return ObservableBuilder.JEDIS.contains(value);
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .eachItemMatches(allOf(isNotNullOrEmpty, isJedi));
    }

    @Test
    public void allItemsShouldNotMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isNull = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value == null;
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .noItemMatches(isNull);
    }

    @Test(expected = AssertionError.class)
    public void noItemMatch() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isNotNullOrEmpty = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value != null && !value.isEmpty();
            }
        };
        Rx2Assertions.assertThatSubscriberTo(observable)
                .noItemMatches(isNotNullOrEmpty);
    }

    @Test
    public void atLeastOneItemShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isLuke = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.equals("Luke");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .atLeastOneItemMatches(isLuke);

    }

    @Test(expected = AssertionError.class)
    public void atLeastOneConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> isDarthVader = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.equals("Darth Vader");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .atLeastOneItemMatches(isDarthVader);
    }

    @Test
    public void atLeastTwoItemsShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .haveAtLeast(2, containsTheLetterA);

    }

    @Test(expected = AssertionError.class)
    public void atLeastConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .haveAtLeast(3, containsTheLetterA);
    }

    @Test
    public void atMostTwoItemsShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .haveAtMost(2, containsTheLetterA);

    }

    @Test(expected = AssertionError.class)
    public void atMostConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .haveAtMost(1, containsTheLetterA);
    }

    @Test
    public void exactlyTwoItemsShouldMeetCondition() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .haveExactly(2, containsTheLetterA);

    }

    @Test(expected = AssertionError.class)
    public void areExactlyConditionCheckShouldFail() {
        Observable<String> observable = ObservableBuilder.getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .haveExactly(3, containsTheLetterA);
    }

    private static Condition<Object> FAILING_CONDITION = new Condition<Object>() {
        @Override
        public boolean matches(Object value) {
            return false;
        }
    };

    private final String testExceptionMessage = "Some error text";

    private final Throwable testExceptionCause = new IOException("File doesn't exist");

    private final Throwable testException = new IllegalStateException(testExceptionMessage, testExceptionCause);

    private final Throwable otherTestException = new RuntimeIOException("File doesn't exist");

}
