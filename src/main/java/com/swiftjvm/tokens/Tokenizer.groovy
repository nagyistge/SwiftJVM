package com.swiftjvm.tokens

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class Tokenizer {
	PushbackReader pushbackReader
	State[] characterState = new State[256]
	NumberState numberState = new NumberState()
	QuoteState quoteState = new QuoteState()
	SlashState slashState = new SlashState()
	WhitespaceState whitespaceState = new WhitespaceState()
	SymbolState symbolState = new SymbolState()
	WordState wordState = new WordState()
	
	public Tokenizer() {
		characterStateRange(0, 255, symbolState)
		characterStateRange(0, ' ', whitespaceState)
		characterStateRange('a', 'z', wordState)
		characterStateRange('A', 'z', wordState)
		characterStateRange(192, 255, wordState)
		characterStateRange('0', '9', numberState)
		characterState['-'] = numberState
		characterState['.'] = numberState
		characterState['"'] = quoteState
		characterState["'"] = quoteState
		characterState["/"] = slashState
	}
	
	public Tokenizer(String s) {
		this()
		this.pushbackReader = new PushbackReader(new StringReader(s), 4)
	}
	
	Token nextToken() throws IOException {
		int c = pushbackReader.read()
		 
		if (c >= 0 && c < characterState.length) {
			return characterState[c].nextToken(pushbackReader, c, this)
		}
		return Token.EOF
	}
	
	void characterStateRange(Object from, Object to, State state) {
		((from as int)..(to as int)).each { Integer item ->
			characterState[item] = state 
		}
	}
}
