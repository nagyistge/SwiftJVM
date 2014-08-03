package com.swiftjvm.tokens

import groovy.transform.Canonical
import groovy.transform.CompileStatic

import java.io.PushbackReader


@CompileStatic
@Canonical
class NumberState extends State {
	int c
	double value
	boolean absorbedLeadingMinus
	boolean absorbedDot
	boolean gotAdigit

	Token nextToken(PushbackReader r, int cin, Tokenizer t) {
		reset(cin)
		parseLeft(r)
		parseRight(r)
		r.unread(c)
		return value(r, t)
	}
		
	double absorbDigits(PushbackReader r, boolean fraction) {
		int divideBy = 1
		int zeroChar = '0' as char
		int nineChar = '9' as char
		double v = 0
		while ( zeroChar <= c && c <= nineChar) {
			gotAdigit = true
			v = v * 10 + (c - ('0' as char))
			c = r.read()
			if (fraction) {
				divideBy *= 10
			}
		}
		if (fraction) {
			v = v / divideBy
		}
		return v
	}
	
	void parseLeft(PushbackReader r) {
		if (c == ('-' as char)) {
			c = r.read()
			absorbedLeadingMinus = true
		}
		value = absorbDigits(r, false)
	}
	
	void parseRight(PushbackReader r) {
		if (c == ('.' as char)) {
			c = r.read()
			absorbedDot = true
			value += absorbDigits(r, true)
		}
	}
	
	void reset(int cin) {
		c = cin
		value = 0
		absorbedLeadingMinus = false
		absorbedDot = false
		gotAdigit = false
	}
	
	Token value(PushbackReader r, Tokenizer t) {
		int minusChar = '-' as char
		int dotChar = '.' as char
		if (!gotAdigit) {
			if (absorbedLeadingMinus && absorbedDot) {
				r.unread('.' as char)
				return t.symbolState.nextToken(r, minusChar, t)
			}
			if (absorbedLeadingMinus) {
				return t.symbolState.nextToken(r, minusChar, t)
			}
			if (absorbedDot) {
				return t.symbolState.nextToken(r, dotChar, t)
			}
		}
		if (absorbedLeadingMinus) {
			value = -value
		}
		return new Token(Type:Type.NUMBER, numberValue:value)
	}
}
