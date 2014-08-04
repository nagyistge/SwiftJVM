package com.swiftjvm.tokens

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import java.io.PushbackReader

@CompileStatic
@Canonical
class SymbolState extends State {
	Trie symbols = new Trie()
	
	@CompileStatic
	static class Node {
		int c = 0
		Node parent
		Map<Integer, Node> children = [:]
		boolean valid = false
		
		Node deepestRead(PushbackReader r) {
			Integer c = r.read()
			Node n = children[c]
			if (n == null) {
				r.unread(c)
				return this
			}
			return n.deepestRead(r)
		}
		
		Node unreadToValid(PushbackReader r) {
			if (valid) {
				return this
			}
			r.unread(c)
			return parent.unreadToValid(r)
		}
		
		String ancestry() {
			if (parent) {
				return parent.ancestry() + String.valueOf((char)c)
			}
			return ""
		}
	}
	
	@CompileStatic
	static class Trie {
		Node root = new Node()
		
		void insert(String symbol) {
			Node node = root
			symbol.getChars().each { char c ->
				Node child = node.children[(int) c]
				if (child == null) {
					child = node.children[(int) c] = new Node(c: (int)c, parent: node)
				}
				node = child
			}
			node.valid = true
		}
		
		String nextSymbol(PushbackReader r, int first) {
			Node n1 = root.children[first].deepestRead(r)
			Node n2 = n1.unreadToValid(r)
			return n2.ancestry()
		}		
	}

	public SymbolState() {
		(0..255).each { int c ->
			add(String.valueOf((char) c))
		}
		add("!=")
		add("<=")
		add(">=")
		add("==")
		add("&&")
		add("||")
	}
	
	void add(String symbol) {
		symbols.insert(symbol)
	}
	
	Token nextToken(PushbackReader r, int c, Tokenizer t) {
		String s = symbols.nextSymbol(r, c)
		return new Token(type:Type.SYMBOL, stringValue:s)
	}

}
