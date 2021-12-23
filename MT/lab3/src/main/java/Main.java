import antlr.GrammarLexer;
import antlr.GrammarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

// TODO: строчки a = 'abc'
// TODO: elif
public class Main {
    public static void main(String[] args) {
//        GrammarLexer lexer = new GrammarLexer(CharStreams.fromString("""
//                a = int(input())
//                b = int(input())
//                print(a + b)
//                """));
        GrammarLexer lexer = new GrammarLexer(CharStreams.fromString("""
                a = input()
                print(a)
                b = 'abc'
                d = b
                print(b)
                c = int(input())
                if c > 111:
                    c = 1
                elif c < 5:
                    c = 2
                elif (c > 5) and (c < 7):
                    c = 11
                else:
                    c = 0
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