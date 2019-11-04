package eatyourbeets.powers.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

public class GeassPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(GeassPower.class.getSimpleName());

    public GeassPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        amount = -1;
        type = PowerType.DEBUFF;

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (power.type == PowerType.DEBUFF && owner == source && (owner.isPlayer != target.isPlayer))
        {
            target.powers.add(0, new ArtifactPower(target, 1));
        }
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        AbstractMonster monster = Utilities.SafeCast(owner, AbstractMonster.class);
        if (monster != null && !PlayerStatistics.IsAttacking(monster.intent))
        {
            if (!monster.hasPower(StunMonsterPower.POWER_ID))
            {
                GameActionsHelper.ApplyPower(owner, owner, new StunMonsterPower(monster));
            }

            GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }
}