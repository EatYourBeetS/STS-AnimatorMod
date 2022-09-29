package eatyourbeets.cards.animator.ultrarare;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.SpecialAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.vfx.megacritCopy.LaserBeamEffect2;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.interfaces.subscribers.OnAfterCardExhaustedSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class NivaLada extends AnimatorCard_UltraRare implements OnAfterCardExhaustedSubscriber, OnAfterCardDiscardedSubscriber, OnStartOfTurnSubscriber
{
    public static final EYBCardData DATA = Register(NivaLada.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                final NivaLada card = new NivaLada();
                card.isPreview = true;
                card.OnCooldownCompleted(null);
                data.AddPreview(card, false);
            });

    public NivaLada()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetCooldown(14, 0, this::OnCooldownCompleted);
        SetAttackType(EYBAttackType.Elemental);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return inBattle ? SpecialAttribute.Instance.SetCard(this).SetText(cooldown.GetSecondaryValueString()) : null;
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
        SetInnate(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return type == CardType.SKILL ? null : super.GetDamageInfo();
    }

    @Override
    public void OnAfterCardExhausted(AbstractCard card)
    {
        GameActions.Bottom.Callback(this::OnExhaustOrDiscard);
    }

    @Override
    public void OnAfterCardDiscarded()
    {
        GameActions.Bottom.Callback(this::OnExhaustOrDiscard);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (type == CardType.ATTACK)
        {
            GameActions.Bottom.VFX(new LaserBeamEffect2(player.hb.cX, player.hb.cY), 0.1f);
            GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05f, 0.05f), m.hb.cY + MathUtils.random(-0.05f, 0.05f)), 0.1f);
            GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05f, 0.05f), m.hb.cY + MathUtils.random(-0.05f, 0.05f)), 0.1f);
            GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05f, 0.05f), m.hb.cY + MathUtils.random(-0.05f, 0.05f)), 0.1f);
            GameActions.Bottom.DealDamage(player, m, damage, DamageInfo.DamageType.THORNS, AttackEffects.NONE);
        }
        else
        {
            GameActions.Bottom.Callback(m, (m1, __) -> cooldown.ProgressCooldownAndTrigger(m1));
        }
    }

    @Override
    public void OnStartOfTurn()
    {
        if (type == CardType.ATTACK && player.discardPile.contains(this) && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameEffects.List.ShowCopy(this, Settings.WIDTH * 0.75f, Settings.HEIGHT * 0.4f);
            GameActions.Bottom.Draw(2);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAfterCardDiscarded.Subscribe(this);
        CombatStats.onAfterCardExhausted.Subscribe(this);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        final NivaLada copy = (NivaLada) super.makeStatEquivalentCopy();
        if (type == CardType.ATTACK)
        {
            copy.OnCooldownCompleted(null);
        }

        return copy;
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        if (type != CardType.ATTACK)
        {
            type = CardType.ATTACK;
            cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
            CombatStats.onAfterCardDiscarded.Unsubscribe(this);
            CombatStats.onAfterCardExhausted.Unsubscribe(this);
            GameUtilities.ModifyCostForCombat(this, 2, false);
            GameUtilities.ModifyDamage(this, 300, false);
            SetAttackTarget(EYBCardTarget.Normal);
            SetRetain(true);
            LoadImage("_Alt");
            cropPortrait = false;

            if (!isPreview && !isPopup)
            {
                CombatStats.onStartOfTurn.Subscribe(this);
            }
        }
    }

    protected void OnExhaustOrDiscard()
    {
        cooldown.ProgressCooldown(false);
    }
}