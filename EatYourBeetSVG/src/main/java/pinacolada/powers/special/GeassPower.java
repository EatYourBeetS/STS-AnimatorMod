package pinacolada.powers.special;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import pinacolada.interfaces.subscribers.OnDamageActionSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.Collections;

public class GeassPower extends PCLPower implements OnTryApplyPowerListener, OnDamageActionSubscriber
{
    public static final String POWER_ID = CreateFullID(GeassPower.class);

    public GeassPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        Initialize(-1, PowerType.DEBUFF, false);
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action)
    {
        return !enabled || (power.type != PowerType.DEBUFF || owner != source || (owner.isPlayer == target.isPlayer));
    }

    @Override
    public void onRemove()
    {
        super.onRemove();
        PCLCombatStats.onDamageAction.Unsubscribe(this);

        PCLActions.Last.Callback(() ->
        {
           if (!owner.powers.contains(this))
           {
               owner.powers.add(this);
               Collections.sort(owner.powers);
           }
        });
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
        PCLCombatStats.onDamageAction.Subscribe(this);

        final AbstractMonster monster = PCLJUtils.SafeCast(owner, AbstractMonster.class);
        if (monster != null && !PCLGameUtilities.IsAttacking(monster.intent))
        {
            if (!monster.hasPower(StunMonsterPower.POWER_ID))
            {
                PCLActions.Bottom.ApplyPower(owner, owner, new StunMonsterPower(monster));
            }

            SetEnabled(false);
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        SetEnabled(false);
    }

    @Override
    public void OnDamageAction(AbstractGameAction action, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        if (action.source.powers.contains(this) && enabled) {
            info.applyPowers(action.source, action.source);
            action.target = action.source;
        }
    }
}