package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Yuuichirou_Asuramaru;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Yuuichirou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yuuichirou.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data
                    .AddPreview(new Yuuichirou_Asuramaru(), true));

    public Yuuichirou()
    {
        super(DATA);

        Initialize(7, 0, 1);
        SetUpgrade(3, 0);

        SetAffinity_Fire(2, 0, 1);
        SetAffinity_Air(1, 1, 1);

        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.Draw(1);

        if (AgilityStance.IsActive()) {
            GameUtilities.MaintainPower(Affinity.Fire);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new Yuuichirou_Asuramaru()).SetUpgrade(upgraded, false);
    }
}