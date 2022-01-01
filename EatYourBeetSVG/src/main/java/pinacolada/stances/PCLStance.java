package pinacolada.stances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.effects.stance.StanceAura;
import pinacolada.effects.stance.StanceParticleVertical;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.resources.GR;
import pinacolada.ui.combat.PCLAffinityRow;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class PCLStance extends AbstractStance
{
    protected static final HashMap<String, FuncT0<PCLStance>> stances = new HashMap<>();
    protected static final HashMap<String, PCLCardTooltip> tooltips = new HashMap<>();
    protected static final int GAIN = PCLAffinityRow.SYNERGY_MULTIPLIER * 2;
    protected static long sfxId = -1L;
    protected final AbstractCreature owner;
    protected final StanceStrings strings;
    public final PCLAffinity affinity;

    public static void Initialize()
    {
        stances.clear();
        stances.put(MightStance.STANCE_ID, MightStance::new);
        stances.put(WisdomStance.STANCE_ID, WisdomStance::new);
        stances.put(VelocityStance.STANCE_ID, VelocityStance::new);
        stances.put(EnduranceStance.STANCE_ID, EnduranceStance::new);
        stances.put(InvocationStance.STANCE_ID, InvocationStance::new);
        stances.put(DesecrationStance.STANCE_ID, DesecrationStance::new);

        tooltips.clear();
        tooltips.put(MightStance.STANCE_ID, GR.Tooltips.MightStance);
        tooltips.put(VelocityStance.STANCE_ID, GR.Tooltips.VelocityStance);
        tooltips.put(WisdomStance.STANCE_ID, GR.Tooltips.WisdomStance);
        tooltips.put(EnduranceStance.STANCE_ID, GR.Tooltips.EnduranceStance);
        tooltips.put(InvocationStance.STANCE_ID, GR.Tooltips.InvocationStance);
        tooltips.put(DesecrationStance.STANCE_ID, GR.Tooltips.DesecrationStance);
        tooltips.put(NeutralStance.STANCE_ID, GR.Tooltips.NeutralStance);
    }

    public static PCLCardTooltip GetStanceTooltip(String stance)
    {
        return tooltips.getOrDefault(stance, null);
    }

    public static AbstractStance GetRandomStance() {
        FuncT0<PCLStance> constructor = PCLJUtils.Random(stances.values());
        if (constructor == null) {
            return new NeutralStance();
        }
        return constructor.Invoke();
    }

    public static String GetRandomStanceID(String... filter) {
        final List<String> filters = Arrays.asList(filter);
        return PCLJUtils.Random(PCLJUtils.Filter(stances.keySet(), id -> !filters.contains(id)));
    }

    public static PCLStance GetStanceFromName(String name)
    {
        return stances.containsKey(name) ? stances.get(name).Invoke() : null;
    }

    public static PCLStance GetStanceFromPCLAffinity(PCLAffinity affinity)
    {
        switch (affinity) {
            case Red:
                return new MightStance();
            case Green:
                return new VelocityStance();
            case Blue:
                return new WisdomStance();
            case Orange:
                return new EnduranceStance();
            case Light:
                return new InvocationStance();
            case Dark:
                return new DesecrationStance();
            default:
                return null;
        }
    }

    public static String CreateFullID(Class<? extends PCLStance> type)
    {
        return GR.PCL.CreateID(type.getSimpleName());
    }

    protected static Color CreateColor(float r1, float r2, float g1, float g2, float b1, float b2)
    {
        return new Color(MathUtils.random(r1, r2), MathUtils.random(g1, g2), MathUtils.random(b1, b2), 0);
    }

    protected abstract Color GetAuraColor();
    protected abstract Color GetParticleColor();
    protected abstract Color GetMainColor();

    protected PCLStance(String id, PCLAffinity affinity, AbstractCreature owner)
    {
        this.ID = id;
        this.strings = GR.GetStanceString(id);
        this.name = strings.NAME;
        this.owner = owner;
        this.affinity = affinity;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = PCLJUtils.Format(strings.DESCRIPTION[0], GAIN);
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
        PCLGameEffects.Queue.Add(new BorderFlashEffect(GetMainColor(), true));

        AbstractPCLAffinityPower po = PCLCombatStats.MatchingSystem.GetPower(affinity);

        if (po != null && TryApplyStance(ID))
        {
            po.SetEffectMultiplier(po.effectMultiplier + 0.5f);
        }
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        this.stopIdleSfx();

        AbstractPCLAffinityPower po = PCLCombatStats.MatchingSystem.GetPower(affinity);
        if (po != null && TryApplyStance(null))
        {
            po.SetEffectMultiplier(po.effectMultiplier - 0.5f);
            PCLActions.Bottom.StackAffinityPower(affinity, GAIN);
        }
    }

    public void onRefreshStance()
    {
        PCLActions.Bottom.StackAffinityPower(affinity, GAIN);
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

    protected void QueueParticle()
    {
        PCLGameEffects.Queue.Add(new StanceParticleVertical(GetParticleColor()));
    }

    protected void QueueAura()
    {
        PCLGameEffects.Queue.Add(new StanceAura(GetAuraColor()));
    }

    protected String FormatDescription(Object... args)
    {
        return PCLJUtils.Format(strings.DESCRIPTION[0], args);
    }

    protected boolean TryApplyStance(String stanceID)
    {
        String current = CombatStats.GetCombatData(PCLStance.class.getSimpleName(), null);
        if (Objects.equals(stanceID, current))
        {
            return false;
        }

        CombatStats.SetCombatData(PCLStance.class.getSimpleName(), stanceID);
        return true;
    }
}
