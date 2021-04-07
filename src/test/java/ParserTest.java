import com.jetbrains.task.util.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void examplesParseTest() {
        Parser parser1 = new Parser("filter{(element>10)}%>%filter{(element<20)}");
        Assertions.assertTrue(parser1.parse());
        Assertions.assertEquals("filter{((element>10)&(element<20))}%>%map{element}",
                parser1.getSimplifiedLine());

        Parser parser2 = new Parser("map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}");
        Assertions.assertTrue(parser2.parse());
        Assertions.assertEquals("filter{((element+10)>10)}%>%map{((element+10)*(element+10))}",
                parser2.getSimplifiedLine());

        Parser parser3 = new Parser("filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)}");
        Assertions.assertTrue(parser3.parse());
        Assertions.assertEquals("filter{((element>0)&(element<0))}%>%map{(element*element)}",
                parser3.getSimplifiedLine());
    }

    @Test
    public void parseSimpleTest() {
        Parser parser = new Parser("map{element}");
        Assertions.assertTrue(parser.parse());
        Assertions.assertEquals("filter{(1=1)}%>%map{element}", parser.getSimplifiedLine());

        parser = new Parser("map{element}%>%filter{(element=3)}");
        Assertions.assertTrue(parser.parse());
        Assertions.assertEquals("filter{(element=3)}%>%map{element}", parser.getSimplifiedLine());

        parser = new Parser("filter{(element>10)}%>%map{(element*2)}");
        Assertions.assertTrue(parser.parse());
        Assertions.assertEquals("filter{(element>10)}%>%map{(element*2)}", parser.getSimplifiedLine());
    }

    @Test
    public void parseNegativeNumbersTest() {
        Parser parser = new Parser("map{(element*-3)}%>%filter{((element>-6)|(element<-12))}");
        Assertions.assertTrue(parser.parse());
        Assertions.assertEquals("filter{(((element*-3)>-6)|((element*-3)<-12))}%>%map{(element*-3)}",
                parser.getSimplifiedLine());
    }

    @Test
    public void parseWithWhiteSpacesTest() {
        Parser parser = new Parser("map{  (element * 2)} %>%        \t  filter{( element < 10) }");
        Assertions.assertTrue(parser.parse());
        Assertions.assertEquals("filter{((element*2)<10)}%>%map{(element*2)}", parser.getSimplifiedLine());

        parser = new Parser(
                "  filter{  (element > 5)  }  %>% map{ (element * -3) } %>% filter{(element < -5)} ");
        Assertions.assertTrue(parser.parse());
        Assertions.assertEquals("filter{((element>5)&((element*-3)<-5))}%>%map{(element*-3)}",
                parser.getSimplifiedLine());
    }

    @Test
    public void parseSyntaxErrorTest() {
        Parser parser = new Parser("map{(element > 10))}");
        Assertions.assertFalse(parser.parse());

        parser = new Parser("filter{}%>%map{element}");
        Assertions.assertFalse(parser.parse());

        parser = new Parser("filter{((element < 2))}");
        Assertions.assertFalse(parser.parse());

        parser = new Parser("filter{(element > 5)|(element<3)}%>%map{((element*3) + 10)}%<%filter{(element<4)}");
        Assertions.assertFalse(parser.parse());
    }

    @Test
    public void parseTypeErrorTest() {
        Parser parser = new Parser("filter{(element<4)+(element=3)}");
        Assertions.assertFalse(parser.parse());

        parser = new Parser("map{(((element>3)|(element<2))+7)}");
        Assertions.assertFalse(parser.parse());

        parser = new Parser("filter{(element > 10)}%>%map{(element < 3)}");
        Assertions.assertFalse(parser.parse());

        parser = new Parser("filter{(element * 2)}");
        Assertions.assertFalse(parser.parse());
    }


}
