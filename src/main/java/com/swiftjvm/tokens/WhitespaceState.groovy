package com.swiftjvm.tokens

import java.io.PushbackReader

class WhitespaceState extends State {
	boolean[] whitespaceChar = new boolean[256]
	
	public WhitespaceState() {
		setWhitespaceChars(0, ' ', true);
	}
	
	void setWhitespaceChars(Object from, Object to, boolean b) {
		int f = (int) from
		int t = (int) to
		(f..t).each { int i ->
			whitespaceChar[i] = true
		}
	}
	
	boolean whitespaceChar(int i) {
		if (i > 0 && i < 255) {
			return whitespaceChar[i]
		}
		return false
	}
	
	Token nextToken(PushbackReader r, int c, Tokenizer t) {
		while (true) {
			c = r.read()
			if (!whitespaceChar(c)) {
				break
			}
		} 
		if (c >= 0) {
			r.unread(c);
		}
		return t.nextToken();
	}

}
