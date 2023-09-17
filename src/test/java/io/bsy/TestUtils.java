package io.bsy;

import io.bsy.protocol.AssessCommand;
import io.bsy.protocol.ListCommand;

public class TestUtils {

    public static ListCommand buildListCommand(String query) {
        var command = new ListCommand();

        for (char c : query.toCharArray()) {
            command.add(c);
        }
        return command;
    }

    public static AssessCommand buildAssessCommand(String left, String right) {
        var command = new AssessCommand();

        for (char c : left.toCharArray()) {
            command.add(c);
        }
        command.setLeftIstDone();
        for (char c : right.toCharArray()) {
            command.add(c);
        }
        return command;
    }
}
