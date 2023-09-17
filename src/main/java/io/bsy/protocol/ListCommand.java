package io.bsy.protocol;

public class ListCommand implements ProtocolCommand {

    private final StringBuilder queryBuilder = new StringBuilder();

    @Override
    public Action getAction() {
        return Action.LIST;
    }

    public void add(char c) {
        queryBuilder.append(c);
    }

    public String getQuery() {
        return queryBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (ListCommand) obj;

        return this.getAction() == that.getAction()
                && this.queryBuilder.toString().equals(that.queryBuilder.toString());
    }

    @Override
    public String toString() {
        return "LIST: <" + queryBuilder.toString() + ">";
    }
}
