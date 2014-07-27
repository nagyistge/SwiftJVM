package com.swiftjvm.tokens

import groovy.transform.Canonical
import groovy.transform.CompileStatic


@CompileStatic
@Canonical
abstract class CharSequenceState extends State {
	char[] charbuf = new char[16]
	
	void checkBufLength(int i) {
		if (i >= charbuf.length) {
			char[] nb = new char[charbuf.length * 2]
			System.arraycopy(charbuf, 0, nb, 0, charbuf.length)
			charbuf = nb
		}
	}
}
