package io.bsy.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProtocolParser {

    private final static char START_OF_ASSESS = '[';
    private final static char END_OF_ASSESS = ']';
    private final static char DELIMITER_IN_ASSESS = ',';
    private final static char START_OF_LIST = '<';
    private final static char END_OF_LIST = '>';

    private final static Set<Character> STARTERS = Set.of(START_OF_ASSESS, START_OF_LIST);
    private final static Set<Character> UNEXPECTED_IN_LIST = Set.of(START_OF_ASSESS, END_OF_ASSESS, DELIMITER_IN_ASSESS,
            START_OF_LIST);
    private final static Set<Character> UNEXPECTED_IN_ASSESS = Set.of(START_OF_ASSESS, START_OF_LIST, END_OF_LIST,
            DELIMITER_IN_ASSESS);

    private ProtocolCommand ongoing;
    private List<ProtocolCommand> parsed = new ArrayList<>();

    public List<ProtocolCommand> parse(String input) {
        if (input == null || input.isEmpty()) {
            return List.of();
        }

        var chars = input.toCharArray();

        if (ongoing == null) {
            defineCommand(chars, 0);
        } else if (ProtocolCommand.Action.ASSESS.equals(ongoing.getAction())) {
            parseAssess(chars, 0);
        } else if (ProtocolCommand.Action.LIST.equals(ongoing.getAction())) {
            parseList(chars, 0);
        }

        var result = List.copyOf(parsed);
        parsed.clear();
        return result;
    }

    private void defineCommand(char[] chars, int startingIdx) {
        int i = startingIdx;
        if (i >= chars.length) {
            return;
        }
        while (i < chars.length - 1 && !STARTERS.contains(chars[i])) {
            i += 1;
        }

        if (chars[i] == START_OF_LIST) {
            ongoing = new ListCommand();
            parseList(chars, i + 1);
        } else if (chars[i] == START_OF_ASSESS) {
            ongoing = new AssessCommand();
            parseAssess(chars, i + 1);
        }
    }

    private void parseList(char[] chars, int startingIdx) {
        var actual = (ListCommand) ongoing;

        int i = startingIdx;
        if (i >= chars.length) {
            return;
        }
        do {
            if (chars[i] == END_OF_LIST) {
                parsed.add(actual);
                ongoing = null;
                break;
            }
            if (UNEXPECTED_IN_LIST.contains(chars[i])) {
                ongoing = null;
                parsed.clear();
                var msg = String.format("Could not parse command. Erronuous char: \'%s\' at %s", chars[i], i);
                throw new UnparsableCommandException(msg);
            }
            actual.add(chars[i]);
            i += 1;
        } while (i < chars.length);

        if (i < chars.length) {
            defineCommand(chars, i + 1);
        }
    }

    private void parseAssess(char[] chars, int startingIdx) {
        var actual = (AssessCommand) ongoing;

        int i = startingIdx;
        if (i >= chars.length) {
            return;
        }

        do {
            if (chars[i] == END_OF_ASSESS) {
                if (actual.leftIsDone()) {
                    parsed.add(actual);
                    ongoing = null;
                    break;
                }
                ongoing = null;
                parsed.clear();
                var msg = String.format("Could not parse command. Erronuous char: \'%s\' at %s", chars[i], i);
                throw new UnparsableCommandException(msg);
            }
            if (chars[i] == DELIMITER_IN_ASSESS && !actual.leftIsDone()) {
                actual.setLeftIstDone();
                i += 1;
                continue;
            }
            if (UNEXPECTED_IN_ASSESS.contains(chars[i])) {
                ongoing = null;
                parsed.clear();
                var msg = String.format("Could not parse command. Erronuous char: \'%s\' at %s", chars[i], i);
                throw new UnparsableCommandException(msg);
            }
            actual.add(chars[i]);
            i += 1;
        } while (i < chars.length);

        if (i < chars.length) {
            defineCommand(chars, i + 1);
        }
    }
}
