package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.SupportDamageAction;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class SupportDamagePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SupportDamagePower.class.getSimpleName());

    public SupportDamagePower(AbstractCreature owner, int stacks)
    {
        super(owner, POWER_ID);

        this.amount = stacks;
        this.type = PowerType.BUFF;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        DamageInfo info = new DamageInfo(owner, amount, DamageInfo.DamageType.NORMAL);
        GameActions.Bottom.Add(new SupportDamageAction(info));
    }

    private static AbstractMonster FindLowestHPEnemy()
    {
        ArrayList<AbstractMonster> enemies = GameUtilities.GetAllEnemies(true);

        AbstractMonster enemy = null;
        int minHealth = Integer.MAX_VALUE;
        for (AbstractMonster m : enemies)
        {
            if (m.currentHealth < minHealth)
            {
                minHealth = m.currentHealth;
                enemy = m;
            }
        }

        return enemy;
    }
}