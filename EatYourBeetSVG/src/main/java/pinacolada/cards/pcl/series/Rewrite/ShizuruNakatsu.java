package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.VelocityStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.Arrays;

public class ShizuruNakatsu extends PCLCard
{
    public static final PCLCardData DATA = Register(ShizuruNakatsu.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeriesFromClassPackage();

    private boolean canAttack;

    public ShizuruNakatsu()
    {
        super(DATA);

        Initialize(1, 3, 2, 6);
        SetUpgrade(0, 3, 0);
        SetAffinity_Green(1, 0, 1);
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
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.GUNSHOT);
        PCLActions.Bottom.GainBlock(block);

        if (!player.stance.ID.equals(VelocityStance.STANCE_ID))
        {
            PCLActions.Bottom.DiscardFromHand(name, magicNumber, true)
                    .ShowEffect(true, true)
                    .SetFilter(c -> c.type == CardType.SKILL)
                    .SetOptions(false, false, false)
                    .AddCallback(() -> PCLActions.Bottom.ChangeStance(VelocityStance.STANCE_ID));
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        PCLAffinity highestAffinity = PCLJUtils.FindMax(Arrays.asList(PCLAffinity.Extended()), this::GetHandAffinity);
        return (highestAffinity.equals(PCLAffinity.Green));
    }
}