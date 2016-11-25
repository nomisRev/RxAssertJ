package com.github.nomisrev.rxassertj;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.condition.AllOf.allOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

public class RxAssertionsTests {

    @Rule
    public RxJavaTestRule rxJavaResetRule = new RxJavaTestRule();

    private TestSubscriber<Long> testSubscriber;
    private ObservableBuilder observableBuilder;

    @Before
    public void setUp() {
        testSubscriber = new TestSubscriber<>();
        observableBuilder = new ObservableBuilder();
    }

    @Test
    public void emptyObservableShouldEmitNothing() {
        RxAssertions.assertThatSubscriberTo(Observable.empty())
                .emitsNothing()
                .completes()
                .withoutErrors();
    }

    @Test
    public void singleItemObservableShouldEmitOneValue() {
        RxAssertions.assertThatSubscriberTo(Observable.just("one"))
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .eachItemMatches(isNotNullOrEmpty);
    }

    @Test(expected = AssertionError.class)
    public void allItemsConditionMatcherShouldFail() {
        Observable<Integer> observable = Observable.just(1);

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
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
        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
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

        RxAssertions.assertThatSubscriberTo(observable)
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areAtLeast(2, containsTheLetterA);
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areAtLeast(3, containsTheLetterA);
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areAtMost(2, containsTheLetterA);
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areAtMost(1, containsTheLetterA);
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areExactly(2, containsTheLetterA);
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

        RxAssertions.assertThatSubscriberTo(observable)
                .completes()
                .withoutErrors()
                .areExactly(3, containsTheLetterA);
    }

    private static Condition<Object> FAILING_CONDITION = new Condition<Object>() {
        @Override
        public boolean matches(Object value) {
            return false;
        }
    };

}
