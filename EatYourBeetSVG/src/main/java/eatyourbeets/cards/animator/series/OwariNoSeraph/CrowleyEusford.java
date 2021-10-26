package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.stances.WrathStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.stances.GuardStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CrowleyEusford extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CrowleyEusford.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public CrowleyEusford()
    {
        super(DATA);

        Initialize(13, 0, 4);
        SetUpgrade(4, 0, 2);

        SetAffinity_Fire(2);
        SetAffinity_Air();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        int vigorToGain = magicNumber;

        vigorToGain += GameUtilities.GetPowerAmount(StrengthPower.POWER_ID);
        vigorToGain += GameUtilities.GetPowerAmount(DexterityPower.POWER_ID);

        GameActions.Bottom.GainVigor(vigorToGain);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY);

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_EnterStance(WrathStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(GuardStance.STANCE_ID));
        }
        choices.Select(1, m);
    }
}