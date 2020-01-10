package eatyourbeets.resources.animator.metrics;

import eatyourbeets.utilities.JavaUtilities;

public class AnimatorTrophies
{
    public int id;
    public int trophy1 = -1;
    public int trophy2 = -1;
    public int trophy3 = -1;

    public AnimatorTrophies()
    {
        this(-1);
    }

    public AnimatorTrophies(int id)
    {
        this.id = id;
    }

    public void Deserialize(String data)
    {
        String[] values = data.split(",");

        int id = JavaUtilities.ParseInt(values[0], -1);
        if (id >= 0)
        {
            this.id = id;
            this.trophy1 = JavaUtilities.ParseInt(values[1], -1);
            this.trophy2 = JavaUtilities.ParseInt(values[2], -1);
            this.trophy3 = JavaUtilities.ParseInt(values[3], -1);
        }
    }

    public String Serialize()
    {
        return id + "," + trophy1 + "," + trophy2 + "," + trophy3;
    }
}