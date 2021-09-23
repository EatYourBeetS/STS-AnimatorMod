package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.Objects;

public abstract class EYBStance extends AbstractStance
{
    protected static final HashMap<String, FuncT0<AbstractStance>> stances = new HashMap<>();
    protected static final HashMap<String, EYBCardTooltip> tooltips = new HashMap<>();
    protected static long sfxId = -1L;
    protected final AbstractCreature owner;
    protected final StanceStrings strings;

    public static void Initialize()
    {
        stances.clear();
        stances.put(ForceStance.STANCE_ID, ForceStance::new);
        stances.put(IntellectStance.STANCE_ID, IntellectStance::new);
        stances.put(AgilityStance.STANCE_ID, AgilityStance::new);

        tooltips.clear();
        tooltips.put(ForceStance.STANCE_ID, GR.Tooltips.ForceStance);
        tooltips.put(AgilityStance.STANCE_ID, GR.Tooltips.AgilityStance);
        tooltips.put(IntellectStance.STANCE_ID, GR.Tooltips.IntellectStance);
        tooltips.put(NeutralStance.STANCE_ID, GR.Tooltips.NeutralStance);
    }

    public static EYBCardTooltip GetStanceTooltip(String stance)
    {
        return tooltips.getOrDefault(stance, null);
    }

    public static AbstractStance GetStanceFromName(String name)
    {
        return stances.containsKey(name) ? stances.get(name).Invoke() : null;
    }

    public static String CreateFullID(Class<? extends EYBStance> type)
    {
        return GR.Common.CreateID(type.getSimpleName());
    }

    protected static Color CreateColor(float r1, float r2, float g1, float g2, float b1, float b2)
    {
        return new Color(MathUtils.random(r1, r2), MathUtils.random(g1, g2), MathUtils.random(b1, b2), 0);
    }

    protected abstract void QueueParticle();
    protected abstract void QueueAura();
    protected abstract Color GetMainColor();

    protected EYBStance(String id, AbstractCreature owner)
    {
        this.ID = id;
        this.strings = GR.GetStanceString(id);
        this.name = strings.NAME;
        this.owner = owner;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = strings.DESCRIPTION[0];
    }

    @Override
    public void updateAnimation()
    {
        if (!Settings.DISABLE_EFFECTS)
        {
            this.particleTimer -= GR.UI.Delta();
            if (this.particleTimer < 0f)
            {
                this.particleTimer = 0.04f;
                QueueParticle();
            }
        }

        this.particleTimer2 -= GR.UI.Delta();
        if (this.particleTimer2 < 0f)
        {
            this.particleTimer2 = MathUtils.random(0.45f, 0.55f);
            QueueAura();
        }
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        if (sfxId != -1L)
        {
            this.stopIdleSfx();
        }

        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        GameEffects.Queue.Add(new BorderFlashEffect(GetMainColor(), true));
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        this.stopIdleSfx();
    }

    public void onRefreshStance()
    {

    }

    @Override
    public void stopIdleSfx()
    {
        if (sfxId != -1L)
        {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
            sfxId = -1L;
        }
    }

    protected String FormatDescription(Object... args)
    {
        return JUtils.Format(strings.DESCRIPTION[0], args);
    }

    protected boolean TryApplyStance(String stanceID)
    {
        String current = CombatStats.GetCombatData(EYBStance.class.getSimpleName(), null);
        if (Objects.equals(stanceID, current))
        {
            return false;
        }

        CombatStats.SetCombatData(EYBStance.class.getSimpleName(), stanceID);
        return true;
    }
}
