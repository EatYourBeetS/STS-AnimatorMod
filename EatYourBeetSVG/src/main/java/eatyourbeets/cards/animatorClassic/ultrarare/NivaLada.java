package eatyourbeets.cards.animatorClassic.ultrarare;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.interfaces.subscribers.OnAfterCardExhaustedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NivaLada extends AnimatorClassicCard_UltraRare implements OnAfterCardExhaustedSubscriber, OnAfterCardDiscardedSubscriber
{
    public static final EYBCardData DATA = Register(NivaLada.class).SetSkill(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public NivaLada()
    {
        super(DATA);

        Initialize(0, 0, 300);
        SetUpgrade(0, 0, 0);

        SetCooldown(18, -2, this::OnCooldownCompleted);
        this.series = CardSeries.HitsugiNoChaika;
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAfterCardDiscarded.Subscribe(this);
        CombatStats.onAfterCardExhausted.Subscribe(this);
    }

    @Override
    public void OnAfterCardExhausted(AbstractCard card)
    {
        if (this.secondaryValue > 0)
        {
            cooldown.ProgressCooldown(false);
        }
    }

    @Override
    public void OnAfterCardDiscarded()
    {
        if (this.secondaryValue > 0)
        {
            cooldown.ProgressCooldown(false);
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
            m = GameUtilities.GetRandomEnemy(true);
        }

        if (m.hasPower(IntangiblePower.POWER_ID))
        {
            GameActions.Bottom.RemovePower(m, m, IntangiblePower.POWER_ID);
        }

        GameActions.Bottom.VFX(new LaserBeamEffect(player.hb.cX, player.hb.cY), 0.1f);
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05f, 0.05f), m.hb.cY + MathUtils.random(-0.05f, 0.05f)), 0.1f);
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05f, 0.05f), m.hb.cY + MathUtils.random(-0.05f, 0.05f)), 0.1f);
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05f, 0.05f), m.hb.cY + MathUtils.random(-0.05f, 0.05f)), 0.1f);
        GameActions.Bottom.DealDamage(player, m, magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
    }
}