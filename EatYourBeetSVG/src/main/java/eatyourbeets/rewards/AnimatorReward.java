package eatyourbeets.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.GR;

public abstract class AnimatorReward extends CustomReward
{
    public static String CreateFullID(String id)
    {
        return GR.Animator.CreateID(id);
    }

    public AnimatorReward(String id, String text, RewardType type)
    {
        super(new Texture(GR.GetRewardImage(id)), text, type);
    }

    public AnimatorReward(Texture rewardImage, String text, RewardType type)
    {
        super(rewardImage, text, type);
    }
}