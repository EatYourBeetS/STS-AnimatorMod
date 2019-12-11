package eatyourbeets.cards.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.effects.LaserBeam2Effect;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnAfterCardDiscardedSubscriber;
import eatyourbeets.interfaces.OnAfterCardExhaustedSubscriber;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.utilities.GameUtilities;

public class NivaLada extends AnimatorCard_UltraRare implements OnBattleStartSubscriber, OnAfterCardExhaustedSubscriber, OnAfterCardDiscardedSubscriber
{
    public static final String ID = Register(NivaLada.class.getSimpleName(), EYBCardBadge.Special);

    public NivaLada()
    {
        super(ID, 0, CardType.SKILL, CardTarget.ENEMY);

        Initialize(0, 0, 300, GetBaseCooldown());

        if (GameUtilities.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onAfterCardDiscarded.Subscribe(this);
        PlayerStatistics.onAfterCardExhausted.Subscribe(this);
    }

    @Override
    public void OnAfterCardExhausted(AbstractCard card)
    {
        if (this.secondaryValue > 0)
        {
            ProgressCooldown();
        }
    }

    @Override
    public void OnAfterCardDiscarded()
    {
        if (this.secondaryValue > 0)
        {
            ProgressCooldown();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (ProgressCooldown())
        {
            OnCooldownCompleted(AbstractDungeon.player, GameUtilities.GetRandomEnemy(true));
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        this.isSecondaryValueModified = (this.secondaryValue == 0);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(-2);
        }
    }

    protected int GetBaseCooldown()
    {
        return upgraded ? 16 : 18;
    }

    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        if (m == null || m.isDeadOrEscaped())
        {
            m = GameUtilities.GetRandomEnemy(true);
        }

        if (m.hasPower(IntangiblePower.POWER_ID))
        {
            GameActions.Bottom.RemovePower(m, m, IntangiblePower.POWER_ID);
        }

        GameActions.Bottom.VFX(new LaserBeam2Effect(p.hb.cX, p.hb.cY), 0.1F);
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05F, 0.05F), m.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F);
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05F, 0.05F), m.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F);
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05F, 0.05F), m.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F);
        GameActions.Bottom.DealDamage(p, m, this.magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
    }

    protected boolean ProgressCooldown()
    {
        boolean activate;
        int newValue;
        if (secondaryValue <= 0)
        {
            newValue = GetBaseCooldown();
            activate = true;
        }
        else
        {
            newValue = secondaryValue - 1;
            activate = false;
        }

        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            NivaLada card = (NivaLada)c;
            card.baseSecondaryValue = card.secondaryValue = newValue;
            //card.applyPowers();
        }

        this.baseSecondaryValue = this.secondaryValue = newValue;

        return activate;
    }
}