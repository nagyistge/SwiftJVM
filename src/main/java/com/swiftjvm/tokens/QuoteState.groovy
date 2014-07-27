package com.swiftjvm.tokens

import java.io.PushbackReader

class QuoteState extends CharSequenceState {

	Token nextToken(PushbackReader r, int cin, Tokenizer t) {
		int i = 0;
		charbuf[i++] = (char) cin;
		int c
		while (c != cin) {
			c = r.read()
			if (c < 0) {
				c = cin
			}
			checkBufLength(i);
			charbuf[i++] = (char) c
		} 
		return new Token(type:Type.QUOTED, stringValue:String.copyValueOf(charbuf, 0, i))
	}
	
}
