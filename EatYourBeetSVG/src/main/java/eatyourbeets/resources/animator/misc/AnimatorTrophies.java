package eatyourbeets.resources.animator.misc;

import eatyourbeets.utilities.JavaUtilities;

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
        String[] values = data.split(",");

        int id = JavaUtilities.ParseInt(values[0], -1);
        if (id >= 0)
        {
            this.ID = id;
            this.Trophy1 = JavaUtilities.ParseInt(values[1], -1);
            this.Trophy2 = JavaUtilities.ParseInt(values[2], -1);
            this.Trophy3 = JavaUtilities.ParseInt(values[3], -1);
        }
    }

    public String Serialize()
    {
        return ID + "," + Trophy1 + "," + Trophy2 + "," + Trophy3;
    }
}