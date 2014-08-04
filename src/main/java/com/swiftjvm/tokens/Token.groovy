package com.swiftjvm.tokens

import groovy.transform.Canonical
import groovy.transform.CompileStatic


@CompileStatic
@Canonical
class Token {
	static Token EOF = new Token(type:Type.EOF)
	
	Type type = Type.SYMBOL
	String stringValue
	double numberValue
	
	boolean is(Type type) {
		return this.type == type
	}
	
	String toString() {
		if (type == Type.EOF) {
			return "EOF"
		}
		return "${value().toString()}<${type.name()}>"
	}
	
	Object value() {
		if (type == Type.NUMBER) {
			return new Double(numberValue)
		}
		return stringValue ?: type.name()
	}
}
