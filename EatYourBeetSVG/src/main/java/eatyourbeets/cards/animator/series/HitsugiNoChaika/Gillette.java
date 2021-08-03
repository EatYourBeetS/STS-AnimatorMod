package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Gillette extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gillette.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeries(CardSeries.HitsugiNoChaika);

    public Gillette()
    {
        super(DATA);

        Initialize(7, 0, 1);
        SetUpgrade(3, 0, 0);

        SetAffinity_Orange(1);
        SetAffinity_Light(1, 1, 0);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.StackPower(new EnergizedPower(p, 1));
    }
}