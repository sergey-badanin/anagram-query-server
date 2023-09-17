package io.bsy.protocol;

public class AssessCommand implements ProtocolCommand {

    private final StringBuilder leftArgument = new StringBuilder();
    private final StringBuilder rightArgument = new StringBuilder();
    private boolean leftIsDone;

    @Override
    public Action getAction() {
        return Action.ASSESS;
    }

    public void setLeftIstDone() {
        leftIsDone = true;
    }

    public boolean leftIsDone() {
        return leftIsDone;
    }

    public void add(char c) {
        if (leftIsDone) {
            rightArgument.append(c);
        } else {
            leftArgument.append(c);
        }
    }

    public String getLeftArgument() {
        return leftArgument.toString();
    }

    public String getRightArgument() {
        return rightArgument.toString();
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
        var that = (AssessCommand) obj;

        return this.getAction() == that.getAction()
                && this.leftIsDone == that.leftIsDone
                && this.leftArgument.toString().equals(that.leftArgument.toString())
                && this.rightArgument.toString().equals(that.rightArgument.toString());
    }

    @Override
    public String toString() {
        return "ASSESS: [" + leftArgument.toString() + "," + rightArgument.toString() + "]";
    }
}
