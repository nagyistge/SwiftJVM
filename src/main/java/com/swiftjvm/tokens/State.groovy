package com.swiftjvm.tokens

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
abstract class State {
	abstract Token nextToken(PushbackReader r, int c, Tokenizer t)
}
