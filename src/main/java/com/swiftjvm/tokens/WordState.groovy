package com.swiftjvm.tokens

import groovy.transform.Canonical
import groovy.transform.CompileStatic

import java.io.PushbackReader

@CompileStatic
@Canonical
class WordState extends CharSequenceState {
	boolean[] wordChar = new boolean[256]
	
	public WordState() {
		setWordChars('a', 'z', true)
		setWordChars('A', 'Z', true)
		setWordChars('0', '9', true)
		setWordChars('-', '-', true)
		setWordChars('_', '_', true)
		setWordChars("'", "'", true)
		setWordChars(192, 255, true)
	}
	
	void setWordChars(Object from, Object to, boolean b) {
		int f = (int) from
		int t = (int) to
		(f..t).each { int i ->
			wordChar[i] = true
		}
	}
	
	boolean wordChar(int i) {
		if (i > 0 && i < 255) {
			return wordChar[i]
		}	
		return false
	}
	
	Token nextToken(PushbackReader r, int c, Tokenizer t) {
		int i = 0
		while(true) {
			checkBufLength(i)
			charbuf[i++] = c as char
			c = r.read()
			if (!wordChar(c)) {
				break
			}
		}
		if (c >= 0) {
			r.unread(c)
		}
		return new Token(type: Type.WORD, stringValue: String.copyValueOf(charbuf, 0, i))
	}
}
