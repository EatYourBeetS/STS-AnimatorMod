package eatyourbeets.ui.common;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class EnchantCampfireOption extends AbstractCampfireOption
{
    private static final String LABEL_GOLD_F1 = GR.Animator.Strings.CardMods.RespecLivingPicture;
    private static final String LABEL_MAX = GR.Animator.Strings.CardMods.RespecLivingPictureLocked;

    private EnchantableRelic relic;
    private RestRoom room;
    private int goldCost;

    public static boolean CanAddOption()
    {
        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            if (r instanceof EnchantableRelic && ((EnchantableRelic) r).GetEnchantmentLevel() > 0)
            {
                return true;
            }
        }

        return false;
    }

    public EnchantCampfireOption()
    {
        this.description = GR.Animator.Strings.CardMods.RespecLivingPictureDescription;
        this.img = GR.Common.Images.CampfireOption_Enchant.Texture();
        Refresh();
    }

    @Override
    public void update()
    {
        super.update();

        if (GR.UI.Elapsed100())
        {
            Refresh();
        }
    }

    public void Refresh()
    {
        this.usable = false;
        this.goldCost = 100;

        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            EnchantableRelic er = JUtils.SafeCast(r, EnchantableRelic.class);
            if (er != null && er.GetEnchantmentLevel() > 0)
            {
                relic = er;
                goldCost += (er.GetEnchantmentLevel() * 50);
                break;
            }
        }

        if (relic.GetEnchantmentLevel() < 1)
        {
            this.label = LABEL_MAX;
            this.usable = false;
        }
        else
        {
            this.label = JUtils.Format(LABEL_GOLD_F1, goldCost);
            this.usable = AbstractDungeon.player.gold >= goldCost;
        }
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

        relic.SetCounter(relic.GetEnchantmentLevel());
        relic.enchantment = null;
        relic.flash();
        Refresh();
    }
}