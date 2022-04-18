package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.stance.StanceAura;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.CorruptionPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class CorruptionStance extends EYBStance
{
    public static final Affinity AFFINITY = CorruptionPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(CorruptionStance.class);
    public static final int STAT_GAIN_AMOUNT = 2;
    public static final int ENERGY_AMOUNT = 3;

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

    public CorruptionStance()
    {
        super(STANCE_ID, AbstractDungeon.player);
    }

    protected Color GetParticleColor()
    {
        return CreateColor(0.5f, 0.6f, 0.05f, 0.15f, 0.8f, 0.9f);
    }

    protected Color GetAuraColor()
    {
        return CreateColor(0.5f, 0.6f, 0.05f, 0.15f, 0.8f, 0.9f);
    }

    @Override
    public void onEnterStance()
    {
        //super.onEnterStance();

        if (sfxId != -1L)
        {
            this.stopIdleSfx();
        }

        SFX.Play(SFX.ATTACK_PIERCING_WAIL, 0.38f, 0.42f);
        sfxId = CardCrawlGame.sound.playAndLoop(SFX.STANCE_LOOP_WRATH);
        GameEffects.Queue.Add(new BorderFlashEffect(GetMainColor(), true));

        if (TryApplyStance(STANCE_ID))
        {
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength, +STAT_GAIN_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, +STAT_GAIN_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus, +STAT_GAIN_AMOUNT);
        }
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        this.stopIdleSfx();

        GameActions.Bottom.GainEnergy(3);

        if (TryApplyStance(null))
        {
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength, -STAT_GAIN_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, -STAT_GAIN_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus, -STAT_GAIN_AMOUNT);
        }
    }

    @Override
    public void stopIdleSfx()
    {
        if (sfxId != -1L)
        {
            CardCrawlGame.sound.stop("STANCE_LOOP_WRATH", sfxId);
            sfxId = -1L;
        }
    }

    @Override
    public void onRefreshStance()
    {

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
        description = FormatDescription(STAT_GAIN_AMOUNT, ENERGY_AMOUNT);
    }
}
