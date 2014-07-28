package com.swiftjvm.tokens

import java.io.PushbackReader

class SlashState extends State {

	Token nextToken(PushbackReader r, int theSlash, Tokenizer t) {
		int c = r.read()
		if (c == '*') {
			return nextSlashStarToken(r, t)
		}
		if (c == '/') {
			return nextSlashSlashToken(r, t)
		}
		if (c >= 0) {
			r.unread(c)
		}
		return new Token(type:Type.SYMBOL, stringValue:"/")
	}
	
	Token nextSlashSlashToken(PushbackReader r, Tokenizer t) {
		int c
		while ((c = r.read()) != '\n' && c != '\r' && c >= 0) {
		}
		return t.nextToken()
	}
	
	Token nextSlashStarToken(PushbackReader r, Tokenizer t) {
		int c = 0
		int lastc = 0
		while (c >= 0) {
			if ((lastc == '*') && (c == '/')) {
				break
			}
			lastc = c
			c = r.read()
		}
		return t.nextToken()
	}

}
