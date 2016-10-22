package be.vergauwen.simon.rxassertj;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rx.observers.TestSubscriber;

import static be.vergauwen.simon.rxassertj.RxAssertJ.assertThat;
import static be.vergauwen.simon.rxassertj.RxAssertJ.assertThatASubscriberTo;

public class RxUtilTest {
    @Rule public RxJavaTestRule rxJavaResetRule = new RxJavaTestRule();

    TestSubscriber<Long> testSubscriber;

    @Before
    public void setUp() {
        testSubscriber = new TestSubscriber<>();
    }

    @Test
    public void testDoSomeRxing() {
        RxUtil.doSomeRxing().subscribe(testSubscriber);
        assertThat(testSubscriber).hasNoErrors().hasReceivedCount(1).hasReceived(1L).isCompleted();
    }

    @Test
    public void testSomeLongRxing() {
        assertThatASubscriberTo(RxUtil.doSomeLongRxing()).isCompleted();
    }

    //15 : 610 = 2 x 5 x 61
    @Test
    public void testgetSomeSingleValue() {
        RxAssertJ.assertThatASubscriberTo(RxUtil.getSomeSingleValue(15)).hasNoErrors().hasReceived(610L).isCompleted();
    }
}