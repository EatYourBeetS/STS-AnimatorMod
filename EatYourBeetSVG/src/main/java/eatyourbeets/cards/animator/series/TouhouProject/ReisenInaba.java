package eatyourbeets.cards.animator.series.TouhouProject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_Auto;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.utility.CallbackEffect;
import eatyourbeets.effects.utility.SequentialEffect;
import eatyourbeets.effects.vfx.megacritCopy.DivinityParticleEffect2;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ReisenInaba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ReisenInaba.class)
            .SetSkill(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .ObtainableAsReward((data, deck) -> deck.size() >= (12 + (10 * data.GetTotalCopies(deck))));

    public ReisenInaba()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (m.hasPower(ArtifactPower.POWER_ID))
        {
            GameUtilities.UseArtifact(m);
        }
        else
        {
            final CardEffectChoice choices = new CardEffectChoice();
            choices.Initialize(this);
            choices.AddEffect(new GenericEffect_Vulnerable(this)).SetNumbers(this);
            choices.AddEffect(new GenericEffect_Weak(this)).SetNumbers(this);
            choices.Select(1, m);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.IsSynergizing)
        {
            GameActions.Bottom.ExhaustFromHand(name, 1, false)
            .SetOptions(false, false, false);
        }
        else
        {
            GameActions.Bottom.DiscardFromHand(name, 1, false)
            .SetOptions(false, false, false);
        }
    }

    public static void PlayDebuffVFX(AbstractCreature target)
    {
        GameEffects.TopLevelQueue.BorderLongFlash(Color.RED);
        for (int i = 0; i < 16; i++)
        {
            final SequentialEffect effect = new SequentialEffect();
            effect.Enqueue(new CallbackEffect(new WaitRealtimeAction(MathUtils.random(0f, 1f))));
            effect.Enqueue(new DivinityParticleEffect2(target).SetColor(0.8f, 1f, 0.2f, 0.3f, 0.2f, 0.3f));
//            effect.Enqueue(new CallbackEffect(new WaitRealtimeAction(MathUtils.random(0f, 0.3f))));
//            effect.Enqueue(new DivinityParticleEffect2(target).SetColor(0.8f, 1f, 0.2f, 0.3f, 0.2f, 0.3f));
            GameEffects.TopLevelQueue.Add(effect);
        }

        SFX.Play(SFX.DEBUFF_2, 0.3f, 0.4f);
    }

    protected static class GenericEffect_Vulnerable extends GenericEffect_Auto
    {
        public GenericEffect_Vulnerable(EYBCard source)
        {
            super(source, 0);
        }

        @Override
        public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
        {
            PlayDebuffVFX(m);
            final AbstractPower toApply = new StrengthPower(m, card.secondaryValue);
            GameActions.Top.ApplyVulnerable(p, m, card.magicNumber)
            .AddCallback(toApply, (buff, debuff) ->
            {
                if (debuff != null)
                {
                    GameActions.Top.StackPower(player, buff);
                }
            });
        }
    }

    protected static class GenericEffect_Weak extends GenericEffect_Auto
    {
        public GenericEffect_Weak(EYBCard source)
        {
            super(source, 1);
        }

        @Override
        public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
        {
            PlayDebuffVFX(m);
            final AbstractPower toApply = new MalleablePower(m, card.secondaryValue);
            GameActions.Top.ApplyWeak(p, m, card.magicNumber)
            .AddCallback(toApply, (buff, debuff) ->
            {
                if (debuff != null)
                {
                    GameActions.Top.StackPower(player, buff);
                }
            });
        }
    }
}