package eatyourbeets.resources.animator.misc;

import eatyourbeets.utilities.JUtils;

public class AnimatorTrophies
{
    public int ID;
    public int Trophy1 = -1;
    public int Trophy2 = -1;
    public int Trophy3 = -1;

    public AnimatorTrophies()
    {
        this(-1);
    }

    public AnimatorTrophies(int id)
    {
        this.ID = id;
    }

    public void Deserialize(String data)
    {
        final String[] values = data.split(",");
        final int id = JUtils.ParseInt(values[0], -1);
        if (id >= 0)
        {
            this.ID = id;
            this.Trophy1 = TryParse(values, 1, -1);
            this.Trophy2 = TryParse(values, 2, -1);
            this.Trophy3 = TryParse(values, 3, -1);
        }
    }

    public String Serialize()
    {
        return ID + "," + Trophy1 + "," + Trophy2 + "," + Trophy3;
    }

    protected int TryParse(String[] values, int index, int defaultValue)
    {
        return index >= values.length ? defaultValue : JUtils.ParseInt(values[index], defaultValue);
    }
}