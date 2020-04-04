package eatyourbeets.powers.monsters;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import patches.CardGlowBorderPatches;

public class UltimateWispPower extends AnimatorPower implements OnStartOfTurnPostDrawSubscriber, OnBattleStartSubscriber
{
    public static final String POWER_ID = CreateFullID(UltimateWispPower.class);

    private static final Color RED = Color.RED.cpy();

    private boolean shouldExhaust = false;

    public UltimateWispPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = -1;

        OnBattleStart();

        updateDescription();
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
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0];
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
        if (target != owner && damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS)
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Burn());
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onBattleStart.Subscribe(this);
        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
        CardGlowBorderPatches.overrideColor = RED;
    }

    @Override
    public void onRemove()
    {
        super.onRemove();
        PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
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

        AbstractPlayer p = AbstractDungeon.player;
        if (!AbstractDungeon.player.hasBlight(eatyourbeets.blights.animator.UltimateWisp.ID))
        {
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(p.hb.cX, p.hb.cY, new eatyourbeets.blights.animator.UltimateWisp());
        }
    }
}