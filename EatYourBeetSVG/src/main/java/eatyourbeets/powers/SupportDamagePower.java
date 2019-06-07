package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.GameActionsHelper;

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
        ArrayList<AbstractMonster> enemies = PlayerStatistics.GetCurrentEnemies(true);

        AbstractMonster target = null;
        int minHealth = Integer.MAX_VALUE;
        for (AbstractMonster m : enemies)
        {
            if (m.currentHealth < minHealth)
            {
                minHealth = m.currentHealth;
                target = m;
            }
        }

        if (target != null)
        {
            if (amount < 10)
            {
                GameActionsHelper.VFX(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.0F);
            }
            else
            {
                GameActionsHelper.VFX(new DieDieDieEffect());
            }

            GameActionsHelper.DamageTarget(owner, target, this.amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }
    }
}
