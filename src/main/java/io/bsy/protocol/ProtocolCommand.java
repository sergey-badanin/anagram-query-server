package io.bsy.protocol;

public interface ProtocolCommand {

    Action getAction();

    public static enum Action {
        ASSESS,
        LIST
    }
}
