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
			String s = "     test"
			PushbackReader pr = new PushbackReader(new StringReader(s), 4)
			Token t = new WhitespaceState().nextToken(pr, pr.read(), mock)
			
			assert pr.readLine() == "test"
			assert t.type == Type.WORD
			assert t.numberValue == 0
			assert t.stringValue == "test"
		}
	}
	
	@Test 
	void testSlashState() {
		String s = "/what after"
		PushbackReader pr = new PushbackReader(new StringReader(s), 4)
		Token t = new SlashState().nextToken(pr, pr.read(), null)
		
		assert pr.readLine() == "what after"
		assert t.type == Type.SYMBOL
		assert t.numberValue == 0
		assert t.stringValue == "/"
	}
	
	@Test
	void testSlashSlashState() {
		Tokenizer mock = mock(Tokenizer.class)
		mock.nextToken().returns(new Token(type:Type.WORD, stringValue:"after"))
		play {
			String s = "//what\nafter"
			PushbackReader pr = new PushbackReader(new StringReader(s), 4)
			Token t = new SlashState().nextToken(pr, pr.read(), mock)
			
			assert pr.readLine() == "after"
			assert t.type == Type.WORD
			assert t.numberValue == 0
			assert t.stringValue == "after"
		}
	}
	
	@Test
	void testSlashStarState() {
		Tokenizer mock = mock(Tokenizer.class)
		mock.nextToken().returns(new Token(type:Type.WORD, stringValue:"after"))
		play {
			String s = "/*what\nafter*/next"
					PushbackReader pr = new PushbackReader(new StringReader(s), 4)
			Token t = new SlashState().nextToken(pr, pr.read(), mock)
			
			assert pr.readLine() == "next"
			assert t.type == Type.WORD
			assert t.numberValue == 0
			assert t.stringValue == "after"
		}
	}
	
	@Test
	void testSymbolState() {
		PushbackReader pr = new PushbackReader(new StringReader("/2343"))
		Token t = new SymbolState().nextToken(pr, pr.read(), null)
		assert pr.readLine() == "2343"
		assert t.type == Type.SYMBOL
		assert t.numberValue == 0
		assert t.stringValue == "/"
		
		pr = new PushbackReader(new StringReader("!=true"))
		t = new SymbolState().nextToken(pr, pr.read(), null)
		assert pr.readLine() == "true"
		assert t.type == Type.SYMBOL
		assert t.numberValue == 0
		assert t.stringValue == "!="
		
		pr = new PushbackReader(new StringReader("==false"))
		t = new SymbolState().nextToken(pr, pr.read(), null)
		assert pr.readLine() == "false"
		assert t.type == Type.SYMBOL
		assert t.numberValue == 0
		assert t.stringValue == "=="
		
		pr = new PushbackReader(new StringReader("!=true"))
		t = new SymbolState().nextToken(pr, pr.read(), null)
		assert pr.readLine() == "true"
		assert t.type == Type.SYMBOL
		assert t.numberValue == 0
		assert t.stringValue == "!="
		
		pr = new PushbackReader(new StringReader(">=1"))
		t = new SymbolState().nextToken(pr, pr.read(), null)
		assert pr.readLine() == "1"
		assert t.type == Type.SYMBOL
		assert t.numberValue == 0
		assert t.stringValue == ">="
	}
	
	@Test
	void testNumberState() {
		PushbackReader pr = new PushbackReader(new StringReader("-.test"), 4)
		int minusChar = '-' as char
		SymbolState symbolState = mock(SymbolState.class)
		Tokenizer mock = mock(Tokenizer.class)

		symbolState.nextToken(pr, minusChar, mock).returns(new Token(type:Type.SYMBOL, stringValue:"-"))
		mock.getSymbolState().returns((SymbolState) symbolState)
		
		play {
			Token t = new NumberState().nextToken(pr, pr.read(), mock)
			assert pr.readLine() == ".test"
			assert t.type == Type.SYMBOL
			assert t.numberValue == 0
			assert t.stringValue == "-"
		}
		
		pr = new PushbackReader(new StringReader("123+1234"), 4)
		Token t = new NumberState().nextToken(pr, pr.read(), mock)
		assert pr.readLine() == "+1234"
		assert t.type == Type.NUMBER
		assert t.numberValue == 123.0
		assert t.stringValue == null

		pr = new PushbackReader(new StringReader("-123.678+4"), 4)
		t = new NumberState().nextToken(pr, pr.read(), mock)
		assert pr.readLine() == "+4"
		assert t.type == Type.NUMBER
		assert t.numberValue == -123.678
		assert t.stringValue == null
		
		pr = new PushbackReader(new StringReader(".32342.123"), 4)
		t = new NumberState().nextToken(pr, pr.read(), mock)
		assert pr.readLine() == ".123"
		assert t.type == Type.NUMBER
		assert t.numberValue == 0.32342
		assert t.stringValue == null
		
		pr = new PushbackReader(new StringReader("100001.00"), 4)
		t = new NumberState().nextToken(pr, pr.read(), mock)
		assert pr.readLine() == 0xFFFF
		assert t.type == Type.NUMBER
		assert t.numberValue == 100001.00
		assert t.stringValue == null
	}
	
}
