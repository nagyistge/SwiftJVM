package com.swiftjvm.tokens

import org.gmock.GMockTestCase
import org.junit.Test

class TokenizerTests extends GMockTestCase {
	
	@Test
	void testWords() {
		Tokenizer tr = new Tokenizer("The cat in the hat sat on the mat.")
		List<Token> tokens = []
		Token t
		while((t = tr.nextToken()) != Token.EOF) {
			tokens.add(t)
		}
		with(Type) {
			assert tokens.type == [WORD, WORD, WORD, WORD, WORD, WORD, WORD, WORD, WORD, SYMBOL]
		}
		assert tokens.collect { it.value() } == ["The", "cat", "in", "the", "hat", "sat", "on", "the", "mat", "."]
	}
	
	@Test
	void testNumbers() {
		Tokenizer tr = new Tokenizer("Add:1+2 Minus:1-3 Multiply:1*3 Divide:6/3=2")
		List<Token> tokens = []
		Token t
		while((t = tr.nextToken()) != Token.EOF) {
			tokens.add(t)
		}
		assert tokens.toString() == "[Add<WORD>, :<SYMBOL>, 1.0<NUMBER>, +<SYMBOL>,"+
			" 2.0<NUMBER>, Minus<WORD>, :<SYMBOL>, 1.0<NUMBER>, -3.0<NUMBER>, Multiply<WORD>,"+
			" :<SYMBOL>, 1.0<NUMBER>, *<SYMBOL>, 3.0<NUMBER>, Divide<WORD>, :<SYMBOL>,"+
			" 6.0<NUMBER>, /<SYMBOL>, 3.0<NUMBER>, =<SYMBOL>, 2.0<NUMBER>]"
	}
	
	@Test
	void testSymbols() {
		Tokenizer tr = new Tokenizer("if(3 < test && 4 >= test) {")
		List<Token> tokens = []
		Token t
		while((t = tr.nextToken()) != Token.EOF) {
			tokens.add(t)
		}
		assert tokens.toString() == "[if<WORD>, (<SYMBOL>, 3.0<NUMBER>, <<SYMBOL>,"+
			" test<WORD>, &&<SYMBOL>, 4.0<NUMBER>, >=<SYMBOL>, test<WORD>,"+
			" )<SYMBOL>, {<SYMBOL>]"
	}
	
	@Test
	void testQuoted() {
		Tokenizer tr = new Tokenizer(/"quoted text"'more quotes'"unclosed    /)
		List<Token> tokens = []
		Token t
		while((t = tr.nextToken()) != Token.EOF) {
			tokens.add(t)
		}
		assert tokens.toString() == $/["quoted text"<QUOTED>, 'more quotes'<QUOTED>, "unclosed    "<QUOTED>]/$
	}
	
	@Test
	void testComments() {
		Tokenizer tr = new Tokenizer("some words //comment one\n/*comment two */\n/* untermintate comment")
		List<Token> tokens = []
		Token t
		while((t = tr.nextToken()) != Token.EOF) {
			tokens.add(t)
		}
		assert tokens.toString() == "[some<WORD>, words<WORD>]"
	}
	
	
}
