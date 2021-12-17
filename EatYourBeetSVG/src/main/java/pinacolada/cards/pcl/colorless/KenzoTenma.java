package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KenzoTenma extends PCLCard
{
    public static final PCLCardData DATA = Register(KenzoTenma.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Monster);

    public KenzoTenma()
    {
        super(DATA);

        Initialize(2, 0, 3 , 2);
        SetUpgrade(1, 0, 1 , 0);

        SetAffinity_Orange(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public int GetXValue() {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Orange,true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount * GetXValue());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);
        PCLActions.Bottom.Callback(m, (enemy, __) -> {
            if (!PCLGameUtilities.IsDeadOrEscaped(enemy)) {
                int stacks = 0;
                for (AbstractPower po : enemy.powers) {
                    if (po.type == AbstractPower.PowerType.DEBUFF) {
                        stacks += po.amount;
                        PCLActions.Bottom.RemovePower(player, po);
                    }
                }
                if (stacks > 0) {
                    PCLActions.Bottom.RecoverHP(stacks * magicNumber);
                }
            }
        });
    }
}