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

package tl.lin.data.fd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import tl.lin.data.fd.SortableEntries.Order;
import tl.lin.data.pair.PairOfInts;

public class Int2IntFrequencyDistributionTestBase {
  protected void test1Common(Int2IntFrequencyDistribution fd) {
    assertEquals(0, fd.get(1));

    fd.increment(1);
    fd.increment(2);
    fd.increment(3);
    fd.increment(2);
    fd.increment(3);
    fd.increment(3);

    assertEquals(3, fd.getNumberOfEvents());
    assertEquals(6, fd.getSumOfCounts());

    assertEquals(1, fd.get(1));
    assertEquals(2, fd.get(2));
    assertEquals(3, fd.get(3));

    assertEquals((float) 1 / 6, fd.computeRelativeFrequency(1), 10e-6);
    assertEquals((float) 2 / 6, fd.computeRelativeFrequency(2), 10e-6);
    assertEquals((float) 3 / 6, fd.computeRelativeFrequency(3), 10e-6);

    assertEquals(Math.log((float) 1 / 6), fd.computeLogRelativeFrequency(1), 10e-6);
    assertEquals(Math.log((float) 2 / 6), fd.computeLogRelativeFrequency(2), 10e-6);
    assertEquals(Math.log((float) 3 / 6), fd.computeLogRelativeFrequency(3), 10e-6);

    fd.decrement(3);

    assertEquals(3, fd.getNumberOfEvents());
    assertEquals(5, fd.getSumOfCounts());

    assertEquals(1, fd.get(1));
    assertEquals(2, fd.get(2));
    assertEquals(2, fd.get(3));

    assertEquals((float) 1 / 5, fd.computeRelativeFrequency(1), 10e-6);
    assertEquals((float) 2 / 5, fd.computeRelativeFrequency(2), 10e-6);
    assertEquals((float) 2 / 5, fd.computeRelativeFrequency(3), 10e-6);

    assertEquals(Math.log((float) 1 / 5), fd.computeLogRelativeFrequency(1), 10e-6);
    assertEquals(Math.log((float) 2 / 5), fd.computeLogRelativeFrequency(2), 10e-6);
    assertEquals(Math.log((float) 2 / 5), fd.computeLogRelativeFrequency(3), 10e-6);

    fd.decrement(1);

    assertEquals(2, fd.getNumberOfEvents());
    assertEquals(4, fd.getSumOfCounts());

    assertEquals(0, fd.get(1));
    assertEquals(2, fd.get(2));
    assertEquals(2, fd.get(3));

    assertEquals((float) 2 / 4, fd.computeRelativeFrequency(2), 10e-6);
    assertEquals((float) 2 / 4, fd.computeRelativeFrequency(3), 10e-6);

    assertEquals(Math.log((float) 2 / 4), fd.computeLogRelativeFrequency(2), 10e-6);
    assertEquals(Math.log((float) 2 / 4), fd.computeLogRelativeFrequency(3), 10e-6);
  }

  protected void test2Common(Int2IntFrequencyDistribution fd) {
    fd.increment(1);
    fd.increment(1);
    fd.increment(2);
    fd.increment(3);

    assertEquals(3, fd.getNumberOfEvents());
    assertEquals(4, fd.getSumOfCounts());

    assertEquals(2, fd.get(1));
    assertEquals(1, fd.get(2));
    assertEquals(1, fd.get(3));

    fd.set(4, 5);

    assertEquals(4, fd.getNumberOfEvents());
    assertEquals(9, fd.getSumOfCounts());

    assertEquals(2, fd.get(1));
    assertEquals(1, fd.get(2));
    assertEquals(1, fd.get(3));
    assertEquals(5, fd.get(4));

    fd.set(1, 5);

    assertEquals(4, fd.getNumberOfEvents());
    assertEquals(12, fd.getSumOfCounts());

    assertEquals(5, fd.get(1));
    assertEquals(1, fd.get(2));
    assertEquals(1, fd.get(3));
    assertEquals(5, fd.get(4));

    fd.increment(3);
    fd.increment(3);
    fd.increment(3);

    assertEquals(4, fd.getNumberOfEvents());
    assertEquals(15, fd.getSumOfCounts());

    assertEquals(5, fd.get(1));
    assertEquals(1, fd.get(2));
    assertEquals(4, fd.get(3));
    assertEquals(5, fd.get(4));

    fd.set(3, 1);

    assertEquals(4, fd.getNumberOfEvents());
    assertEquals(12, fd.getSumOfCounts());

    assertEquals(5, fd.get(1));
    assertEquals(1, fd.get(2));
    assertEquals(1, fd.get(3));
    assertEquals(5, fd.get(4));
  }

  protected void test3Common(Int2IntFrequencyDistribution fd) {
    fd.increment(1);
    fd.increment(1);
    fd.increment(2);
    fd.increment(3);

    assertEquals(3, fd.getNumberOfEvents());
    assertEquals(4, fd.getSumOfCounts());

    assertEquals(2, fd.get(1));
    assertEquals(1, fd.get(2));
    assertEquals(1, fd.get(3));

    fd.clear();
    assertEquals(0, fd.getNumberOfEvents());
    assertEquals(0, fd.getSumOfCounts());
  }

  protected void testFailedDecrement1Common(Int2IntFrequencyDistribution fd) {
    fd.increment(1);

    assertEquals(1, fd.getNumberOfEvents());
    assertEquals(1, fd.getSumOfCounts());
    assertEquals(1, fd.get(1));

    fd.decrement(1);

    assertEquals(0, fd.getNumberOfEvents());
    assertEquals(0, fd.getSumOfCounts());
    assertEquals(0, fd.get(1));

    fd.decrement(1);
  }

  protected void testFailedDecrement2Common(Int2IntFrequencyDistribution fd) {
    fd.increment(1, 1000);

    assertEquals(1, fd.getNumberOfEvents());
    assertEquals(1000, fd.getSumOfCounts());
    assertEquals(1000, fd.get(1));

    fd.decrement(1, 997);

    assertEquals(1, fd.getNumberOfEvents());
    assertEquals(3, fd.getSumOfCounts());
    assertEquals(3, fd.get(1));

    fd.decrement(1, 3);

    assertEquals(0, fd.getNumberOfEvents());
    assertEquals(0, fd.getSumOfCounts());
    assertEquals(0, fd.get(1));

    fd.increment(1, 3);
    fd.decrement(1, 4);
  }

  protected void testMultiIncrementDecrementCommon(Int2IntFrequencyDistribution fd) {
    fd.increment(1, 2);
    fd.increment(2, 3);
    fd.increment(3, 4);

    assertEquals(3, fd.getNumberOfEvents());
    assertEquals(9, fd.getSumOfCounts());

    assertEquals(2, fd.get(1));
    assertEquals(3, fd.get(2));
    assertEquals(4, fd.get(3));

    fd.decrement(2, 2);

    assertEquals(3, fd.getNumberOfEvents());
    assertEquals(7, fd.getSumOfCounts());

    assertEquals(2, fd.get(1));
    assertEquals(1, fd.get(2));
    assertEquals(4, fd.get(3));
  }

  protected void testGetFrequencySortedEventsCommon(Int2IntFrequencyDistribution fd) {
    fd.set(1, 5);
    fd.set(4, 2);
    fd.set(2, 5);
    fd.set(5, 2);
    fd.set(6, 1);
    fd.set(3, 5);

    assertEquals(6, fd.getNumberOfEvents());
    assertEquals(20, fd.getSumOfCounts());

    List<PairOfInts> list = fd.getEntries(Order.ByRightElementDescending);

    assertEquals(6, list.size());

    assertEquals(1, list.get(0).getLeftElement());
    assertEquals(5, list.get(0).getRightElement());
    assertEquals(2, list.get(1).getLeftElement());
    assertEquals(5, list.get(1).getRightElement());
    assertEquals(3, list.get(2).getLeftElement());
    assertEquals(5, list.get(2).getRightElement());
    assertEquals(4, list.get(3).getLeftElement());
    assertEquals(2, list.get(3).getRightElement());
    assertEquals(5, list.get(4).getLeftElement());
    assertEquals(2, list.get(4).getRightElement());
    assertEquals(6, list.get(5).getLeftElement());
    assertEquals(1, list.get(5).getRightElement());

    list = fd.getEntries(Order.ByRightElementAscending);

    assertEquals(6, list.size());

    assertEquals(6, list.get(0).getLeftElement());
    assertEquals(1, list.get(0).getRightElement());
    assertEquals(4, list.get(1).getLeftElement());
    assertEquals(2, list.get(1).getRightElement());
    assertEquals(5, list.get(2).getLeftElement());
    assertEquals(2, list.get(2).getRightElement());
    assertEquals(1, list.get(3).getLeftElement());
    assertEquals(5, list.get(3).getRightElement());
    assertEquals(2, list.get(4).getLeftElement());
    assertEquals(5, list.get(4).getRightElement());
    assertEquals(3, list.get(5).getLeftElement());
    assertEquals(5, list.get(5).getRightElement());

    list = fd.getEntries(Order.ByRightElementDescending, 4);

    assertEquals(4, list.size());

    assertEquals(1, list.get(0).getLeftElement());
    assertEquals(5, list.get(0).getRightElement());
    assertEquals(2, list.get(1).getLeftElement());
    assertEquals(5, list.get(1).getRightElement());
    assertEquals(3, list.get(2).getLeftElement());
    assertEquals(5, list.get(2).getRightElement());
    assertEquals(4, list.get(3).getLeftElement());
    assertEquals(2, list.get(3).getRightElement());

    list = fd.getEntries(Order.ByRightElementAscending, 4);

    assertEquals(4, list.size());

    assertEquals(6, list.get(0).getLeftElement());
    assertEquals(1, list.get(0).getRightElement());
    assertEquals(4, list.get(1).getLeftElement());
    assertEquals(2, list.get(1).getRightElement());
    assertEquals(5, list.get(2).getLeftElement());
    assertEquals(2, list.get(2).getRightElement());
    assertEquals(1, list.get(3).getLeftElement());
    assertEquals(5, list.get(3).getRightElement());
  }

  protected void testGetSortedEventsCommon(Int2IntFrequencyDistribution fd) {
    fd.set(1, 1);
    fd.set(4, 3);
    fd.set(2, 4);
    fd.set(5, 7);
    fd.set(6, 9);
    fd.set(3, 2);

    assertEquals(6, fd.getNumberOfEvents());
    assertEquals(26, fd.getSumOfCounts());

    List<PairOfInts> list = fd.getEntries(Order.ByLeftElementAscending);

    assertEquals(6, list.size());

    assertEquals(1, list.get(0).getLeftElement());
    assertEquals(1, list.get(0).getRightElement());
    assertEquals(2, list.get(1).getLeftElement());
    assertEquals(4, list.get(1).getRightElement());
    assertEquals(3, list.get(2).getLeftElement());
    assertEquals(2, list.get(2).getRightElement());
    assertEquals(4, list.get(3).getLeftElement());
    assertEquals(3, list.get(3).getRightElement());
    assertEquals(5, list.get(4).getLeftElement());
    assertEquals(7, list.get(4).getRightElement());
    assertEquals(6, list.get(5).getLeftElement());
    assertEquals(9, list.get(5).getRightElement());

    list = fd.getEntries(Order.ByLeftElementDescending);

    assertEquals(6, list.size());

    assertEquals(6, list.get(0).getLeftElement());
    assertEquals(9, list.get(0).getRightElement());
    assertEquals(5, list.get(1).getLeftElement());
    assertEquals(7, list.get(1).getRightElement());
    assertEquals(4, list.get(2).getLeftElement());
    assertEquals(3, list.get(2).getRightElement());
    assertEquals(3, list.get(3).getLeftElement());
    assertEquals(2, list.get(3).getRightElement());
    assertEquals(2, list.get(4).getLeftElement());
    assertEquals(4, list.get(4).getRightElement());
    assertEquals(1, list.get(5).getLeftElement());
    assertEquals(1, list.get(5).getRightElement());

    list = fd.getEntries(Order.ByLeftElementAscending, 4);

    assertEquals(4, list.size());

    assertEquals(1, list.get(0).getLeftElement());
    assertEquals(1, list.get(0).getRightElement());
    assertEquals(2, list.get(1).getLeftElement());
    assertEquals(4, list.get(1).getRightElement());
    assertEquals(3, list.get(2).getLeftElement());
    assertEquals(2, list.get(2).getRightElement());
    assertEquals(4, list.get(3).getLeftElement());
    assertEquals(3, list.get(3).getRightElement());

    list = fd.getEntries(Order.ByLeftElementDescending, 4);

    assertEquals(4, list.size());

    assertEquals(6, list.get(0).getLeftElement());
    assertEquals(9, list.get(0).getRightElement());
    assertEquals(5, list.get(1).getLeftElement());
    assertEquals(7, list.get(1).getRightElement());
    assertEquals(4, list.get(2).getLeftElement());
    assertEquals(3, list.get(2).getRightElement());
    assertEquals(3, list.get(3).getLeftElement());
    assertEquals(2, list.get(3).getRightElement());
  }

  protected void testIterableCommon(Int2IntFrequencyDistribution fd) {
    fd.set(1, 1);
    fd.set(4, 3);
    fd.set(2, 4);
    fd.set(5, 7);
    fd.set(6, 9);
    fd.set(3, 2);

    assertEquals(6, fd.getNumberOfEvents());
    assertEquals(26, fd.getSumOfCounts());

    SortedSet<PairOfInts> list = new TreeSet<PairOfInts>();

    for (PairOfInts pair : fd) {
      list.add(pair.clone());
    }

    assertEquals(6, list.size());

    Iterator<PairOfInts> iter = list.iterator();
    PairOfInts e = iter.next();
    assertEquals(1, e.getLeftElement());
    assertEquals(1, e.getRightElement());
    e = iter.next();
    assertEquals(2, e.getLeftElement());
    assertEquals(4, e.getRightElement());
    e = iter.next();
    assertEquals(3, e.getLeftElement());
    assertEquals(2, e.getRightElement());
    e = iter.next();
    assertEquals(4, e.getLeftElement());
    assertEquals(3, e.getRightElement());
    e = iter.next();
    assertEquals(5, e.getLeftElement());
    assertEquals(7, e.getRightElement());
    e = iter.next();
    assertEquals(6, e.getLeftElement());
    assertEquals(9, e.getRightElement());
  }

  protected void testSerialization(Int2IntFrequencyDistribution fd,
      Class<? extends Int2IntFrequencyDistribution> cls) throws Exception {
    fd.set(1, 1);
    fd.set(4, 3);
    fd.set(2, 4);
    fd.set(5, 7);
    fd.set(6, 9);
    fd.set(3, 2);

    assertEquals(6, fd.getNumberOfEvents());
    assertEquals(26, fd.getSumOfCounts());

    ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(bytesOut);
    fd.write(dataOut);

    Int2IntFrequencyDistribution reconstructed = cls.newInstance();
    reconstructed.readFields(new DataInputStream(new ByteArrayInputStream(bytesOut.toByteArray())));

    assertFalse(fd == reconstructed);
    assertEquals(1, reconstructed.get(1));
    assertEquals(3, reconstructed.get(4));
    assertEquals(4, reconstructed.get(2));
    assertEquals(7, reconstructed.get(5));
    assertEquals(9, reconstructed.get(6));
    assertEquals(2, reconstructed.get(3));

    assertEquals(6, reconstructed.getNumberOfEvents());
    assertEquals(26, reconstructed.getSumOfCounts());
  }
}
