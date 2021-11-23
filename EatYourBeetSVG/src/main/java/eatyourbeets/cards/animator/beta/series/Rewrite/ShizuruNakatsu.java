package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.Arrays;

public class ShizuruNakatsu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShizuruNakatsu.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    private boolean canAttack;

    public ShizuruNakatsu()
    {
        super(DATA);

        Initialize(1, 3, 2, 6);
        SetUpgrade(0, 3, 0);
        SetAffinity_Green(2, 0, 1);
        SetAffinity_Orange(0, 0, 1);
    }

    @Override
    protected float GetInitialDamage()
    {
        if (CheckSpecialCondition(false))
        {
            return super.GetInitialDamage() + secondaryValue;
        }
        return super.GetInitialDamage();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.GUNSHOT);
        GameActions.Bottom.GainBlock(block);

        if (!player.stance.ID.equals(VelocityStance.STANCE_ID))
        {
            GameActions.Bottom.DiscardFromHand(name, magicNumber, true)
                    .ShowEffect(true, true)
                    .SetFilter(c -> c.type == CardType.SKILL)
                    .SetOptions(false, false, false)
                    .AddCallback(() -> GameActions.Bottom.ChangeStance(VelocityStance.STANCE_ID));
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        Affinity highestAffinity = JUtils.FindMax(Arrays.asList(Affinity.Extended()), this::GetHandAffinity);
        return (highestAffinity.equals(Affinity.Green));
    }
}