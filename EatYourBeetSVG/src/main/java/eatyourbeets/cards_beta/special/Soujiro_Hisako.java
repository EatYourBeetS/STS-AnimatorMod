package eatyourbeets.cards_beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards_beta.LogHorizon.Soujiro;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Hisako extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Hisako.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Soujiro.DATA.Series);

    public Soujiro_Hisako()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetEthereal(true);
        SetEvokeOrbCount(1);
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