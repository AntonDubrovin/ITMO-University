// Generated from /home/anton/_University/5sem/MT/Lab4/src/main/java/antlr/Grammar.g4 by ANTLR 4.9.2
package antlr.grammar;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GrammarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, STRING=6, OPERATIONS=7, DOT=8, 
		LBRACKET=9, RBRACKET=10, FIGURELBRACKET=11, FIGURERBRACKET=12, REGEX=13, 
		OR=14, RETURNTYPE=15, RETURNS=16, RETURN=17, SEMILOCON=18, WS=19, NEWLINE=20, 
		WORD=21;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "STRING", "OPERATIONS", "DOT", 
			"LBRACKET", "RBRACKET", "FIGURELBRACKET", "FIGURERBRACKET", "REGEX", 
			"OR", "RETURNTYPE", "RETURNS", "RETURN", "SEMILOCON", "WS", "NEWLINE", 
			"WORD"
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


	public GrammarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Grammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\27\u00a0\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\6\7F\n\7\r\7\16\7G\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3"+
		"\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16"+
		"c\n\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20"+
		"\177\n\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\23\3\23\3\24\6\24\u0093\n\24\r\24\16\24\u0094\3\25\6"+
		"\25\u0098\n\25\r\25\16\25\u0099\3\26\6\26\u009d\n\26\r\26\16\26\u009e"+
		"\2\2\27\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22#\23%\24\'\25)\26+\27\3\2\4\5\2,-//\61\61\6\2\62;C\\a"+
		"ac|\2\u00aa\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2"+
		"\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\3-\3\2\2\2"+
		"\5\66\3\2\2\2\79\3\2\2\2\t;\3\2\2\2\13>\3\2\2\2\r@\3\2\2\2\17K\3\2\2\2"+
		"\21M\3\2\2\2\23O\3\2\2\2\25Q\3\2\2\2\27S\3\2\2\2\31U\3\2\2\2\33b\3\2\2"+
		"\2\35d\3\2\2\2\37~\3\2\2\2!\u0080\3\2\2\2#\u0088\3\2\2\2%\u008f\3\2\2"+
		"\2\'\u0092\3\2\2\2)\u0097\3\2\2\2+\u009c\3\2\2\2-.\7i\2\2./\7t\2\2/\60"+
		"\7c\2\2\60\61\7o\2\2\61\62\7o\2\2\62\63\7c\2\2\63\64\7t\2\2\64\65\7\""+
		"\2\2\65\4\3\2\2\2\66\67\7<\2\2\678\7\"\2\28\6\3\2\2\29:\7&\2\2:\b\3\2"+
		"\2\2;<\7.\2\2<=\7\"\2\2=\n\3\2\2\2>?\7)\2\2?\f\3\2\2\2@E\7)\2\2AF\5+\26"+
		"\2BF\5\23\n\2CF\5\25\13\2DF\7\"\2\2EA\3\2\2\2EB\3\2\2\2EC\3\2\2\2ED\3"+
		"\2\2\2FG\3\2\2\2GE\3\2\2\2GH\3\2\2\2HI\3\2\2\2IJ\7)\2\2J\16\3\2\2\2KL"+
		"\t\2\2\2L\20\3\2\2\2MN\7\60\2\2N\22\3\2\2\2OP\7*\2\2P\24\3\2\2\2QR\7+"+
		"\2\2R\26\3\2\2\2ST\7}\2\2T\30\3\2\2\2UV\7\177\2\2V\32\3\2\2\2WX\7]\2\2"+
		"XY\7c\2\2YZ\7/\2\2Z[\7|\2\2[c\7_\2\2\\]\7]\2\2]^\7\62\2\2^_\7/\2\2_`\7"+
		";\2\2`a\7_\2\2ac\7-\2\2bW\3\2\2\2b\\\3\2\2\2c\34\3\2\2\2de\7\"\2\2ef\7"+
		"~\2\2fg\7\"\2\2g\36\3\2\2\2hi\7K\2\2ij\7p\2\2j\177\7v\2\2kl\7U\2\2lm\7"+
		"v\2\2mn\7t\2\2no\7k\2\2op\7p\2\2p\177\7i\2\2qr\7D\2\2rs\7q\2\2st\7q\2"+
		"\2tu\7n\2\2uv\7g\2\2vw\7c\2\2w\177\7p\2\2xy\7F\2\2yz\7q\2\2z{\7w\2\2{"+
		"|\7d\2\2|}\7n\2\2}\177\7g\2\2~h\3\2\2\2~k\3\2\2\2~q\3\2\2\2~x\3\2\2\2"+
		"\177 \3\2\2\2\u0080\u0081\7t\2\2\u0081\u0082\7g\2\2\u0082\u0083\7v\2\2"+
		"\u0083\u0084\7w\2\2\u0084\u0085\7t\2\2\u0085\u0086\7p\2\2\u0086\u0087"+
		"\7u\2\2\u0087\"\3\2\2\2\u0088\u0089\7t\2\2\u0089\u008a\7g\2\2\u008a\u008b"+
		"\7v\2\2\u008b\u008c\7w\2\2\u008c\u008d\7t\2\2\u008d\u008e\7p\2\2\u008e"+
		"$\3\2\2\2\u008f\u0090\7=\2\2\u0090&\3\2\2\2\u0091\u0093\7\"\2\2\u0092"+
		"\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0092\3\2\2\2\u0094\u0095\3\2"+
		"\2\2\u0095(\3\2\2\2\u0096\u0098\7\f\2\2\u0097\u0096\3\2\2\2\u0098\u0099"+
		"\3\2\2\2\u0099\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a*\3\2\2\2\u009b"+
		"\u009d\t\3\2\2\u009c\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009c\3\2"+
		"\2\2\u009e\u009f\3\2\2\2\u009f,\3\2\2\2\n\2EGb~\u0094\u0099\u009e\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}