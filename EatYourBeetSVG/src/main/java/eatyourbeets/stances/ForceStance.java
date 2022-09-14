package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.stance.StanceAura;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.animatorClassic.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ForceStance extends EYBStance
{
    public static final Affinity AFFINITY = ForcePower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(ForceStance.class);
    public static final int STAT_GAIN_AMOUNT = 1;

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

    public ForceStance()
    {
        super(STANCE_ID, AbstractDungeon.player);
    }

    protected Color GetParticleColor()
    {
        return CreateColor(0.8f, 0.9f, 0.3f, 0.4f, 0.2f, 0.3f);
    }

    protected Color GetAuraColor()
    {
        return CreateColor(0.8f, 0.9f, 0.3f, 0.4f, 0.2f, 0.3f);
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        //GameActions.Bottom.StackAffinityPower(AFFINITY, 1, true);

        if (TryApplyStance(STANCE_ID))
        {
            if (AbstractDungeon.actionManager.currentAction instanceof EYBAction)
            {
                final AbstractCard card = ((EYBAction) AbstractDungeon.actionManager.currentAction).sourceCard;
                if (card != null)
                {
                    onPlayCard(card);
                }
            }

            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength, +STAT_GAIN_AMOUNT);
        }
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        if (TryApplyStance(null))
        {
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength, -STAT_GAIN_AMOUNT);
        }
    }

    @Override
    public void onPlayCard(AbstractCard card)
    {
        super.onPlayCard(card);

        if (card.type == AbstractCard.CardType.ATTACK)
        {
            final int amount = card.costForTurn;
            if (amount > 0)
            {
                GameActions.Bottom.TakeDamageAtEndOfTurn(amount, AttackEffects.NONE).ShowEffect(false, true);
                GameActions.Bottom.StackPower(new VigorPower(owner, amount * 2)).ShowEffect(true, true);
            }
        }
    }

    @Override
    public void onRefreshStance()
    {
        //GameActions.Bottom.StackAffinityPower(AFFINITY, 1, true);
    }

    @Override
    protected void QueueParticle()
    {
        GameEffects.Queue.Add(new StanceParticleVertical(GetParticleColor()));
    }

    @Override
    protected void QueueAura()
    {
        GameEffects.Queue.Add(new StanceAura(GetAuraColor()));
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(1f, 0.3f, 0.2f, 1f);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(STAT_GAIN_AMOUNT);
    }
}
