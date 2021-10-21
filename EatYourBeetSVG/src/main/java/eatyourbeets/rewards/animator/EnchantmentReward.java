package eatyourbeets.rewards.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class EnchantmentReward extends AnimatorReward
{
    public static final String ID = CreateFullID(EnchantmentReward.class);

    private final EnchantableRelic relic;

    public EnchantmentReward(EnchantableRelic relic)
    {
        super(ImageMaster.UI_GOLD,GR.Animator.Strings.Rewards.Enchantment, GR.Enums.Rewards.ENCHANTMENT);

        this.relic = relic;
    }

    public static void TryAddReward(EnchantableRelic relic, ArrayList<RewardItem> rewards)
    {
        EnchantableRelic er = relic;
        if (er == null) {
            er = GetEnchantableRelic();
        }
        if (er != null && GameUtilities.InBossRoom())
        {
            EnchantmentReward reward = new EnchantmentReward(er);
            if (reward.cards.size() > 0)
            {
                rewards.add(reward);
            }
        }
    }

    public static EnchantableRelic GetEnchantableRelic() {
        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            EnchantableRelic er = JUtils.SafeCast(r, EnchantableRelic.class);
            if (er != null && er.GetEnchantmentLevel() < 2)
            {
                return er;
            }
        }
        return null;
    }

    @Override
    public boolean claimReward()
    {
        GameEffects.Queue.Callback(new SelectFromPile(relic.name, 1, relic.CreateUpgradeGroup())
                .HideTopPanel(true)
                .CancellableFromPlayer(true)
                .AddCallback(selection -> {
                    if (selection.size() > 0) {
                        Enchantment e = (Enchantment) selection.get(0);
                        relic.ApplyEnchantment(e);
                        e.OnObtain();
                        relic.flash();
                    }
                }));

        this.isDone = true;

        return true;
    }

    public static class Serializer implements BaseMod.LoadCustomReward, BaseMod.SaveCustomReward
    {
        @Override
        public CustomReward onLoad(RewardSave rewardSave)
        {
            return new EnchantmentReward(GameUtilities.GetRelic(rewardSave.id));
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            EnchantmentReward reward = JUtils.SafeCast(customReward, EnchantmentReward.class);
            if (reward != null)
            {
                return new RewardSave(customReward.type.toString(), reward.relic.relicId, 0, 0);
            }

            return null;
        }
    }
}