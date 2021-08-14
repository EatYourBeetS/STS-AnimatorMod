package eatyourbeets.ui.common;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import eatyourbeets.cards.animator.beta.colorless.Kirby;
import eatyourbeets.effects.special.KirbyEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;

public class KirbyCampfireOption extends AbstractCampfireOption
{
    private Kirby kirby;
    private RestRoom room;

    public static boolean CanAddOption()
    {
        return AbstractDungeon.player.masterDeck.findCardById(Kirby.DATA.ID) != null;
    }

    public KirbyCampfireOption()
    {
        kirby = (Kirby) AbstractDungeon.player.masterDeck.findCardById(Kirby.DATA.ID);
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
        this.description = "Regurgitate cards from Kirby (Free option).";
        this.img = GR.Common.Images.CampfireOption_Kirby.Texture();
    }

    @Override
    public void useOption()
    {
        Refresh();

        if (!usable || kirby == null)
        {
            GameEffects.Queue.Callback(new WaitAction(0.1f), ((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI::reopen);
            return;
        }

        room = (RestRoom) AbstractDungeon.getCurrRoom();
        room.campfireUI.somethingSelected = true;

        GameEffects.Queue.Add(new KirbyEffect(kirby, kirby.COPIED_CARDS)).AddCallback(() -> {
            room.campfireUI.reopen();
        });
    }
}