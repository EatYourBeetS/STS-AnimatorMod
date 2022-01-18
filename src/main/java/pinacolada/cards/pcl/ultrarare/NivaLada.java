package pinacolada.cards.pcl.ultrarare;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.interfaces.subscribers.OnAfterCardExhaustedSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.vfx.megacritCopy.LaserBeamEffect2;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class NivaLada extends PCLCard_UltraRare implements OnAfterCardExhaustedSubscriber, OnAfterCardDiscardedSubscriber
{
    public static final PCLCardData DATA = Register(NivaLada.class)
            .SetSkill(0, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika);

    public NivaLada()
    {
        super(DATA);

        Initialize(0, 0, 300);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1);

        SetCooldown(18, -2, this::OnCooldownCompleted);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onAfterCardDiscarded.Subscribe(this);
        PCLCombatStats.onAfterCardExhausted.Subscribe(this);
    }

    @Override
    public void OnAfterCardExhausted(AbstractCard card)
    {
        if (this.secondaryValue > 0)
        {
            cooldown.ProgressCooldown();
        }
    }

    @Override
    public void OnAfterCardDiscarded()
    {
        if (this.secondaryValue > 0)
        {
            cooldown.ProgressCooldown();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        if (m == null || m.isDeadOrEscaped())
        {
            m = PCLGameUtilities.GetRandomEnemy(true);
        }

        if (m.hasPower(IntangiblePower.POWER_ID))
        {
            PCLActions.Bottom.RemovePower(m, m, IntangiblePower.POWER_ID);
        }

        PCLActions.Bottom.VFX(new LaserBeamEffect2(player.hb.cX, player.hb.cY), 0.1f);
        PCLActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05f, 0.05f), m.hb.cY + MathUtils.random(-0.05f, 0.05f)), 0.1f);
        PCLActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05f, 0.05f), m.hb.cY + MathUtils.random(-0.05f, 0.05f)), 0.1f);
        PCLActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05f, 0.05f), m.hb.cY + MathUtils.random(-0.05f, 0.05f)), 0.1f);
        PCLActions.Bottom.DealDamage(player, m, magicNumber, DamageInfo.DamageType.THORNS, AttackEffects.NONE);
    }
}