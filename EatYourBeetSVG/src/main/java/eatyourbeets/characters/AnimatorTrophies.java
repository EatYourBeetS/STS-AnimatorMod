package eatyourbeets.characters;

public class AnimatorTrophies
{
    public int id;
    public int trophy1;
    public int trophy2;
    public int trophy3;

    private int ParseInt(String s)
    {
        int result;
        try
        {
            result = Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }

        return result;
    }

    public AnimatorTrophies(int id)
    {
        this.id = id;
        this.trophy1 = -1;
        this.trophy2 = -1;
        this.trophy3 = -1;
    }

    public AnimatorTrophies(String deserialize)
    {
        String[] values = deserialize.split(",");

        int id = ParseInt(values[0]);
        if (id > 0)
        {
            this.id = id;
            this.trophy1 = ParseInt(values[1]);
            this.trophy2 = ParseInt(values[2]);
            this.trophy3 = ParseInt(values[3]);
        }
    }

    public String Serialize()
    {
        return id + "," + trophy1 + "," + trophy2 + "," + trophy3;
    }
}