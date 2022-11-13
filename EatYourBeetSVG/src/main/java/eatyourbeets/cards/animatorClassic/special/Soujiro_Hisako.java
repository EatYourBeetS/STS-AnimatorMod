package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Hisako extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Soujiro_Hisako.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);

    public Soujiro_Hisako()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);

        SetEthereal(true);
        SetEvokeOrbCount(1);
        SetSpellcaster();
        this.series = CardSeries.LogHorizon;
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.EvokeOrb(1)
        .AddCallback(orbs ->
        {
            if (orbs.size() > 0)
            {
                GameActions.Bottom.GainIntellect(1, true);
            }
        });
    }
}