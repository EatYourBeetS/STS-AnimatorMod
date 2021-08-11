package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ElricAlphonse_Alt;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ElricAlphonse extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricAlphonse.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final ElricAlphonse_Alt PREVIEW = new ElricAlphonse_Alt();
    static
    {
        DATA.AddPreview(PREVIEW, true);
    }

    public ElricAlphonse()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetEthereal(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(PREVIEW).SetUpgrade(upgraded, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainIntellect(GameUtilities.GetPowerAmount(Affinity.Blue) <= magicNumber ? 1 : 0, true);
    }
}