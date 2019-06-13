package eatyourbeets.powers.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

public class GeassPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(GeassPower.class.getSimpleName());

    public GeassPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        type = PowerType.DEBUFF;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        AbstractMonster monster = Utilities.SafeCast(owner, AbstractMonster.class);
        if (monster != null && (
                monster.intent != AbstractMonster.Intent.ATTACK &&
                monster.intent != AbstractMonster.Intent.ATTACK_DEBUFF &&
                monster.intent != AbstractMonster.Intent.ATTACK_BUFF &&
                monster.intent != AbstractMonster.Intent.ATTACK_DEFEND))
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