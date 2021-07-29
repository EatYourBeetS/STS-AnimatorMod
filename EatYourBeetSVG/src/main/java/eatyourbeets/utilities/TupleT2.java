package eatyourbeets.utilities;

import java.util.Objects;

public class TupleT2<V1, V2>
{
    public V1 V1;
    public V2 V2;

    public TupleT2()
    {

    }

    public TupleT2(V1 v1, V2 v2)
    {
        this.V1 = v1;
        this.V2 = v2;
    }

    public void Clear()
    {
        this.V1 = null;
        this.V2 = null;
    }

    @Override
    public String toString()
    {
        return this.V1 + ": " + this.V2;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof TupleT2)
        {
            TupleT2 b = (TupleT2) other;
            return V1 == b.V1 && V2 == b.V2;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(V1, V2);
    }
}
