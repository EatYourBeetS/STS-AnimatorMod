package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.NoGameNoLife.Sora;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Sora_Strategy2 extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sora_Strategy2.class)
            .SetImagePath(Sora_Strategy1.DATA.ImagePath)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Sora.DATA.Series);

    public Sora_Strategy2()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetUpgrade(0,0,1);

        SetAffinity_Blue(2);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Exhaust(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryArtifact(magicNumber);
    }
}