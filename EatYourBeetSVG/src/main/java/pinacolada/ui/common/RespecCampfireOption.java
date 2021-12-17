package pinacolada.ui.common;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import pinacolada.relics.PCLEnchantableRelic;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

public class RespecCampfireOption extends AbstractCampfireOption
{
    private static final String LABEL_GOLD_F1 = GR.PCL.Strings.CardMods.RespecLivingPicture;
    private static final String LABEL_MAX = GR.PCL.Strings.CardMods.RespecLivingPictureLocked;

    private PCLEnchantableRelic relic;
    private RestRoom room;
    private int goldCost;

    public static boolean CanAddOption()
    {
        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            if (r instanceof PCLEnchantableRelic && ((PCLEnchantableRelic) r).GetEnchantmentLevel() > 0)
            {
                return true;
            }
        }

        return false;
    }

    public RespecCampfireOption()
    {
        this.description = GR.PCL.Strings.CardMods.RespecLivingPictureDescription;
        this.img = GR.PCL.Images.CampfireOption_Enchant.Texture();
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
            PCLEnchantableRelic er = PCLJUtils.SafeCast(r, PCLEnchantableRelic.class);
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
            this.label = PCLJUtils.Format(LABEL_GOLD_F1, goldCost);
            this.usable = AbstractDungeon.player.gold >= goldCost;
        }
    }

    @Override
    public void useOption()
    {
        Refresh();

        if (!usable)
        {
            PCLGameEffects.Queue.Callback(new WaitAction(0.1f), ((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI::reopen);
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