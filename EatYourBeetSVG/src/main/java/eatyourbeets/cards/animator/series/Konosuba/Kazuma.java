package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.stances.GuardStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kazuma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kazuma.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Kazuma()
    {
        super(DATA);

        Initialize(5, 2, 2, 1);
        SetUpgrade(1, 1, 1);

        SetAffinity_Fire(1);
        SetAffinity_Air(1);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this,m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ChangeStance(GuardStance.STANCE_ID); //LZLZLZ Test

        this.baseDamage += magicNumber;

        if (info.IsSynergizing && GameUtilities.IsSameSeries(this, info.PreviousCard) && info.PreviousCard.baseBlock > 0) {
            info.PreviousCard.baseBlock += secondaryValue;
        };
    }
}