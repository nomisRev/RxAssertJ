package be.vergauwen.simon;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

public class RxAssertionsTests {

    @Rule
    public RxJavaTestRule rxJavaResetRule = new RxJavaTestRule();

    private TestSubscriber<Long> testSubscriber;
    private RxUtil rxUtil;

    @Before
    public void setUp() {
        testSubscriber = new TestSubscriber<>();
        rxUtil = new RxUtil();
    }

    @Test
    public void emptyObservableShouldEmitNothing() throws Exception {
        RxAssertions.assertThatSubscriberTo(Observable.empty())
                .emitsNothing()
                .completes()
                .withoutErrors();
    }

    @Test
    public void singleItemObservableShouldEmitOneValue() throws Exception {
        RxAssertions.assertThatSubscriberTo(Observable.just("one"))
                .hasReceivedCount(1)
                .completes()
                .withoutErrors();
    }
}
