import antlr.GrammarLexer;
import antlr.GrammarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {
    public static void main(String[] args) {
        GrammarLexer lexer = new GrammarLexer(CharStreams.fromString("""
                a = int(input())
                b = int(input())
                print(a + b)
                """));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokens);
        ParseTree tree = parser.start();
        ParseTreeWalker walker = new ParseTreeWalker();
        GrammarWalker grammarWalker = new GrammarWalker();
        walker.walk(grammarWalker, tree);
        System.out.println(grammarWalker.getResult());
    }
}