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
		characterState[0..255] = symbolState
		characterState[0..' '] = whitespaceState
		characterState['a'..'z'] = wordState
		characterState['A'..'z'] = wordState
		characterState[192..255] = wordState
		characterState['0'..'9'] = numberState
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
}
