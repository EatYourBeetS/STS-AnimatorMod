package eatyourbeets.events.base;

import java.util.function.Consumer;

public class PhaseHandler
{
    public final int phase;
    public final Runnable create;
    public final Consumer<Integer> handle;

    public PhaseHandler(int phase, Runnable create, Consumer<Integer> handle)
    {
        this.phase = phase;
        this.create = create;
        this.handle = handle;
    }
}
