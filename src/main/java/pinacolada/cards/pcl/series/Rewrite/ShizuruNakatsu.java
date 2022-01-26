package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.pcl.EnduranceStance;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;

public class ShizuruNakatsu extends PCLCard
{
    public static final PCLCardData DATA = Register(ShizuruNakatsu.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged, PCLCardTarget.AoE).SetSeriesFromClassPackage();

    private boolean canAttack;

    public ShizuruNakatsu()
    {
        super(DATA);

        Initialize(1, 4, 2, 5);
        SetUpgrade(0, 3, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(0, 0, 1);
    }

    @Override
    protected float GetInitialDamage()
    {
        if (EnduranceStance.IsActive())
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

        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
                .ShowEffect(true, true)
                .SetFilter(c -> c.type == CardType.SKILL)
                .SetOptions(false, true, false)
                .AddCallback((cards) -> {
                            if (cards.size() >= magicNumber) {
                                PCLActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
                            }
                        }
                );
    }
}