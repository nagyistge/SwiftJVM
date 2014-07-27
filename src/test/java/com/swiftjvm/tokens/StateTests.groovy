package com.swiftjvm.tokens

import static org.junit.Assert.*

import org.junit.Test

class StateTests {

	@Test
	void testQuoteState() {
		String s = "\"scott's\nquoted\" text"
		PushbackReader pr = new PushbackReader(new StringReader(s), 4)
		
		Token t = new QuoteState().nextToken(pr, pr.read(), null)
		
		assert t.toString() == "\"scott's\nquoted\""
	}

}
