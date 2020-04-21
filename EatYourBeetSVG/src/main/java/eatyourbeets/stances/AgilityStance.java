package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.effects.stance.StanceParticleHorizontal;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.interfaces.subscribers.OnLoseHpSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.DeenergizedPower;
import eatyourbeets.powers.common.PiercingNextTurnPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class AgilityStance extends EYBStance
{
    public static final String STANCE_ID = CreateFullID(AgilityStance.class);
    public static int STAT_GAIN_AMOUNT = 2;
    public static int STAT_LOSE_AMOUNT = 1;
    public static int DRAW_CARD_AMOUNT = 1;

    public AgilityStance()
    {
        super(STANCE_ID, AbstractDungeon.player);
    }

    protected Color GetParticleColor()
    {
        return CreateColor(0.2f, 0.3f, 0.7f, 0.8f, 0.2f, 0.3f);
    }

    protected Color GetAuraColor()
    {
        return CreateColor(0.4f, 0.5f, 0.8f, 0.9f, 0.4f, 0.5f);
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.GainDexterity(STAT_GAIN_AMOUNT);
        GameActions.Bottom.GainStrength(-STAT_LOSE_AMOUNT)
        .ShowEffect(false, true)
        .IgnoreArtifact(true);
        GameActions.Bottom.GainFocus(-STAT_LOSE_AMOUNT)
        .ShowEffect(false, true)
        .IgnoreArtifact(true);
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        GameActions.Bottom.ApplyPower(new DrawCardNextTurnPower(AbstractDungeon.player, DRAW_CARD_AMOUNT));
        GameActions.Bottom.GainDexterity(-STAT_GAIN_AMOUNT)
        .ShowEffect(false, true)
        .IgnoreArtifact(true);
        GameActions.Bottom.GainStrength(STAT_LOSE_AMOUNT);
        GameActions.Bottom.GainFocus(STAT_LOSE_AMOUNT);
    }

    @Override
    protected void QueueParticle()
    {
        GameEffects.Queue.Add(new StanceParticleHorizontal(GetParticleColor()));
        GameEffects.Queue.Add(new StanceParticleVertical(GetAuraColor()));
    }

    @Override
    protected void QueueAura()
    {
        //GameEffects.Queue.Add(new StanceAura(GetAuraColor()));
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.2f, 1f, 0.2f, 1f);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(STAT_GAIN_AMOUNT, STAT_LOSE_AMOUNT, DRAW_CARD_AMOUNT);
    }
}
