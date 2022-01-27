// Generated from /home/anton/_University/5sem/MT/Lab4/src/main/java/antlr/Grammar.g4 by ANTLR 4.9.2
package antlr.grammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GrammarParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(GrammarParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#firstLine}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFirstLine(GrammarParser.FirstLineContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode(GrammarParser.CodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#nonTerminal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonTerminal(GrammarParser.NonTerminalContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#rules}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRules(GrammarParser.RulesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#ruleOnly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleOnly(GrammarParser.RuleOnlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#oneRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOneRule(GrammarParser.OneRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#receivedArguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReceivedArguments(GrammarParser.ReceivedArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(GrammarParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(GrammarParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#returnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnType(GrammarParser.ReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#returnn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnn(GrammarParser.ReturnnContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#terminal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerminal(GrammarParser.TerminalContext ctx);
}