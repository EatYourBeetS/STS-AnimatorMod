package eatyourbeets.resources.animator.misc;

import eatyourbeets.utilities.Mathf;

public class AnimatorLoadoutStats
{
    protected final static int MAX = 10;

    public int id;
    public int total_runs = 0;
    public int total_wins = 0;
    public Integer[] recent_runs = new Integer[MAX];

    public AnimatorLoadoutStats()
    {
        this(-1);
    }

    public AnimatorLoadoutStats(int id)
    {
        this.id = id;
    }

    public float GetRecentWinRate()
    {
        int t = 0;
        int w = 0;
        for (Integer run : recent_runs)
        {
            if (run != null)
            {
                t += 1;

                if (run == 3)
                {
                    w += 1;
                }
            }
        }

        return w / (float) t;
    }

    public int[] GetRecentPerformance()
    {
        final int[] result = new int[4];
        for (Integer run : recent_runs)
        {
            if (run != null)
            {
                result[run] += 1;
            }
        }

        return result;
    }

    public void AddRunData(int act, boolean victory)
    {
        System.arraycopy(recent_runs, 0, recent_runs, 1, recent_runs.length - 1);
        total_runs += 1;

        if (victory)
        {
            recent_runs[0] = 3;
            total_wins += 1;
        }
        else
        {
            recent_runs[0] = Mathf.Clamp(act - 1, 0, 2);
        }
    }
}
