// Generated from /home/anton/_University/5sem/MT/lab3/src/main/java/antlr/Grammar.g4 by ANTLR 4.9.2
package antlr;
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
		T__0=1, TAB=2, IF=3, ELSE=4, NEWLINE=5, LBRACKET=6, RBRACKET=7, COMPARE=8, 
		BITWISE=9, BOOLEAN=10, EQUAL=11, EQUALS=12, INPUT=13, PRINT=14, OPERATION=15, 
		VARIABLE=16, NUMBER=17;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "TAB", "IF", "ELSE", "NEWLINE", "LBRACKET", "RBRACKET", "COMPARE", 
			"BITWISE", "BOOLEAN", "EQUAL", "EQUALS", "INPUT", "PRINT", "OPERATION", 
			"VARIABLE", "NUMBER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':'", null, "'if '", "'else:'", "'\n'", "'('", "')'", null, null, 
			null, "' = '", null, null, "'print'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "TAB", "IF", "ELSE", "NEWLINE", "LBRACKET", "RBRACKET", "COMPARE", 
			"BITWISE", "BOOLEAN", "EQUAL", "EQUALS", "INPUT", "PRINT", "OPERATION", 
			"VARIABLE", "NUMBER"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\23\u00dd\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\3\3\3\3\3\3\3\6\3,\n\3\r\3\16\3-\3\4\3\4\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\tV\n\t"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\5\nj\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\5\13x\n\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\5\r\u008e\n\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\5\16\u00aa\n\16\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00be"+
		"\n\20\3\21\3\21\3\22\3\22\7\22\u00c4\n\22\f\22\16\22\u00c7\13\22\3\22"+
		"\3\22\7\22\u00cb\n\22\f\22\16\22\u00ce\13\22\5\22\u00d0\n\22\3\22\3\22"+
		"\3\22\3\22\3\22\7\22\u00d7\n\22\f\22\16\22\u00da\13\22\5\22\u00dc\n\22"+
		"\2\2\23\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22#\23\3\2\5\4\2C\\c|\3\2\63;\3\2\62;\2\u00f4\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"+
		"\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"+
		"\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3%\3\2"+
		"\2\2\5+\3\2\2\2\7/\3\2\2\2\t\63\3\2\2\2\139\3\2\2\2\r;\3\2\2\2\17=\3\2"+
		"\2\2\21U\3\2\2\2\23i\3\2\2\2\25w\3\2\2\2\27y\3\2\2\2\31\u008d\3\2\2\2"+
		"\33\u00a9\3\2\2\2\35\u00ab\3\2\2\2\37\u00bd\3\2\2\2!\u00bf\3\2\2\2#\u00db"+
		"\3\2\2\2%&\7<\2\2&\4\3\2\2\2\'(\7\"\2\2()\7\"\2\2)*\7\"\2\2*,\7\"\2\2"+
		"+\'\3\2\2\2,-\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\6\3\2\2\2/\60\7k\2\2\60\61"+
		"\7h\2\2\61\62\7\"\2\2\62\b\3\2\2\2\63\64\7g\2\2\64\65\7n\2\2\65\66\7u"+
		"\2\2\66\67\7g\2\2\678\7<\2\28\n\3\2\2\29:\7\f\2\2:\f\3\2\2\2;<\7*\2\2"+
		"<\16\3\2\2\2=>\7+\2\2>\20\3\2\2\2?@\7\"\2\2@A\7@\2\2AB\7?\2\2BV\7\"\2"+
		"\2CD\7\"\2\2DE\7>\2\2EF\7?\2\2FV\7\"\2\2GH\7\"\2\2HI\7@\2\2IV\7\"\2\2"+
		"JK\7\"\2\2KL\7>\2\2LV\7\"\2\2MN\7\"\2\2NO\7#\2\2OP\7?\2\2PV\7\"\2\2QR"+
		"\7\"\2\2RS\7?\2\2ST\7?\2\2TV\7\"\2\2U?\3\2\2\2UC\3\2\2\2UG\3\2\2\2UJ\3"+
		"\2\2\2UM\3\2\2\2UQ\3\2\2\2V\22\3\2\2\2WX\7\"\2\2XY\7c\2\2YZ\7p\2\2Z[\7"+
		"f\2\2[j\7\"\2\2\\]\7\"\2\2]^\7q\2\2^_\7t\2\2_j\7\"\2\2`a\7\"\2\2ab\7`"+
		"\2\2bj\7\"\2\2cd\7\"\2\2de\7(\2\2ej\7\"\2\2fg\7\"\2\2gh\7~\2\2hj\7\"\2"+
		"\2iW\3\2\2\2i\\\3\2\2\2i`\3\2\2\2ic\3\2\2\2if\3\2\2\2j\24\3\2\2\2kl\7"+
		"V\2\2lm\7t\2\2mn\7w\2\2no\7g\2\2ox\7\"\2\2pq\7\"\2\2qr\7H\2\2rs\7c\2\2"+
		"st\7n\2\2tu\7u\2\2uv\7g\2\2vx\7\"\2\2wk\3\2\2\2wp\3\2\2\2x\26\3\2\2\2"+
		"yz\7\"\2\2z{\7?\2\2{|\7\"\2\2|\30\3\2\2\2}~\7\"\2\2~\177\7/\2\2\177\u0080"+
		"\7?\2\2\u0080\u008e\7\"\2\2\u0081\u0082\7\"\2\2\u0082\u0083\7-\2\2\u0083"+
		"\u0084\7?\2\2\u0084\u008e\7\"\2\2\u0085\u0086\7\"\2\2\u0086\u0087\7,\2"+
		"\2\u0087\u0088\7?\2\2\u0088\u008e\7\"\2\2\u0089\u008a\7\"\2\2\u008a\u008b"+
		"\7\61\2\2\u008b\u008c\7?\2\2\u008c\u008e\7\"\2\2\u008d}\3\2\2\2\u008d"+
		"\u0081\3\2\2\2\u008d\u0085\3\2\2\2\u008d\u0089\3\2\2\2\u008e\32\3\2\2"+
		"\2\u008f\u0090\7k\2\2\u0090\u0091\7p\2\2\u0091\u0092\7v\2\2\u0092\u0093"+
		"\7*\2\2\u0093\u0094\7k\2\2\u0094\u0095\7p\2\2\u0095\u0096\7r\2\2\u0096"+
		"\u0097\7w\2\2\u0097\u0098\7v\2\2\u0098\u0099\7*\2\2\u0099\u009a\7+\2\2"+
		"\u009a\u00aa\7+\2\2\u009b\u009c\7h\2\2\u009c\u009d\7n\2\2\u009d\u009e"+
		"\7q\2\2\u009e\u009f\7c\2\2\u009f\u00a0\7v\2\2\u00a0\u00a1\7*\2\2\u00a1"+
		"\u00a2\7k\2\2\u00a2\u00a3\7p\2\2\u00a3\u00a4\7r\2\2\u00a4\u00a5\7w\2\2"+
		"\u00a5\u00a6\7v\2\2\u00a6\u00a7\7*\2\2\u00a7\u00a8\7+\2\2\u00a8\u00aa"+
		"\7+\2\2\u00a9\u008f\3\2\2\2\u00a9\u009b\3\2\2\2\u00aa\34\3\2\2\2\u00ab"+
		"\u00ac\7r\2\2\u00ac\u00ad\7t\2\2\u00ad\u00ae\7k\2\2\u00ae\u00af\7p\2\2"+
		"\u00af\u00b0\7v\2\2\u00b0\36\3\2\2\2\u00b1\u00b2\7\"\2\2\u00b2\u00b3\7"+
		"-\2\2\u00b3\u00be\7\"\2\2\u00b4\u00b5\7\"\2\2\u00b5\u00b6\7/\2\2\u00b6"+
		"\u00be\7\"\2\2\u00b7\u00b8\7\"\2\2\u00b8\u00b9\7,\2\2\u00b9\u00be\7\""+
		"\2\2\u00ba\u00bb\7\"\2\2\u00bb\u00bc\7\61\2\2\u00bc\u00be\7\"\2\2\u00bd"+
		"\u00b1\3\2\2\2\u00bd\u00b4\3\2\2\2\u00bd\u00b7\3\2\2\2\u00bd\u00ba\3\2"+
		"\2\2\u00be \3\2\2\2\u00bf\u00c0\t\2\2\2\u00c0\"\3\2\2\2\u00c1\u00c5\t"+
		"\3\2\2\u00c2\u00c4\t\4\2\2\u00c3\u00c2\3\2\2\2\u00c4\u00c7\3\2\2\2\u00c5"+
		"\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00cf\3\2\2\2\u00c7\u00c5\3\2"+
		"\2\2\u00c8\u00cc\7\60\2\2\u00c9\u00cb\t\4\2\2\u00ca\u00c9\3\2\2\2\u00cb"+
		"\u00ce\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00d0\3\2"+
		"\2\2\u00ce\u00cc\3\2\2\2\u00cf\u00c8\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0"+
		"\u00dc\3\2\2\2\u00d1\u00dc\7\62\2\2\u00d2\u00d3\7\62\2\2\u00d3\u00d4\7"+
		"\60\2\2\u00d4\u00d8\3\2\2\2\u00d5\u00d7\t\3\2\2\u00d6\u00d5\3\2\2\2\u00d7"+
		"\u00da\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00dc\3\2"+
		"\2\2\u00da\u00d8\3\2\2\2\u00db\u00c1\3\2\2\2\u00db\u00d1\3\2\2\2\u00db"+
		"\u00d2\3\2\2\2\u00dc$\3\2\2\2\17\2-Uiw\u008d\u00a9\u00bd\u00c5\u00cc\u00cf"+
		"\u00d8\u00db\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}