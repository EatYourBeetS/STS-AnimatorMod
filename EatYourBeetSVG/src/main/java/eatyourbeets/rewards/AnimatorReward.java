package eatyourbeets.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.Resources_Animator;

public abstract class AnimatorReward extends CustomReward
{
    public static String CreateFullID(String id)
    {
        return "Animator_" + id;
    }

    public AnimatorReward(String id, String text, RewardType type)
    {
        super(new Texture(Resources_Animator.GetRewardImage(id)), text, type);
    }

    public AnimatorReward(Texture rewardImage, String text, RewardType type)
    {
        super(rewardImage, text, type);
    }
}