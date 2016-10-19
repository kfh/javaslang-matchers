package in.tazj.javaslang.matchers;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javaslang.collection.List;

import static in.tazj.javaslang.matchers.CollectionMatchers.contains;
import static in.tazj.javaslang.matchers.CollectionMatchers.containsInAnyOrder;
import static in.tazj.javaslang.matchers.CollectionMatchers.hasSize;
import static in.tazj.javaslang.matchers.CollectionMatchers.isEmpty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

public class CollectionMatchersTest {
  @Test
  public void testIsEmpty() throws Exception {
    assertThat(List.empty(), isEmpty());
    assertThat(List.of(1), not(isEmpty()));
  }

  @Test
  public void testHasSize() throws Exception {
    assertThat(List.of(1,2,3,4,5), hasSize(5));
    assertThat(List.empty(), not(hasSize(5)));
  }

  @Test
  public void testContains() throws Exception {
    assertThat(List.of(1,2,3), contains(2));
    assertThat(List.of(1,2,3), not(contains(4)));
    assertThat(List.empty(), not(contains(1)));
  }

  @Test
  public void testContainsInAnyOrder() throws Exception {
    assertThat(List.of(3,2,1), containsInAnyOrder(List.of(1,2,3)));
    assertThat(List.empty(), not(containsInAnyOrder(List.of(1,2,3))));
  }

  @Test
  public void testContainsInAnyOrderMismatch() {
    // Hack to be able to set this list in the Answer
    final List[] output = new List[1];
    final Description mismatchDescription = Mockito.mock(Description.class);

    Mockito.when(mismatchDescription.appendText(anyString())).thenReturn(mismatchDescription);
    Mockito.when(mismatchDescription
        .appendValueList(anyString(), anyString(), anyString(), any(Iterable.class)))
        .then(new Answer<Description>() {
          @Override
          public Description answer(InvocationOnMock invocationOnMock) throws Throwable {
            output[0] = (List) invocationOnMock.getArguments()[3];
            return mismatchDescription;
          }
        });

    containsInAnyOrder(List.of(1,2,3,4,5,6)).describeMismatch(List.of(1,2,3), mismatchDescription);
    assertThat(output[0], containsInAnyOrder(List.of(4,5,6)));
  }
}
