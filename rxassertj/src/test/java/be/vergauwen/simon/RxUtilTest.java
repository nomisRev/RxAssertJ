package be.vergauwen.simon;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rx.observers.TestSubscriber;

import static be.vergauwen.simon.RxAssertJ.assertThat;
import static be.vergauwen.simon.RxAssertJ.assertThatASubscriberTo;

public class RxUtilTest {
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
    public void testDoSomeRxing() {
        rxUtil.doSomeRxing().subscribe(testSubscriber);
        assertThat(testSubscriber).hasNoErrors().hasReceivedCount(1).hasReceived(1L).isCompleted();
    }

    @Test
    public void testSomeLongRxing() {
        assertThatASubscriberTo(rxUtil.doSomeLongRxing()).isCompleted();
    }

    //15 : 610 = 2 x 5 x 61
    @Test
    public void testgetSomeSingleValue() {
        assertThatASubscriberTo(rxUtil.getSomeSingleValue(15)).hasNoErrors().hasReceived(610L).isCompleted();
    }
}