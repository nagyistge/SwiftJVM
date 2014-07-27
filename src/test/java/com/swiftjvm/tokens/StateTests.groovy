package com.swiftjvm.tokens

import static org.junit.Assert.*

import org.gmock.GMockTestCase
import org.junit.Test

class StateTests extends GMockTestCase {
	
	@Test
	void testQuoteState() {
		String s = "\"scott's\nquoted\" text"
		PushbackReader pr = new PushbackReader(new StringReader(s), 4)
		
		Token t = new QuoteState().nextToken(pr, pr.read(), null)
		
		assert t.type == Type.QUOTED
		assert t.numberValue == 0
		assert t.stringValue == "\"scott's\nquoted\""
	}

	@Test
	void testWordState() {
		String s = "Startof_asaword then something"
		PushbackReader pr = new PushbackReader(new StringReader(s), 4)
		
		Token t = new WordState().nextToken(pr, pr.read(), null)
		
		assert t.type == Type.WORD
		assert t.numberValue == 0
		assert t.stringValue == "Startof_asaword"
	}
	
	@Test
	void testWhitespcaeState() {
		Tokenizer mock = mock(Tokenizer.class)
		mock.nextToken().returns(new Token(type:Type.WORD, stringValue:"test"))
		play {
			PushbackReader pr = new PushbackReader(new StringReader("     test"), 4)
			Token t = new WhitespaceState().nextToken(pr, pr.read(), mock)
			
			assert t.type == Type.WORD
			assert t.numberValue == 0
			assert t.stringValue == "test"
		}
	}
}
