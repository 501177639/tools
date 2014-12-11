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

package tl.lin.data.pair;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.junit.Test;

public class PairOfWritablesTest {

  @Test
  public void testBasic() throws IOException {
    PairOfWritables<Text, IntWritable> pair1 = new PairOfWritables<Text, IntWritable>(
        new Text("hi"), new IntWritable(1));

    assertEquals(new Text("hi"), pair1.getLeftElement());
    assertEquals(new IntWritable(1), pair1.getRightElement());

    PairOfWritables<IntWritable, FloatWritable> pair2 = new PairOfWritables<IntWritable, FloatWritable>(
        new IntWritable(1), new FloatWritable(1.0f));

    assertEquals(new IntWritable(1), pair2.getLeftElement());
    assertEquals(new FloatWritable(1.0f), pair2.getRightElement());
  }

  @Test
  public void testSerialize() throws IOException {
    PairOfWritables<Text, IntWritable> origPair = new PairOfWritables<Text, IntWritable>(new Text(
        "hi"), new IntWritable(1));

    ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(bytesOut);

    origPair.write(dataOut);

    PairOfWritables<Text, IntWritable> pair = new PairOfWritables<Text, IntWritable>();

    pair.readFields(new DataInputStream(new ByteArrayInputStream(bytesOut.toByteArray())));

    assertEquals(new Text("hi"), pair.getLeftElement());
    assertEquals(new IntWritable(1), pair.getRightElement());
  }

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(PairOfWritablesTest.class);
  }
}
