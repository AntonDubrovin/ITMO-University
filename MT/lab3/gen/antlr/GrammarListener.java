// Generated from /home/anton/_University/5sem/MT/lab3/src/main/java/antlr/Grammar.g4 by ANTLR 4.9.2
package antlr;
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
	 * Enter a parse tree produced by {@link GrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(GrammarParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(GrammarParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#conditionBody}.
	 * @param ctx the parse tree
	 */
	void enterConditionBody(GrammarParser.ConditionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#conditionBody}.
	 * @param ctx the parse tree
	 */
	void exitConditionBody(GrammarParser.ConditionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#ifCond}.
	 * @param ctx the parse tree
	 */
	void enterIfCond(GrammarParser.IfCondContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#ifCond}.
	 * @param ctx the parse tree
	 */
	void exitIfCond(GrammarParser.IfCondContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#elseCond}.
	 * @param ctx the parse tree
	 */
	void enterElseCond(GrammarParser.ElseCondContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#elseCond}.
	 * @param ctx the parse tree
	 */
	void exitElseCond(GrammarParser.ElseCondContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(GrammarParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(GrammarParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(GrammarParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(GrammarParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#input}.
	 * @param ctx the parse tree
	 */
	void enterInput(GrammarParser.InputContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#input}.
	 * @param ctx the parse tree
	 */
	void exitInput(GrammarParser.InputContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#print}.
	 * @param ctx the parse tree
	 */
	void enterPrint(GrammarParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#print}.
	 * @param ctx the parse tree
	 */
	void exitPrint(GrammarParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(GrammarParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(GrammarParser.ExpressionContext ctx);
}