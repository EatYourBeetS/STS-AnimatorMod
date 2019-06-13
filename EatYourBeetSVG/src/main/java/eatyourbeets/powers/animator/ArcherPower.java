package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

public class ArcherPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ArcherPower.class.getSimpleName());

    private int baseDamage;

    public ArcherPower(AbstractCreature owner, int damage)
    {
        super(owner, POWER_ID);
        this.baseDamage = damage;
        this.amount = damage + GetPlayerStrength();

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        this.baseDamage += stackAmount;
        this.amount = baseDamage + GetPlayerStrength();

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (target.isPlayer)
        {
            this.amount = baseDamage + GetPlayerStrength();

            updateDescription();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (isPlayer)
        {
            this.amount = baseDamage + GetPlayerStrength();

            updateDescription();

            for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
            {
                for (AbstractPower p : m.powers)
                {
                    if (p.type == PowerType.DEBUFF)
                    {

                        float x = m.hb.cX + (m.hb.width * AbstractDungeon.miscRng.random(-0.1f, 0.1f));
                        float y = m.hb.cY + (m.hb.height * AbstractDungeon.miscRng.random(-0.2f, 0.2f));

                        AbstractDungeon.actionManager.addToTop(new VFXAction(new ThrowDaggerEffect(x, y)));
                        GameActionsHelper.DamageTarget(AbstractDungeon.player, m, this.amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
                    }
                }
            }

            this.flash();
        }
    }

    private int GetPlayerStrength()
    {
        AbstractPlayer player = AbstractDungeon.player;
        if (player.hasPower(StrengthPower.POWER_ID))
        {
            return player.getPower(StrengthPower.POWER_ID).amount;
        }
        return 0;
    }
}
