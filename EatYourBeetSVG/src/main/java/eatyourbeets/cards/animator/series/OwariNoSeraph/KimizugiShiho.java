package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KimizugiShiho extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KimizugiShiho.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public KimizugiShiho()
    {
        super(DATA);

        Initialize(4, 0, 2, 3);
        SetUpgrade(1, 0, 0, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Green(1);

        SetCardPreview(GameUtilities::IsHindrance);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainForce(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainForce(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER).SetVFXColor(Color.RED);
        }

        GameActions.Bottom.StackAffinityPower(Affinity.Red);
        GameActions.Bottom.Draw(1).SetFilter(GameUtilities::IsHindrance, false);
    }
}