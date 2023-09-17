package io.bsy.protocol;

import static io.bsy.TestUtils.buildAssessCommand;
import static io.bsy.TestUtils.buildListCommand;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProtocolParserTest {

    @Test
    public void testParsingCorrectlyBuiltListCommandContainedInSingleInput() {
        var parser = new ProtocolParser();

        var given = "<Hello>";
        var expected = buildListCommand("Hello");

        var actual = parser.parse(given);

        Assertions.assertEquals(expected, actual.get(0));
    }

    @Test
    public void testParsingCorrectlyBuiltListCommandContainedInMultipleInput() {
        var parser = new ProtocolParser();

        var given = "<Hello";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        given = " world";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        given = "!>";
        var actual = parser.parse(given);

        var expected = buildListCommand("Hello world!");

        Assertions.assertEquals(expected, actual.get(0));
    }

    @Test
    public void testParsingCorrectlyBuiltListCommandContainedInMultipleInputWithSomeExcessiveSymbols() {
        var parser = new ProtocolParser();

        var given = "Before<Hello";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        given = " world";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        given = "!>After";
        var actual = parser.parse(given);

        var expected = buildListCommand("Hello world!");

        Assertions.assertEquals(expected, actual.get(0));
    }

    @Test
    public void testParsingMultipleCorrectlyBuiltListCommandsContainedInMultipleInput() {
        var parser = new ProtocolParser();

        var given = "<Hello world!>, <Goodbye";
        var actual = parser.parse(given);
        var expected = List.of(buildListCommand("Hello world!"));
        Assertions.assertEquals(expected, actual);

        given = " ";
        actual = parser.parse(given);
        System.out.println(actual);
        Assertions.assertTrue(actual.isEmpty());

        given = "void!>";
        actual = parser.parse(given);
        expected = List.of(buildListCommand("Goodbye void!"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testParsingMultipleCorrectlyBuiltListCommandsContainedInSingleInput() {
        var parser = new ProtocolParser();

        var given = "<Hello world!>, <Goodbye void!>";
        var actual = parser.parse(given);
        var expected = List.of(buildListCommand("Hello world!"), buildListCommand("Goodbye void!"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testParsingIncorrectlyBuiltListCommandContainedInMultipleInput() {
        var parser = new ProtocolParser();

        var given = "Before<Hello";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        var given1 = " worl]d";
        Assertions.assertThrows(UnparsableCommandException.class,
                () -> parser.parse(given1),
                "Could not parse command. Erronuous char: ']' at 4");
    }

    @Test
    public void testParsingCorrectlyBuiltAssessCommandConainedInSingleInput() {
        var parser = new ProtocolParser();

        var given = "[Hello,olleH]";
        var expected = List.of(buildAssessCommand("Hello", "olleH"));

        var actual = parser.parse(given);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testParsingCorrectlyBuiltAssessCommandContainedInMultipleInput() {
        var parser = new ProtocolParser();

        var given = "[Hello";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        given = ",world";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        given = "!]";
        var actual = parser.parse(given);

        var expected = List.of(buildAssessCommand("Hello", "world!"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testParsingCorrectlyBuiltAssessCommandContainedInMultipleInputWithSomeExcessiveSymbols() {
        var parser = new ProtocolParser();

        var given = "Before[Hello,";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        given = "world";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        given = "!]After";
        var actual = parser.parse(given);

        var expected = List.of(buildAssessCommand("Hello", "world!"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testParsingMultipleCorrectlyBuiltAssessCommandsContainedInMultipleInput() {
        var parser = new ProtocolParser();

        var given = "[Hello,world!], [Goodbye";
        var actual = parser.parse(given);
        var expected = List.of(buildAssessCommand("Hello", "world!"));
        Assertions.assertEquals(expected, actual);

        given = ",";
        actual = parser.parse(given);
        System.out.println(actual);
        Assertions.assertTrue(actual.isEmpty());

        given = "void!]";
        actual = parser.parse(given);
        expected = List.of(buildAssessCommand("Goodbye", "void!"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testParsingMultipleCorrectlyBuiltAssessCommandsContainedInSingleInput() {
        var parser = new ProtocolParser();

        var given = "[Hello,world!], [Goodbye,void!]";
        var actual = parser.parse(given);
        var expected = List.of(buildAssessCommand("Hello", "world!"), buildAssessCommand("Goodbye", "void!"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testParsingIncorrectlyBuiltAssessCommandContainedInMultipleInput() {
        var parser = new ProtocolParser();

        var given = "Before[Hello,";
        Assertions.assertTrue(parser.parse(given).isEmpty());

        var given1 = "world,]";
        Assertions.assertThrows(UnparsableCommandException.class,
                () -> parser.parse(given1),
                "Could not parse command. Erronuous char: ',' at 12");
    }

}
