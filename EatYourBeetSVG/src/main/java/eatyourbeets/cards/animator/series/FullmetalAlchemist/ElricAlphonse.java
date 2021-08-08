package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ElricAlphonseAlt;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class ElricAlphonse extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricAlphonse.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new ElricAlphonseAlt(), true);
    }

    public ElricAlphonse()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);
        SetAffinity_Light(1);

        SetEthereal(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new ElricAlphonseAlt()).SetUpgrade(upgraded, false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (CombatStats.Affinities.GetPowerAmount(Affinity.Blue) <= magicNumber)
        {
            GameActions.Bottom.GainIntellect(1, true);
        }
    }
}