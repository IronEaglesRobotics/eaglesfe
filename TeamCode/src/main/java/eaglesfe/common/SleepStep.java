package eaglesfe.common;

import eaglesfe.common.Step;

public class SleepStep extends Step {
    public SleepStep(String description, int timeout) {
        super(description, timeout);
    }

    public void enter() { }

    public boolean isFinished() { return false; }

    public void leave() { }
}