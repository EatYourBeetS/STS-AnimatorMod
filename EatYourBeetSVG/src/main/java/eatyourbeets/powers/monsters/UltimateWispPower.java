package eatyourbeets.powers.monsters;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import patches.CardGlowBorderPatches;

public class UltimateWispPower extends AnimatorPower implements OnStartOfTurnPostDrawSubscriber, OnBattleStartSubscriber
{
    public static final String POWER_ID = CreateFullID(UltimateWispPower.class);

    private static final Color RED = Color.RED.cpy();

    private boolean shouldExhaust = false;

    public UltimateWispPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        Initialize(-1);
        OnBattleStart();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        OnBattleStart();

        shouldExhaust = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        shouldExhaust = true;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        super.onUseCard(card, action);

        if (shouldExhaust)
        {
            action.actionType = AbstractGameAction.ActionType.EXHAUST;
            action.exhaustCard = true;
            shouldExhaust = false;
            CardGlowBorderPatches.overrideColor = null;
        }
    }

    @Override
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        super.onInflictDamage(info, damageAmount, target);

        if (target != owner && damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS)
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Burn());
        }
    }

    @Override
    public void OnBattleStart()
    {
        CombatStats.onBattleStart.Subscribe(this);
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        CardGlowBorderPatches.overrideColor = RED;
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        CardGlowBorderPatches.overrideColor = RED;
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        if (!player.hasBlight(eatyourbeets.blights.animator.UltimateWisp.ID))
        {
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(player.hb.cX, player.hb.cY, new eatyourbeets.blights.animator.UltimateWisp());
        }
    }
}