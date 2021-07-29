package eatyourbeets.ui.common;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class EnchantCampfireOption extends AbstractCampfireOption
{
    private static final String LABEL_GOLD_F1 = "Enchant [{0} Gold]";
    private static final String LABEL_MAX = "Enchant [Max Level]";

    private RestRoom room;
    private EnchantableRelic relic;
    private int goldCost;

    public static boolean CanAddOption()
    {
        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            if (r instanceof EnchantableRelic && ((EnchantableRelic) r).GetEnchantmentLevel() < 2)
            {
                return true;
            }
        }

        return false;
    }

    public EnchantCampfireOption()
    {
        Refresh();
    }

    public void Refresh()
    {
        this.usable = false;
        this.goldCost = 100;

        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            EnchantableRelic er = JUtils.SafeCast(r, EnchantableRelic.class);
            if (er != null && er.GetEnchantmentLevel() < 2)
            {
                relic = er;
                goldCost += (er.GetEnchantmentLevel() * 50);
                break;
            }
        }

        if (relic.GetEnchantmentLevel() > 1)
        {
            this.label = LABEL_MAX;
            this.usable = false;
        }
        else
        {
            this.label = JUtils.Format(LABEL_GOLD_F1, goldCost);
            this.usable = AbstractDungeon.player.gold >= goldCost;
        }

        this.description = "Improve the effects of Living Picture.";
        this.img = GR.Common.Images.CampfireOption_Enchant.Texture();
    }

    @Override
    public void useOption()
    {
        Refresh();

        if (!usable)
        {
            GameEffects.Queue.Callback(new WaitAction(0.1f), ((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI::reopen);
            return;
        }

        room = (RestRoom) AbstractDungeon.getCurrRoom();
        room.campfireUI.somethingSelected = true;

        GameEffects.Queue.Callback(new SelectFromPile(relic.name, 1, relic.CreateUpgradeGroup())
        .CancellableFromPlayer(true)
        .AddCallback(c -> OnEffectSelected((Enchantment)c.get(0))), () -> room.campfireUI.reopen())
        .ShowBlackScreen(0.95f);
    }

    public void OnEffectSelected(Enchantment enchantment)
    {
        AbstractDungeon.player.loseGold(goldCost);
        relic.ApplyEnchantment(enchantment);
        relic.flash();
        Refresh();
    }
}