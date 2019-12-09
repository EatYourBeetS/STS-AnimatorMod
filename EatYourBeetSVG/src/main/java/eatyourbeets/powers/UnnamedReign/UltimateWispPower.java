package eatyourbeets.powers.UnnamedReign;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnStartOfTurnPostDrawSubscriber;
import patches.CardGlowBorderPatch;

public class UltimateWispPower extends AnimatorPower implements OnStartOfTurnPostDrawSubscriber, OnBattleStartSubscriber
{
    private boolean shouldExhaust = false;

    private static final Color RED = Color.RED.cpy();

    public static final String POWER_ID = CreateFullID(UltimateWispPower.class.getSimpleName());

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
            CardGlowBorderPatch.overrideColor = null;
        }
    }

    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (target != owner && damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS)
        {
            GameActionsHelper_Legacy.MakeCardInDiscardPile(new Burn(), 1, false);
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onBattleStart.Subscribe(this);
        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
        CardGlowBorderPatch.overrideColor = RED;
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        CardGlowBorderPatch.overrideColor = RED;
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