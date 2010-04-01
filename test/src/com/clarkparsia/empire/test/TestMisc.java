/*
 * Copyright (c) 2009-2010 Clark & Parsia, LLC. <http://www.clarkparsia.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clarkparsia.empire.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.clarkparsia.empire.codegen.InstanceGenerator;
import com.clarkparsia.empire.test.api.TestInterface;
import com.clarkparsia.empire.SupportsRdfId;

import java.net.URI;

/**
 * <p>Various miscellaneous tests for non-JPA parts of the Empire API.</p>
 *
 * @author Michael Grove
 * @version 0.6.4
 * @since 0.6.4
 */
public class TestMisc {
	@Test
	public void testInstGen() throws Exception {
		Class<TestInterface> aIntClass = InstanceGenerator.generateInstanceClass(TestInterface.class);

		TestInterface aInt = aIntClass.newInstance();

		// this should successfully re-use the previously generated class file.  we want to make sure
		// this can happen without error.
		TestInterface aInt2 = InstanceGenerator.generateInstanceClass(TestInterface.class).newInstance();

		URI aURI = URI.create("urn:uri");
		Integer aNumber = 5;
		String aStr = "some string value";
		SupportsRdfId.RdfKey aKey = new SupportsRdfId.URIKey(URI.create("urn:id"));
		SupportsRdfId.RdfKey aKey2 = new SupportsRdfId.URIKey(URI.create("urn:id2"));

		aInt.setURI(aURI);
		aInt.setInt(aNumber);
		aInt.setString(aStr);
		aInt.setRdfId(aKey);

		aInt2.setRdfId(aKey2);

		aInt.setObject(aInt2);

		assertEquals(aInt, aInt);
		assertEquals(aURI, aInt.getURI());
		assertEquals(aStr, aInt.getString());
		assertEquals(aNumber, aInt.getInt());
		assertEquals(aKey, aInt.getRdfId());
		assertEquals(aInt2, aInt.getObject());
		assertEquals(aKey.toString(), aInt.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBadInstGen() throws Exception {
		InstanceGenerator.generateInstanceClass(BadTestInterface.class);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNoSupportsInstGen() throws Exception {
		InstanceGenerator.generateInstanceClass(NoSupportsTestInterface.class);
	}

	public interface NoSupportsTestInterface {
		public String getBar();
		public void setBar(String theStr);
	}
	
	public interface BadTestInterface extends SupportsRdfId {
		public void foo();

		public String getBar();
		public void setBar(String theStr);
	}
}