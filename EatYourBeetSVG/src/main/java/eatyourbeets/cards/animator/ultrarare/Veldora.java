package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Veldora extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Veldora.class)
            .SetSkill(4, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TenseiSlime);

    public Veldora()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 1, 2);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Blue(2);

        showEvokeValue = true;
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);

        GameActions.Bottom.ChannelOrb(AbstractOrb.getRandomOrb(true));
        GameActions.Bottom.ChannelRandomOrb(2);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainIntellect(secondaryValue);

        int orbCount = p.filledOrbCount();
        for (int i = 0; i < magicNumber - 1; i++)
        {
            for (AbstractOrb orb : p.orbs)
            {
                if (!(orb instanceof EmptyOrbSlot))
                {
                    GameActions.Bottom.Callback(orb, (orb_, __) ->
                    {
                        orb_.triggerEvokeAnimation();
                        orb_.onEvoke();
                    });
                }
            }
        }

        GameActions.Bottom.Add(new AnimateOrbAction(orbCount));
        GameActions.Bottom.Add(new EvokeOrbAction(orbCount));
    }
}