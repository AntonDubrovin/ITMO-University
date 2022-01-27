// Generated from /home/anton/_University/5sem/MT/Lab4/src/main/java/antlr/Grammar.g4 by ANTLR 4.9.2
package antlr.grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GrammarParser}.
 */
public interface GrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GrammarParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(GrammarParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(GrammarParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#firstLine}.
	 * @param ctx the parse tree
	 */
	void enterFirstLine(GrammarParser.FirstLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#firstLine}.
	 * @param ctx the parse tree
	 */
	void exitFirstLine(GrammarParser.FirstLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#code}.
	 * @param ctx the parse tree
	 */
	void enterCode(GrammarParser.CodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#code}.
	 * @param ctx the parse tree
	 */
	void exitCode(GrammarParser.CodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#nonTerminal}.
	 * @param ctx the parse tree
	 */
	void enterNonTerminal(GrammarParser.NonTerminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#nonTerminal}.
	 * @param ctx the parse tree
	 */
	void exitNonTerminal(GrammarParser.NonTerminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#rules}.
	 * @param ctx the parse tree
	 */
	void enterRules(GrammarParser.RulesContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#rules}.
	 * @param ctx the parse tree
	 */
	void exitRules(GrammarParser.RulesContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#ruleOnly}.
	 * @param ctx the parse tree
	 */
	void enterRuleOnly(GrammarParser.RuleOnlyContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#ruleOnly}.
	 * @param ctx the parse tree
	 */
	void exitRuleOnly(GrammarParser.RuleOnlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#oneRule}.
	 * @param ctx the parse tree
	 */
	void enterOneRule(GrammarParser.OneRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#oneRule}.
	 * @param ctx the parse tree
	 */
	void exitOneRule(GrammarParser.OneRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#receivedArguments}.
	 * @param ctx the parse tree
	 */
	void enterReceivedArguments(GrammarParser.ReceivedArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#receivedArguments}.
	 * @param ctx the parse tree
	 */
	void exitReceivedArguments(GrammarParser.ReceivedArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(GrammarParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(GrammarParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(GrammarParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(GrammarParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#returnType}.
	 * @param ctx the parse tree
	 */
	void enterReturnType(GrammarParser.ReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#returnType}.
	 * @param ctx the parse tree
	 */
	void exitReturnType(GrammarParser.ReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#returnn}.
	 * @param ctx the parse tree
	 */
	void enterReturnn(GrammarParser.ReturnnContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#returnn}.
	 * @param ctx the parse tree
	 */
	void exitReturnn(GrammarParser.ReturnnContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#terminal}.
	 * @param ctx the parse tree
	 */
	void enterTerminal(GrammarParser.TerminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#terminal}.
	 * @param ctx the parse tree
	 */
	void exitTerminal(GrammarParser.TerminalContext ctx);
}