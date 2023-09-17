package io.bsy.protocol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProtocolExecutor {

    private final Map<Map<Character, Integer>, Set<String>> signatureToWords = new HashMap<>();

    public String execute(ProtocolCommand command) {
        if (ProtocolCommand.Action.ASSESS.equals(command.getAction())) {
            return executeAssess((AssessCommand) command);
        }
        if (ProtocolCommand.Action.LIST.equals(command.getAction())) {
            return executeList((ListCommand) command);
        }
        throw new IllegalStateException("Could not define command");
    }

    public String executeAssess(AssessCommand cmd) {
        var leftWord = cmd.getLeftArgument();
        var rightWord = cmd.getRightArgument();

        var leftSignature = buildSignature(homogenize(leftWord));
        var rightSignature = buildSignature(homogenize(rightWord));

        signatureToWords.computeIfAbsent(leftSignature, k -> new HashSet<>()).add(leftWord);
        signatureToWords.computeIfAbsent(rightSignature, k -> new HashSet<>()).add(rightWord);

        return Boolean.toString(leftSignature.equals(rightSignature));
    }

    public String executeList(ListCommand cmd) {
        var query = cmd.getQuery();
        var signature = buildSignature(homogenize(query));

        return signatureToWords
                .getOrDefault(signature, Set.of())
                .stream()
                .filter(s -> !s.equals(query))
                .collect(Collectors.joining(",", "[", "]"));
    }

    private Map<Character, Integer> buildSignature(String word) {
        var signature = new HashMap<Character, Integer>();
        for (char c : word.toCharArray()) {
            signature.merge(c, 1, (v1, v2) -> v1 + v2);
        }
        return signature;
    }

    private String homogenize(String word) {
        return word.replaceAll("\\s", "").toLowerCase();
    }
}
