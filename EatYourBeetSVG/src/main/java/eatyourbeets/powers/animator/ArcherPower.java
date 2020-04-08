package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ArcherPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ArcherPower.class);

    public ArcherPower(AbstractCreature owner, int damage)
    {
        super(owner, POWER_ID);

        this.amount = damage;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (isPlayer)
        {
            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                for (int i = 0; i < GameUtilities.GetDebuffsCount(m.powers); i++)
                {
                    float x = m.hb.cX + (m.hb.width * AbstractDungeon.cardRandomRng.random(-0.1f, 0.1f));
                    float y = m.hb.cY + (m.hb.height * AbstractDungeon.cardRandomRng.random(-0.2f, 0.2f));

                    GameActions.Bottom.VFX(new ThrowDaggerEffect(x, y));
                    GameActions.Bottom.DealDamage(owner, m, this.amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE)
                    .SetVFX(true, true);
                }
            }

            this.flash();
        }
    }
}
