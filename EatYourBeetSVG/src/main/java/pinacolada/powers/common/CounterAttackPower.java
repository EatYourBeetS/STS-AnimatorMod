package pinacolada.powers.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class CounterAttackPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(CounterAttackPower.class);
    public static final int VULNERABLE_AMOUNT = 1;
    public static boolean retain = false;
    private AbstractGameAction.AttackEffect attackEffect;

    public CounterAttackPower(AbstractCreature owner, int amount)
    {
        this(owner, amount, AttackEffects.BLUNT_HEAVY);
    }

    public CounterAttackPower(AbstractCreature owner, int amount, AbstractGameAction.AttackEffect attackEffect)
    {
        super(owner, POWER_ID);
        this.attackEffect = attackEffect;

        Initialize(amount, PowerType.BUFF, false);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ATTACK_IRON_1, 1.25f, 1.35f, 0.7f);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount < info.output)
        {
            int[] damageMatrix = DamageInfo.createDamageMatrix(amount, false);
            PCLActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.NORMAL, attackEffect);
            ReducePower(1);
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (!retain) {
            RemovePower();
        }

    }
}