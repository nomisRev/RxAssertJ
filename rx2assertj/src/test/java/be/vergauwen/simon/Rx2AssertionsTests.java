package be.vergauwen.simon;


import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.condition.AllOf.allOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

public class Rx2AssertionsTests {

    private TestSubscriber<Integer> testSubscriber;
    private TestObserver<Long> testObserver;

    @Before
    public void setUp() {
        testSubscriber = new TestSubscriber<>();
        testObserver = new TestObserver<>();
    }

    @Test
    public void allItemsShouldMeetCondition() throws Exception {
        Observable<String> observable = getJediStringEmittingObservable();

        Condition<String> isNotNullOrEmpty = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value != null && !value.isEmpty();
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .eachItemMatches(isNotNullOrEmpty);
    }

    @Test
    public void allItemsShouldMeetCombinedConditions() throws Exception {
        Observable<String> observable = getJediStringEmittingObservable();

        Condition<String> isNotNullOrEmpty = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value != null && !value.isEmpty();
            }
        };
        Condition<String> isJedi = new Condition<String>("jedi") {
            @Override
            public boolean matches(String value) {
                return JEDIS.contains(value);
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .eachItemMatches(allOf(isNotNullOrEmpty, isJedi));
    }

    @Test
    public void allItemsShouldNotMeetCondition() throws Exception {
        Observable<String> observable = getJediStringEmittingObservable();

        Condition<String> isNull = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value == null;
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .noItemNotMatches(isNull);
    }

    @Test
    public void atLeastOneItemShouldMeetCondition() throws Exception {
        Observable<String> observable = getJediStringEmittingObservable();

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

    @Test
    public void atLeastTwoItemsShouldMeetCondition() throws Exception {
        Observable<String> observable = getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .areAtLeast(2, containsTheLetterA);

    }

    @Test
    public void atMostTwoItemsShouldMeetCondition() throws Exception {
        Observable<String> observable = getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .areAtMost(2, containsTheLetterA);

    }

    @Test
    public void exactlyTwoItemsShouldMeetCondition() throws Exception {
        Observable<String> observable = getJediStringEmittingObservable();

        Condition<String> containsTheLetterA = new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return value.contains("a");
            }
        };

        Rx2Assertions.assertThatSubscriberTo(observable)
                .isComplete()
                .withoutErrors()
                .areExactly(2, containsTheLetterA);

    }

    private static Set<String> JEDIS = newLinkedHashSet("Luke", "Yoda", "Obiwan");

    private Observable<String> getJediStringEmittingObservable() {
        return Observable.fromIterable(JEDIS);
    }
}
