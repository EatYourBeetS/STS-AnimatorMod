package eatyourbeets.ui.common;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
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
    private RestRoom room;
    private EnchantableRelic relic;

    public static boolean CanUse()
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
        this.label = "Enchant";
        this.description = "Improve the effects of Living Picture.";
        this.usable = CanUse();
        this.img = GR.Common.Images.CampfireOption_Enchant.Texture();
    }

    @Override
    public void useOption()
    {
        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            EnchantableRelic er = JUtils.SafeCast(r, EnchantableRelic.class);
            if (er != null && er.GetEnchantmentLevel() < 2)
            {
                relic = er;
            }
        }

        if (relic == null)
        {
            GameEffects.Queue.Callback(new WaitAction(0.1f), ((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI::reopen);
            JUtils.LogWarning(this, "EnchantableRelic not found, despite 'usable' being true.");
            usable = false;
            return;
        }

        room = (RestRoom) AbstractDungeon.getCurrRoom();
        room.campfireUI.somethingSelected = true;

        GameEffects.Queue.Callback(new SelectFromPile(relic.name, 1, relic.CreateUpgradeGroup())
        .CancellableFromPlayer(true)
        .AddCallback(c ->
        {
            if (c.size() > 0)
            {
                CampfireUI.hidden = true;
                AbstractRoom.waitTimer = 0.0F;
                room.phase = AbstractRoom.RoomPhase.COMPLETE;
                room.cutFireSound();
                OnEffectSelected((Enchantment)c.get(0));
            }
        }), () ->
        {
            if (room.phase == AbstractRoom.RoomPhase.COMPLETE)
            {
                room.fadeIn();
            }
            else
            {
                room.campfireUI.reopen();
            }
        })
        .ShowBlackScreen(0.95f);
    }

    public void OnEffectSelected(Enchantment enchantment)
    {
        relic.ApplyEnchantment(enchantment);
        relic.flash();
    }
}