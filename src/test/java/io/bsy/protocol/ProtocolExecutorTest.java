package io.bsy.protocol;

import static io.bsy.TestUtils.buildAssessCommand;
import static io.bsy.TestUtils.buildListCommand;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProtocolExecutorTest {

    @Test
    public void testExecuteAssessCommandForActualAnagrams() {
        var executor = new ProtocolExecutor();

        var given = buildAssessCommand("Hello", "Olleh");
        var actual = executor.execute(given);
        var expected = "true";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExecuteAssessCommandForActualAnagramsIgnoresSpaces() {
        var executor = new ProtocolExecutor();

        var given = buildAssessCommand("Hello", "Ol l eh");
        var actual = executor.execute(given);
        var expected = "true";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExecuteAssessCommandForActualAnagramsExampleFromWiki() {
        var executor = new ProtocolExecutor();

        var given = buildAssessCommand("New York Times", "monkeys write");
        var actual = executor.execute(given);
        var expected = "true";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExecuteAssessCommandForEdgeCaseWithEmptyStrings() {
        var executor = new ProtocolExecutor();

        var given = buildAssessCommand("   ", "  ");
        var actual = executor.execute(given);
        var expected = "true";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExecuteListCommandForSimplePositiveCase() {
        var executor = new ProtocolExecutor();

        var assessCmd = buildAssessCommand("AAAss", "AsAsA");
        Assertions.assertEquals("true", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsss", "AsAss");
        Assertions.assertEquals("true", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsAs", "sssAA");
        Assertions.assertEquals("false", executor.execute(assessCmd));

        var given = buildListCommand("AAsAs");
        var expected = Set.of("AAAss", "AsAsA");
        var actual = executor.execute(given)
                .replace("[", "")
                .replace("]", "")
                .replaceAll(",", "\n")
                .lines()
                .collect(Collectors.toSet());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExecuteListCommandForSimplePositiveCase01() {
        var executor = new ProtocolExecutor();

        var assessCmd = buildAssessCommand("AAAss", "AsAsA");
        Assertions.assertEquals("true", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsss", "AsAss");
        Assertions.assertEquals("true", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsAs", "sssAA");
        Assertions.assertEquals("false", executor.execute(assessCmd));

        var given = buildListCommand("ssAAA");
        var expected = Set.of("AAAss", "AsAsA", "AAsAs");
        var actual = executor.execute(given)
                .replace("[", "")
                .replace("]", "")
                .replaceAll(",", "\n")
                .lines()
                .collect(Collectors.toSet());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExecuteListCommandForSimpleNegativeCase() {
        var executor = new ProtocolExecutor();

        var assessCmd = buildAssessCommand("AAAss", "AsAsA");
        Assertions.assertEquals("true", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsss", "AsAss");
        Assertions.assertEquals("true", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsAs", "sssAA");
        Assertions.assertEquals("false", executor.execute(assessCmd));

        var given = buildListCommand("T");
        var expected = Set.of();
        var actual = executor.execute(given)
                .replace("[", "")
                .replace("]", "")
                .replaceAll(",", "\n")
                .lines()
                .collect(Collectors.toSet());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExecuteListCommandForEdgeCase() {
        var executor = new ProtocolExecutor();

        var assessCmd = buildAssessCommand("AAAss", "AsAsA");
        Assertions.assertEquals("true", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsss", "AsAss");
        Assertions.assertEquals("true", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsAs", "sssAA");
        Assertions.assertEquals("false", executor.execute(assessCmd));

        var given = buildListCommand("   ");
        var expected = Set.of();
        var actual = executor.execute(given)
                .replace("[", "")
                .replace("]", "")
                .replaceAll(",", "\n")
                .lines()
                .collect(Collectors.toSet());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExecuteListCommandForEmptyStringsEdgeCase() {
        var executor = new ProtocolExecutor();

        var assessCmd = buildAssessCommand("  ", "AsAsA");
        Assertions.assertEquals("false", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsss", "AsAss");
        Assertions.assertEquals("true", executor.execute(assessCmd));

        assessCmd = buildAssessCommand("AAsAs", "    ");
        Assertions.assertEquals("false", executor.execute(assessCmd));

        var given = buildListCommand("   ");
        var expected = Set.of("  ", "    ");
        var actual = executor.execute(given)
                .replace("[", "")
                .replace("]", "")
                .replaceAll(",", "\n")
                .lines()
                .collect(Collectors.toSet());
        Assertions.assertEquals(expected, actual);
    }
}
