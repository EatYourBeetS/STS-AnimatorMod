package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Zadkiel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zadkiel.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.DateALive);

    public Zadkiel()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetCostUpgrade(-1);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainOrbSlots(1);
        GameActions.Bottom.Callback(() ->
                GameActions.Bottom.ChannelOrbs(Frost::new, JUtils.Count(player.orbs, o -> o instanceof EmptyOrbSlot)));
    }
}