import com.jetbrains.task.util.Lexer;
import com.jetbrains.task.util.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LexerTest {

    @Test
    public void getNextTokenOneFilterTest() {
        Lexer lexer = new Lexer("filter{(element>1)|(element<3)}");
        for (int i = 0; i < 6; i++) {
            lexer.getNextToken();
        }
        System.out.println(lexer.getNextToken());
    }

    @Test
    public void getNextTokenTest() {
        Lexer lexer = new Lexer("filter{   element  }   %>%  map{element+10}");
        Assertions.assertEquals(Token.TK_KEY_FILTER, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_KEY_ELEMENT, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_RBRACKET, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_CALL, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_KEY_MAP, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_KEY_ELEMENT, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_PLUS, lexer.getNextToken());
        Assertions.assertEquals(Token.INTEGER, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_RBRACKET, lexer.getNextToken());

        Assertions.assertTrue(lexer.isExhausted());
    }

    @Test
    void getNextTokenIncorrectTest() {
        Lexer lexer = new Lexer("map{element} hello from hse spb!");
        Assertions.assertEquals(Token.TK_KEY_MAP, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_KEY_ELEMENT, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_RBRACKET, lexer.getNextToken());

        Assertions.assertNull(lexer.getNextToken());
        Assertions.assertTrue(lexer.isExhausted());

    }

    @Test
    void getInputSubstrTest() {
        Lexer lexer = new Lexer("map{element *   -5} %>% filter{element > 10}");

        Assertions.assertEquals(Token.TK_KEY_MAP, lexer.getNextToken());
        int lastPos = lexer.getCurPos();
        Assertions.assertEquals(Token.TK_KEY_ELEMENT, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_MUL, lexer.getNextToken());

        Assertions.assertEquals("element", lexer.getInputSubstr(lastPos));

        Assertions.assertEquals(Token.TK_MINUS, lexer.getNextToken());
        Assertions.assertEquals(Token.INTEGER, lexer.getNextToken());
        Assertions.assertEquals(Token.TK_RBRACKET, lexer.getNextToken());

        Assertions.assertEquals("map{element*-5", lexer.getInputSubstr(0));
    }
}
