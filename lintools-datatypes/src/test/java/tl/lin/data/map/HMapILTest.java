/*
 * Lintools: tools by @lintool
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package tl.lin.data.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Random;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public class HMapILTest {
  @Test
  public void testRandomInsert() {
    int size = 100000;
    Random r = new Random();
    long[] longs = new long[size];

    MapIL map = new HMapIL();
    for (int i = 0; i < size; i++) {
      int k = r.nextInt(size);
      map.put(i, k * Integer.MAX_VALUE);
      longs[i] = k * Integer.MAX_VALUE;
    }

    for (int i = 0; i < size; i++) {
      long v = map.get(i);

      assertEquals(longs[i], v);
      assertTrue(map.containsKey(i));
    }
  }

  @Test
  public void testRandomUpdate() {
    int size = 100000;
    Random r = new Random();
    long[] longs = new long[size];

    MapIL map = new HMapIL();
    for (int i = 0; i < size; i++) {
      int k = r.nextInt(size);
      map.put(i, k + (long) Integer.MAX_VALUE);
      longs[i] = k + (long) Integer.MAX_VALUE;
    }

    assertEquals(size, map.size());

    for (int i = 0; i < size; i++) {
      map.put(i, longs[i] + (long) Integer.MAX_VALUE);
    }

    assertEquals(size, map.size());

    for (int i = 0; i < size; i++) {
      long v = map.get(i);

      assertEquals(longs[i] + (long) Integer.MAX_VALUE, v);
      assertTrue(map.containsKey(i));
    }
  }

  @Test
  public void testBasic() throws IOException {
    HMapIL m = new HMapIL();

    m.put(1, Integer.MAX_VALUE + 5L);
    m.put(2, Integer.MAX_VALUE + 22L);

    long value;

    assertEquals(2, m.size());

    value = m.get(1);
    assertEquals(Integer.MAX_VALUE + 5L, value);

    value = m.remove(1);
    assertEquals(m.size(), 1);

    value = m.get(2);
    assertEquals(Integer.MAX_VALUE + 22L, value);
  }

  @Test
  public void testPlus() throws IOException {
    HMapIL m1 = new HMapIL();

    m1.put(1, Integer.MAX_VALUE + 5L);
    m1.put(2, Integer.MAX_VALUE + 22L);
    m1.put(Integer.MAX_VALUE, Integer.MAX_VALUE);

    HMapIL m2 = new HMapIL();

    m2.put(1, 4L);
    m2.put(3, Integer.MAX_VALUE + 5L);
    m2.put(Integer.MAX_VALUE, Integer.MAX_VALUE);

    m1.plus(m2);

    assertEquals(m1.size(), 4);
    assertTrue(m1.get(1) == Integer.MAX_VALUE + 9L);
    assertTrue(m1.get(2) == Integer.MAX_VALUE + 22L);
    assertTrue(m1.get(3) == Integer.MAX_VALUE + 5L);
    assertTrue(m1.get(Integer.MAX_VALUE) == 2L * Integer.MAX_VALUE);
  }

  @Test
  public void testDot() throws IOException {
    HMapIL m1 = new HMapIL();

    m1.put(1, 2L);
    m1.put(2, 1L);
    m1.put(3, 3L);
    m1.put(Integer.MAX_VALUE, (long) Integer.MAX_VALUE);

    HMapIL m2 = new HMapIL();

    m2.put(1, 1L);
    m2.put(2, 4L);
    m2.put(4, 5L);
    m2.put(Integer.MAX_VALUE, (long) Integer.MAX_VALUE);

    long s = m1.dot(m2);

    assertTrue(s > Integer.MAX_VALUE);
    assertEquals((long) Integer.MAX_VALUE * Integer.MAX_VALUE + 6L, s);
  }

  @Test
  public void testIncrement() {
    HMapIL m = new HMapIL();
    assertEquals(0L, m.get(1));

    m.increment(1, 1L);
    assertEquals(1L, m.get(1));

    m.increment(1, 1L);
    m.increment(2, 0L);
    m.increment(3, -1L);

    assertEquals(2L, m.get(1));
    assertEquals(0L, m.get(2));
    assertEquals(-1L, m.get(3));

    m.increment(Integer.MAX_VALUE, Integer.MAX_VALUE);
    assertEquals((long) Integer.MAX_VALUE, m.get(Integer.MAX_VALUE));

    m.increment(Integer.MAX_VALUE);
    assertEquals(1L + Integer.MAX_VALUE, m.get(Integer.MAX_VALUE));
  }

  @Test
  public void testSortedEntries1() {
    HMapIL m = new HMapIL();

    m.put(1, 5L);
    m.put(2, 2L);
    m.put(3, 3L);
    m.put(4, 3L);
    m.put(5, 1L);

    MapIL.Entry[] e = m.getEntriesSortedByValue();
    assertEquals(5, e.length);

    assertEquals(1, e[0].getKey());
    assertEquals(5L, e[0].getValue());

    assertEquals(3, e[1].getKey());
    assertEquals(3L, e[1].getValue());

    assertEquals(4, e[2].getKey());
    assertEquals(3L, e[2].getValue());

    assertEquals(2, e[3].getKey());
    assertEquals(2L, e[3].getValue());

    assertEquals(5, e[4].getKey());
    assertEquals(1L, e[4].getValue());
  }

  @Test
  public void testSortedEntries2() {
    HMapIL m = new HMapIL();

    m.put(1, 5L);
    m.put(2, 2L);
    m.put(3, 3L);
    m.put(4, 3L);
    m.put(5, 1L);

    MapIL.Entry[] e = m.getEntriesSortedByValue(2);

    assertEquals(2, e.length);

    assertEquals(1, e[0].getKey());
    assertEquals(5L, e[0].getValue());

    assertEquals(3, e[1].getKey());
    assertEquals(3L, e[1].getValue());
  }

  @Test
  public void testSortedEntries3() {
    HMapIL m = new HMapIL();

    m.put(1, 5L);
    m.put(2, 2L);

    MapIL.Entry[] e = m.getEntriesSortedByValue(5);

    assertEquals(2, e.length);

    assertEquals(1, e[0].getKey());
    assertEquals(5L, e[0].getValue());

    assertEquals(2, e[1].getKey());
    assertEquals(2L, e[1].getValue());
  }

  @Test
  public void testSortedEntries4() {
    HMapIL m = new HMapIL();

    MapIL.Entry[] e = m.getEntriesSortedByValue();
    assertTrue(e == null);
  }

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(HMapILTest.class);
  }
}