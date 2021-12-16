package eatyourbeets.resources.animator.misc;

import eatyourbeets.utilities.JUtils;

public class AnimatorTrophies
{
    public static final int MAXIMUM_TROPHY = 20;
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
        String[] values = data.split(",");

        int id = JUtils.ParseInt(values[0], -1);
        if (id >= 0)
        {
            this.ID = id;
            this.Trophy1 = JUtils.ParseInt(values[1], -1);
            this.Trophy2 = JUtils.ParseInt(values[2], -1);
            this.Trophy3 = JUtils.ParseInt(values[3], -1);
        }
    }

    public String Serialize()
    {
        return ID + "," + Trophy1 + "," + Trophy2 + "," + Trophy3;
    }
}