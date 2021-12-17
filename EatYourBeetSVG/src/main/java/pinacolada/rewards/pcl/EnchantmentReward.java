package pinacolada.rewards.pcl;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.pcl.enchantments.Enchantment;
import pinacolada.relics.PCLEnchantableRelic;
import pinacolada.resources.GR;
import pinacolada.rewards.PCLReward;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class EnchantmentReward extends PCLReward
{
    public static final String ID = CreateFullID(EnchantmentReward.class);

    private final PCLEnchantableRelic relic;

    public EnchantmentReward(PCLEnchantableRelic relic)
    {
        super(ID,GR.PCL.Strings.Rewards.Enchantment, GR.Enums.Rewards.ENCHANTMENT);

        this.relic = relic;
    }

    public static void TryAddReward(PCLEnchantableRelic relic, ArrayList<RewardItem> rewards)
    {
        PCLEnchantableRelic er = relic;
        if (er == null) {
            er = GetEnchantableRelic();
        }
        if (er != null && PCLGameUtilities.InBossRoom())
        {
            EnchantmentReward reward = new EnchantmentReward(er);
            if (reward.cards.size() > 0)
            {
                rewards.add(reward);
            }
        }
    }

    public static PCLEnchantableRelic GetEnchantableRelic() {
        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            PCLEnchantableRelic er = PCLJUtils.SafeCast(r, PCLEnchantableRelic.class);
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
        PCLGameEffects.Queue.Callback(new SelectFromPile(relic.name, 1, relic.CreateUpgradeGroup())
                .HideTopPanel(true)
                .CancellableFromPlayer(true)
                .AddCallback(selection -> {
                    if (selection.size() > 0) {
                        Enchantment e = (Enchantment) selection.get(0);
                        relic.ApplyEnchantment(e);
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
            return new EnchantmentReward(PCLGameUtilities.GetRelic(rewardSave.id));
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            EnchantmentReward reward = PCLJUtils.SafeCast(customReward, EnchantmentReward.class);
            if (reward != null)
            {
                return new RewardSave(customReward.type.toString(), reward.relic.relicId, 0, 0);
            }

            return null;
        }
    }
}