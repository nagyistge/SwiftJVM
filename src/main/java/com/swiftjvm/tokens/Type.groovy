package com.swiftjvm.tokens

import groovy.transform.CompileStatic

@CompileStatic
enum Type {
	EOF,
	NUMBER,
	WORD,
	SYMBOL,
	QUOTED,
	RESERVED
}
