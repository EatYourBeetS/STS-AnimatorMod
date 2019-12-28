package eatyourbeets.powers.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

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
            ArtifactPower artifact = GameUtilities.GetPower(owner, ArtifactPower.POWER_ID);
            if (artifact != null)
            {
                artifact.amount += 1;
            }
            else
            {
                target.powers.add(0, new ArtifactPower(target, 1));
            }
        }
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        AbstractMonster monster = JavaUtilities.SafeCast(owner, AbstractMonster.class);
        if (monster != null && !GameUtilities.IsAttacking(monster.intent))
        {
            if (!monster.hasPower(StunMonsterPower.POWER_ID))
            {
                GameActions.Bottom.ApplyPower(owner, owner, new StunMonsterPower(monster));
            }

            RemovePower();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        RemovePower();
    }
}