package eatyourbeets.ui.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.VividPicture;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class EnchantCampfireOption extends AbstractCampfireOption
{
    private RestRoom room;
    private LivingPicture relic;

    public static boolean CanUse()
    {
        return AbstractDungeon.player.hasRelic(LivingPicture.ID) || AbstractDungeon.player.hasRelic(VividPicture.ID);
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
        relic = GameUtilities.GetRelic(LivingPicture.ID);
        if (relic == null)
        {
            relic = GameUtilities.GetRelic(VividPicture.ID);
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