package eatyourbeets.actions.unnamed;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.DynamicCardBuilder;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.markers.DontCopy;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.combat.UnnamedDollSlot;
import eatyourbeets.utilities.*;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

public class SummonDoll extends EYBActionWithCallback<ArrayList<UnnamedDoll>>
{
    protected final ArrayList<UnnamedDoll> result = new ArrayList<>();
    protected boolean sameType;
    protected Vector2f position;
    protected UnnamedDollSlot slot;
    protected UnnamedDoll original;

    public SummonDoll(int amount)
    {
        super(ActionType.SPECIAL, 0.5f);

        Initialize(amount);
    }

    public SummonDoll CloneFrom(UnnamedDoll doll)
    {
        this.original = doll;
        this.sameType = doll != null;

        return this;
    }

    public SummonDoll SameType(boolean sameType)
    {
        this.sameType = sameType;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        TrySummon(original == null ? null : new GenericEffect_Clone(this, original));
    }

    protected void TrySummon(GenericEffect_SummonDoll preselected)
    {
        if (!CombatStats.Dolls.CanSummon(true))
        {
            Complete(result);
            return;
        }

        slot = CombatStats.Dolls.GetAvailableSlot();

        if (slot == null)
        {
            CombatStats.Dolls.OverSummon(amount);
            Complete(result);
            return;
        }

        if (preselected != null)
        {
            preselected.summonData.Slot = slot.Index;
            preselected.summonData.Position = slot.GetPosition();

            final UnnamedDoll doll = CombatStats.Dolls.Summon(preselected.summonData);
            if (original != null)
            {
                doll.SwitchIntent(original.intent);
                doll.maxHealth = original.maxHealth;
                doll.currentHealth = original.currentHealth;
                doll.powers.clear();
                for (AbstractPower p : original.powers)
                {
                    final CloneablePowerInterface cloneablePower = JUtils.SafeCast(p, CloneablePowerInterface.class);
                    if (cloneablePower != null && !(p instanceof DontCopy))
                    {
                        final AbstractPower power = cloneablePower.makeCopy();
                        if (power != null)
                        {
                            doll.powers.add(power);
                            power.amount = p.amount;
                            power.owner = doll;
                            power.onInitialApplication();
                        }
                    }
                }
            }

            result.add(doll);

            if (amount > 1)
            {
                amount -= 1;
                TrySummon(sameType ? preselected : null);
            }
            else
            {
                Complete(result);
            }
        }
        else
        {
            final CardEffectChoice choices = new CardEffectChoice();
            choices.Initialize(new eatyourbeets.cards.unnamed.basic.SummonDoll(), true);
            choices.AddEffect(new GenericEffect_Defensive(this));
            choices.AddEffect(new GenericEffect_Balanced(this));
            choices.AddEffect(new GenericEffect_Offensive(this));
            choices.Select(GameActions.Instant, 1, null);
        }
    }

    @Override
    protected void Complete(ArrayList<UnnamedDoll> result)
    {
        if (result != null && result.size() > 0)
        {
            SFX.Play(SFX.ATTACK_FIRE, 0.35f, 0.35f, 0.85f);

            for (UnnamedDoll doll : result)
            {
                GameEffects.Queue.Add(VFX.Hemokinesis(doll.hb, player.hb).SetScale(0.5f).SetFrequency(0.1f).SetColor(Color.VIOLET));
                GameEffects.Queue.WaitRealtime(0.2f)
                .AddCallback(doll, (d, __) ->
                {
                    d.Visible = true;
                    d.tint.changeColor(d.tint.color.cpy(), 2f);
                    d.tint.color = Colors.Violet(0.3f).cpy();
                    d.showHealthBar();
                    d.healthBarUpdatedEvent();
                });
            }
        }

        super.Complete(result);
    }

    protected static abstract class GenericEffect_SummonDoll extends GenericEffect
    {
        protected SummonDoll action;
        protected UnnamedDoll.SummonData summonData;

        protected GenericEffect_SummonDoll(SummonDoll action, int hp, int strength, int dexterity, UnnamedDoll.StartingIntent intent)
        {
            this.action = action;
            this.summonData = new UnnamedDoll.SummonData();
            this.summonData.Health = hp;
            this.summonData.Strength = strength;
            this.summonData.Dexterity = dexterity;
            this.summonData.Intent = intent;
        }

        @Override
        public String GetText()
        {
            return JUtils.Format(eatyourbeets.cards.unnamed.basic.SummonDoll.DATA.Strings.EXTENDED_DESCRIPTION[0],
            summonData.Strength, summonData.Dexterity,
            summonData.Intent == UnnamedDoll.StartingIntent.Attack ? GR.Common.Strings.Terms.Attack :
            summonData.Intent == UnnamedDoll.StartingIntent.Defend ? GR.Common.Strings.Terms.Defend : GR.Common.Strings.Terms.Random);
        }

        @Override
        public void OnCreateBuilder(DynamicCardBuilder builder)
        {
            builder.SetNumbers(0, 0, summonData.Health, 0);
            builder.SetSpecialInfo(c -> HPAttribute.Instance.SetCard(c, true));
        }

        @Override
        public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
        {
            action.TrySummon(this);
        }
    }

    protected static class GenericEffect_Defensive extends GenericEffect_SummonDoll
    {
        protected GenericEffect_Defensive(SummonDoll action)
        {
            super(action, 10, 1, 2, UnnamedDoll.StartingIntent.Defend);
        }
    }

    protected static class GenericEffect_Balanced extends GenericEffect_SummonDoll
    {
        protected GenericEffect_Balanced(SummonDoll action)
        {
            super(action, 7, 3, 2, UnnamedDoll.StartingIntent.Random);
        }
    }

    protected static class GenericEffect_Offensive extends GenericEffect_SummonDoll
    {
        protected GenericEffect_Offensive(SummonDoll action)
        {
            super(action, 5, 5, 1, UnnamedDoll.StartingIntent.Attack);
        }
    }

    protected static class GenericEffect_Clone extends GenericEffect_SummonDoll
    {
        protected GenericEffect_Clone(SummonDoll action, UnnamedDoll source)
        {
            super(action, source.maxHealth,
                    GameUtilities.GetPowerAmount(source, StrengthPower.POWER_ID),
                    GameUtilities.GetPowerAmount(source, DexterityPower.POWER_ID),
                    GameUtilities.IsAttacking(source.intent) ? UnnamedDoll.StartingIntent.Attack : UnnamedDoll.StartingIntent.Defend);
        }
    }
}
