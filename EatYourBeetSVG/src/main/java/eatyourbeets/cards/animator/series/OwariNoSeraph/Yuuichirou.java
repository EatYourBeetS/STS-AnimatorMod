package eatyourbeets.cards.animator.series.OwariNoSeraph;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Yuuichirou_Asuramaru;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Yuuichirou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yuuichirou.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Yuuichirou_Asuramaru(), true);
    }

    public Yuuichirou()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(3, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(1, 1, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.Draw(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new Yuuichirou_Asuramaru()).SetUpgrade(upgraded, false);
    }
}