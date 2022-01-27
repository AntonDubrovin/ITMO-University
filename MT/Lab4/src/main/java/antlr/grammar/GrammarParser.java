// Generated from /home/anton/_University/5sem/MT/Lab4/src/main/java/antlr/Grammar.g4 by ANTLR 4.9.2
package antlr.grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, STRING=6, OPERATIONS=7, DOT=8, 
		LBRACKET=9, RBRACKET=10, FIGURELBRACKET=11, FIGURERBRACKET=12, REGEX=13, 
		OR=14, RETURNTYPE=15, RETURNS=16, RETURN=17, SEMILOCON=18, WS=19, NEWLINE=20, 
		WORD=21;
	public static final int
		RULE_start = 0, RULE_firstLine = 1, RULE_code = 2, RULE_nonTerminal = 3, 
		RULE_rules = 4, RULE_ruleOnly = 5, RULE_oneRule = 6, RULE_receivedArguments = 7, 
		RULE_arguments = 8, RULE_argument = 9, RULE_returnType = 10, RULE_returnn = 11, 
		RULE_terminal = 12;
	private static String[] makeRuleNames() {
		return new String[] {
			"start", "firstLine", "code", "nonTerminal", "rules", "ruleOnly", "oneRule", 
			"receivedArguments", "arguments", "argument", "returnType", "returnn", 
			"terminal"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'grammar '", "': '", "'$'", "', '", "'''", null, null, "'.'", 
			"'('", "')'", "'{'", "'}'", null, "' | '", null, "'returns'", "'return'", 
			"';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, "STRING", "OPERATIONS", "DOT", "LBRACKET", 
			"RBRACKET", "FIGURELBRACKET", "FIGURERBRACKET", "REGEX", "OR", "RETURNTYPE", 
			"RETURNS", "RETURN", "SEMILOCON", "WS", "NEWLINE", "WORD"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Grammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public GrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StartContext extends ParserRuleContext {
		public FirstLineContext firstLine() {
			return getRuleContext(FirstLineContext.class,0);
		}
		public CodeContext code() {
			return getRuleContext(CodeContext.class,0);
		}
		public TerminalNode EOF() { return getToken(GrammarParser.EOF, 0); }
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitStart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			firstLine();
			setState(27);
			code();
			setState(28);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FirstLineContext extends ParserRuleContext {
		public TerminalNode WORD() { return getToken(GrammarParser.WORD, 0); }
		public TerminalNode SEMILOCON() { return getToken(GrammarParser.SEMILOCON, 0); }
		public TerminalNode NEWLINE() { return getToken(GrammarParser.NEWLINE, 0); }
		public FirstLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_firstLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterFirstLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitFirstLine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitFirstLine(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FirstLineContext firstLine() throws RecognitionException {
		FirstLineContext _localctx = new FirstLineContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_firstLine);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			match(T__0);
			setState(31);
			match(WORD);
			setState(32);
			match(SEMILOCON);
			setState(33);
			match(NEWLINE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodeContext extends ParserRuleContext {
		public List<TerminalContext> terminal() {
			return getRuleContexts(TerminalContext.class);
		}
		public TerminalContext terminal(int i) {
			return getRuleContext(TerminalContext.class,i);
		}
		public List<NonTerminalContext> nonTerminal() {
			return getRuleContexts(NonTerminalContext.class);
		}
		public NonTerminalContext nonTerminal(int i) {
			return getRuleContext(NonTerminalContext.class,i);
		}
		public CodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterCode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitCode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitCode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodeContext code() throws RecognitionException {
		CodeContext _localctx = new CodeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_code);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WORD) {
				{
				setState(37);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(35);
					terminal();
					}
					break;
				case 2:
					{
					setState(36);
					nonTerminal();
					}
					break;
				}
				}
				setState(41);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonTerminalContext extends ParserRuleContext {
		public TerminalNode WORD() { return getToken(GrammarParser.WORD, 0); }
		public RulesContext rules() {
			return getRuleContext(RulesContext.class,0);
		}
		public TerminalNode SEMILOCON() { return getToken(GrammarParser.SEMILOCON, 0); }
		public TerminalNode NEWLINE() { return getToken(GrammarParser.NEWLINE, 0); }
		public List<TerminalNode> WS() { return getTokens(GrammarParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(GrammarParser.WS, i);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public NonTerminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonTerminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterNonTerminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitNonTerminal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitNonTerminal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonTerminalContext nonTerminal() throws RecognitionException {
		NonTerminalContext _localctx = new NonTerminalContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_nonTerminal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			match(WORD);
			setState(44);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(43);
				match(WS);
				}
				break;
			}
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(46);
				arguments();
				}
			}

			setState(50);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(49);
				match(WS);
				}
			}

			setState(53);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RETURNS) {
				{
				setState(52);
				returnType();
				}
			}

			setState(55);
			match(T__1);
			setState(56);
			rules();
			setState(57);
			match(SEMILOCON);
			setState(58);
			match(NEWLINE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RulesContext extends ParserRuleContext {
		public List<RuleOnlyContext> ruleOnly() {
			return getRuleContexts(RuleOnlyContext.class);
		}
		public RuleOnlyContext ruleOnly(int i) {
			return getRuleContext(RuleOnlyContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(GrammarParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(GrammarParser.OR, i);
		}
		public RulesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rules; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterRules(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitRules(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitRules(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RulesContext rules() throws RecognitionException {
		RulesContext _localctx = new RulesContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_rules);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			ruleOnly();
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(61);
				match(OR);
				setState(62);
				ruleOnly();
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleOnlyContext extends ParserRuleContext {
		public List<TerminalNode> WS() { return getTokens(GrammarParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(GrammarParser.WS, i);
		}
		public List<OneRuleContext> oneRule() {
			return getRuleContexts(OneRuleContext.class);
		}
		public OneRuleContext oneRule(int i) {
			return getRuleContext(OneRuleContext.class,i);
		}
		public ReturnnContext returnn() {
			return getRuleContext(ReturnnContext.class,0);
		}
		public RuleOnlyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleOnly; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterRuleOnly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitRuleOnly(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitRuleOnly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleOnlyContext ruleOnly() throws RecognitionException {
		RuleOnlyContext _localctx = new RuleOnlyContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_ruleOnly);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(68);
				match(WS);
				}
			}

			setState(75); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(71);
				oneRule();
				setState(73);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
				case 1:
					{
					setState(72);
					match(WS);
					}
					break;
				}
				}
				}
				setState(77); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WORD );
			setState(80);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(79);
				match(WS);
				}
				break;
			}
			setState(83);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FIGURELBRACKET) {
				{
				setState(82);
				returnn();
				}
			}

			setState(86);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(85);
				match(WS);
				}
			}

			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OneRuleContext extends ParserRuleContext {
		public TerminalNode WORD() { return getToken(GrammarParser.WORD, 0); }
		public ReceivedArgumentsContext receivedArguments() {
			return getRuleContext(ReceivedArgumentsContext.class,0);
		}
		public OneRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oneRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterOneRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitOneRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitOneRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OneRuleContext oneRule() throws RecognitionException {
		OneRuleContext _localctx = new OneRuleContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_oneRule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(WORD);
			setState(90);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(89);
				receivedArguments();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReceivedArgumentsContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(GrammarParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(GrammarParser.RBRACKET, 0); }
		public List<TerminalNode> WORD() { return getTokens(GrammarParser.WORD); }
		public TerminalNode WORD(int i) {
			return getToken(GrammarParser.WORD, i);
		}
		public List<TerminalNode> DOT() { return getTokens(GrammarParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(GrammarParser.DOT, i);
		}
		public List<TerminalNode> OPERATIONS() { return getTokens(GrammarParser.OPERATIONS); }
		public TerminalNode OPERATIONS(int i) {
			return getToken(GrammarParser.OPERATIONS, i);
		}
		public List<TerminalNode> WS() { return getTokens(GrammarParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(GrammarParser.WS, i);
		}
		public ReceivedArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_receivedArguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterReceivedArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitReceivedArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitReceivedArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReceivedArgumentsContext receivedArguments() throws RecognitionException {
		ReceivedArgumentsContext _localctx = new ReceivedArgumentsContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_receivedArguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(LBRACKET);
			setState(94); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(93);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << OPERATIONS) | (1L << DOT) | (1L << WS) | (1L << WORD))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(96); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << OPERATIONS) | (1L << DOT) | (1L << WS) | (1L << WORD))) != 0) );
			setState(98);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(GrammarParser.LBRACKET, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public TerminalNode RBRACKET() { return getToken(GrammarParser.RBRACKET, 0); }
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(LBRACKET);
			setState(101);
			argument();
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(102);
				match(T__3);
				setState(103);
				argument();
				}
				}
				setState(108);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(109);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentContext extends ParserRuleContext {
		public TerminalNode WORD() { return getToken(GrammarParser.WORD, 0); }
		public TerminalNode RETURNTYPE() { return getToken(GrammarParser.RETURNTYPE, 0); }
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			match(WORD);
			setState(112);
			match(T__1);
			setState(113);
			match(RETURNTYPE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnTypeContext extends ParserRuleContext {
		public TerminalNode RETURNS() { return getToken(GrammarParser.RETURNS, 0); }
		public TerminalNode RETURNTYPE() { return getToken(GrammarParser.RETURNTYPE, 0); }
		public TerminalNode WS() { return getToken(GrammarParser.WS, 0); }
		public ReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitReturnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitReturnType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnTypeContext returnType() throws RecognitionException {
		ReturnTypeContext _localctx = new ReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_returnType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			match(RETURNS);
			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(116);
				match(WS);
				}
			}

			setState(119);
			match(RETURNTYPE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnnContext extends ParserRuleContext {
		public TerminalNode FIGURELBRACKET() { return getToken(GrammarParser.FIGURELBRACKET, 0); }
		public TerminalNode RETURN() { return getToken(GrammarParser.RETURN, 0); }
		public TerminalNode FIGURERBRACKET() { return getToken(GrammarParser.FIGURERBRACKET, 0); }
		public TerminalNode WS() { return getToken(GrammarParser.WS, 0); }
		public List<TerminalNode> WORD() { return getTokens(GrammarParser.WORD); }
		public TerminalNode WORD(int i) {
			return getToken(GrammarParser.WORD, i);
		}
		public List<TerminalNode> DOT() { return getTokens(GrammarParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(GrammarParser.DOT, i);
		}
		public List<TerminalNode> LBRACKET() { return getTokens(GrammarParser.LBRACKET); }
		public TerminalNode LBRACKET(int i) {
			return getToken(GrammarParser.LBRACKET, i);
		}
		public List<TerminalNode> RBRACKET() { return getTokens(GrammarParser.RBRACKET); }
		public TerminalNode RBRACKET(int i) {
			return getToken(GrammarParser.RBRACKET, i);
		}
		public ReturnnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnn; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterReturnn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitReturnn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitReturnn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnnContext returnn() throws RecognitionException {
		ReturnnContext _localctx = new ReturnnContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_returnn);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(FIGURELBRACKET);
			setState(122);
			match(RETURN);
			setState(124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(123);
				match(WS);
				}
			}

			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(126);
				match(T__2);
				}
			}

			setState(130); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(129);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOT) | (1L << LBRACKET) | (1L << RBRACKET) | (1L << WORD))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(132); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOT) | (1L << LBRACKET) | (1L << RBRACKET) | (1L << WORD))) != 0) );
			setState(134);
			match(FIGURERBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TerminalContext extends ParserRuleContext {
		public TerminalNode WORD() { return getToken(GrammarParser.WORD, 0); }
		public TerminalNode SEMILOCON() { return getToken(GrammarParser.SEMILOCON, 0); }
		public TerminalNode NEWLINE() { return getToken(GrammarParser.NEWLINE, 0); }
		public TerminalNode STRING() { return getToken(GrammarParser.STRING, 0); }
		public TerminalNode REGEX() { return getToken(GrammarParser.REGEX, 0); }
		public TerminalNode OPERATIONS() { return getToken(GrammarParser.OPERATIONS, 0); }
		public List<TerminalNode> WS() { return getTokens(GrammarParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(GrammarParser.WS, i);
		}
		public TerminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterTerminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitTerminal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GrammarVisitor ) return ((GrammarVisitor<? extends T>)visitor).visitTerminal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TerminalContext terminal() throws RecognitionException {
		TerminalContext _localctx = new TerminalContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_terminal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			match(WORD);
			setState(137);
			match(T__1);
			setState(139);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(138);
				match(WS);
				}
			}

			setState(146);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				{
				setState(141);
				match(STRING);
				}
				break;
			case REGEX:
				{
				setState(142);
				match(REGEX);
				}
				break;
			case T__4:
				{
				setState(143);
				match(T__4);
				setState(144);
				match(OPERATIONS);
				setState(145);
				match(T__4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(148);
				match(WS);
				}
			}

			setState(151);
			match(SEMILOCON);
			setState(152);
			match(NEWLINE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\27\u009d\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3"+
		"\4\3\4\7\4(\n\4\f\4\16\4+\13\4\3\5\3\5\5\5/\n\5\3\5\5\5\62\n\5\3\5\5\5"+
		"\65\n\5\3\5\5\58\n\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\7\6B\n\6\f\6\16\6"+
		"E\13\6\3\7\5\7H\n\7\3\7\3\7\5\7L\n\7\6\7N\n\7\r\7\16\7O\3\7\5\7S\n\7\3"+
		"\7\5\7V\n\7\3\7\5\7Y\n\7\3\b\3\b\5\b]\n\b\3\t\3\t\6\ta\n\t\r\t\16\tb\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\7\nk\n\n\f\n\16\nn\13\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\f\3\f\5\fx\n\f\3\f\3\f\3\r\3\r\3\r\5\r\177\n\r\3\r\5\r\u0082\n"+
		"\r\3\r\6\r\u0085\n\r\r\r\16\r\u0086\3\r\3\r\3\16\3\16\3\16\5\16\u008e"+
		"\n\16\3\16\3\16\3\16\3\16\3\16\5\16\u0095\n\16\3\16\5\16\u0098\n\16\3"+
		"\16\3\16\3\16\3\16\2\2\17\2\4\6\b\n\f\16\20\22\24\26\30\32\2\4\6\2\5\5"+
		"\t\n\25\25\27\27\4\2\n\f\27\27\2\u00a7\2\34\3\2\2\2\4 \3\2\2\2\6)\3\2"+
		"\2\2\b,\3\2\2\2\n>\3\2\2\2\fG\3\2\2\2\16Z\3\2\2\2\20^\3\2\2\2\22f\3\2"+
		"\2\2\24q\3\2\2\2\26u\3\2\2\2\30{\3\2\2\2\32\u008a\3\2\2\2\34\35\5\4\3"+
		"\2\35\36\5\6\4\2\36\37\7\2\2\3\37\3\3\2\2\2 !\7\3\2\2!\"\7\27\2\2\"#\7"+
		"\24\2\2#$\7\26\2\2$\5\3\2\2\2%(\5\32\16\2&(\5\b\5\2\'%\3\2\2\2\'&\3\2"+
		"\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*\7\3\2\2\2+)\3\2\2\2,.\7\27\2\2-/"+
		"\7\25\2\2.-\3\2\2\2./\3\2\2\2/\61\3\2\2\2\60\62\5\22\n\2\61\60\3\2\2\2"+
		"\61\62\3\2\2\2\62\64\3\2\2\2\63\65\7\25\2\2\64\63\3\2\2\2\64\65\3\2\2"+
		"\2\65\67\3\2\2\2\668\5\26\f\2\67\66\3\2\2\2\678\3\2\2\289\3\2\2\29:\7"+
		"\4\2\2:;\5\n\6\2;<\7\24\2\2<=\7\26\2\2=\t\3\2\2\2>C\5\f\7\2?@\7\20\2\2"+
		"@B\5\f\7\2A?\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\13\3\2\2\2EC\3\2\2"+
		"\2FH\7\25\2\2GF\3\2\2\2GH\3\2\2\2HM\3\2\2\2IK\5\16\b\2JL\7\25\2\2KJ\3"+
		"\2\2\2KL\3\2\2\2LN\3\2\2\2MI\3\2\2\2NO\3\2\2\2OM\3\2\2\2OP\3\2\2\2PR\3"+
		"\2\2\2QS\7\25\2\2RQ\3\2\2\2RS\3\2\2\2SU\3\2\2\2TV\5\30\r\2UT\3\2\2\2U"+
		"V\3\2\2\2VX\3\2\2\2WY\7\25\2\2XW\3\2\2\2XY\3\2\2\2Y\r\3\2\2\2Z\\\7\27"+
		"\2\2[]\5\20\t\2\\[\3\2\2\2\\]\3\2\2\2]\17\3\2\2\2^`\7\13\2\2_a\t\2\2\2"+
		"`_\3\2\2\2ab\3\2\2\2b`\3\2\2\2bc\3\2\2\2cd\3\2\2\2de\7\f\2\2e\21\3\2\2"+
		"\2fg\7\13\2\2gl\5\24\13\2hi\7\6\2\2ik\5\24\13\2jh\3\2\2\2kn\3\2\2\2lj"+
		"\3\2\2\2lm\3\2\2\2mo\3\2\2\2nl\3\2\2\2op\7\f\2\2p\23\3\2\2\2qr\7\27\2"+
		"\2rs\7\4\2\2st\7\21\2\2t\25\3\2\2\2uw\7\22\2\2vx\7\25\2\2wv\3\2\2\2wx"+
		"\3\2\2\2xy\3\2\2\2yz\7\21\2\2z\27\3\2\2\2{|\7\r\2\2|~\7\23\2\2}\177\7"+
		"\25\2\2~}\3\2\2\2~\177\3\2\2\2\177\u0081\3\2\2\2\u0080\u0082\7\5\2\2\u0081"+
		"\u0080\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0084\3\2\2\2\u0083\u0085\t\3"+
		"\2\2\u0084\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0084\3\2\2\2\u0086"+
		"\u0087\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u0089\7\16\2\2\u0089\31\3\2\2"+
		"\2\u008a\u008b\7\27\2\2\u008b\u008d\7\4\2\2\u008c\u008e\7\25\2\2\u008d"+
		"\u008c\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u0094\3\2\2\2\u008f\u0095\7\b"+
		"\2\2\u0090\u0095\7\17\2\2\u0091\u0092\7\7\2\2\u0092\u0093\7\t\2\2\u0093"+
		"\u0095\7\7\2\2\u0094\u008f\3\2\2\2\u0094\u0090\3\2\2\2\u0094\u0091\3\2"+
		"\2\2\u0095\u0097\3\2\2\2\u0096\u0098\7\25\2\2\u0097\u0096\3\2\2\2\u0097"+
		"\u0098\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009a\7\24\2\2\u009a\u009b\7"+
		"\26\2\2\u009b\33\3\2\2\2\31\').\61\64\67CGKORUX\\blw~\u0081\u0086\u008d"+
		"\u0094\u0097";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}