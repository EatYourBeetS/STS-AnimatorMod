package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import eatyourbeets.actions.cardManipulation.RandomCostIncrease;
import eatyourbeets.cards.animator.status.Status_Void;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.stances.CorruptionStance;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public class ArthurGaz extends AnimatorCard
{
    public static final ArrayList<ArthurGazEffect> EFFECTS = new ArrayList<>();
    public static final EYBCardData DATA = Register(ArthurGaz.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                EFFECTS.add(new ArthurGazEffect_AffinityPowers());
                EFFECTS.add(new ArthurGazEffect_Metallicize());
                EFFECTS.add(new ArthurGazEffect_DemonStance());
                EFFECTS.add(new ArthurGazEffect_Energy());
                EFFECTS.add(new ArthurGazEffect_DrawCards());
                for (ArthurGazEffect e : EFFECTS)
                {
                    data.AddPreview(e.makeCopy(), false);
                }
            });

    private int tempHP;

    public ArthurGaz()
    {
        super(DATA);

        Initialize(0, 0, 4, 6);
        SetUpgrade(0, 0, 0, -2);

        SetAffinity_Blue(1);
        SetAffinity_Dark(2);

        SetPurge(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return tempHP > 0 ? TempHPAttribute.Instance.SetCard(this, tempHP) : null;
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        tempHP = GetTempHP();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if ((tempHP = GetTempHP()) > 0)
        {
            GameActions.Bottom.GainTemporaryHP(tempHP);
        }
        if (p.currentHealth > 0)
        {
            GameActions.Bottom.LoseHPUntilEndOfCombat(p.currentHealth, AttackEffects.DARK);
        }

        GameActions.Bottom.StackPower(new ArthurGazPower(p, magicNumber));
    }

    private int GetTempHP()
    {
        return (player.currentHealth - 1) / secondaryValue;
    }

    public static class ArthurGazPower extends AnimatorPower
    {
        public ArthurGazPower(AbstractCreature owner, int amount)
        {
            super(owner, ArthurGaz.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.SelectFromPile(name, 1, GetEffects())
            .SetOptions(false, false)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    ((ArthurGazEffect)c).ExecuteEffect();
                }
            });

            ReducePower(1);
            flashWithoutSound();
        }

        protected CardGroup GetEffects()
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final RandomizedList<ArthurGazEffect> effects = new RandomizedList<>();
            for (ArthurGazEffect effect : EFFECTS)
            {
                if (effect.CanExecute())
                {
                    effects.Add(effect);
                }
            }

            final int max = Mathf.Min(3, effects.Size());
            for (int i = 0; i < max; i++)
            {
                group.group.add(effects.Retrieve(rng).makeCopy());
            }

            return group;
        }
    }

    public static abstract class ArthurGazEffect extends AnimatorCard implements Hidden
    {
        public static final int TEMP_HP = 3;

        public ArthurGazEffect(int index)
        {
            super(DATA);

            cardText.OverrideDescription(DATA.Strings.EXTENDED_DESCRIPTION[index], true);
            cost = costForTurn = -2;
        }

        @Override
        public boolean canUpgrade()
        {
            return false;
        }

        @Override
        public void upgrade()
        {

        }

        @Override
        public AbstractCard makeCopy()
        {
            return JUtils.CallDefaultConstructor(getClass());
        }

        @Override
        public AbstractAttribute GetSpecialInfo()
        {
            return TempHPAttribute.Instance.SetCard(this, TEMP_HP);
        }

        public boolean CanExecute()
        {
            return true;
        }

        public void ExecuteEffect()
        {
            GameActions.Bottom.GainTemporaryHP(TEMP_HP);
        }
    }

    public static class ArthurGazEffect_DemonStance extends ArthurGazEffect
    {
        public ArthurGazEffect_DemonStance()
        {
            super(1);
        }

        @Override
        public boolean CanExecute()
        {
            return !GameUtilities.InStance(CorruptionStance.STANCE_ID);
        }

        @Override
        public void ExecuteEffect()
        {
            super.ExecuteEffect();

            GameActions.Bottom.ChangeStance(CorruptionStance.STANCE_ID);
            GameActions.Bottom.MakeCardInDiscardPile(new Status_Void(true));
        }
    }

    public static class ArthurGazEffect_AffinityPowers extends ArthurGazEffect
    {
        public ArthurGazEffect_AffinityPowers()
        {
            super(2);

            Initialize(0, 0, 7, 2);
        }

        @Override
        public void ExecuteEffect()
        {
            super.ExecuteEffect();

            GameActions.Bottom.GainBlock(magicNumber);
            for (Affinity affinity : Affinity.Basic())
            {
                GameActions.Bottom.GainAffinity(affinity, secondaryValue, false);
            }
            GameActions.Bottom.DiscardFromHand(name, 1, true)
            .SetFilter(c -> c.type == CardType.ATTACK);
        }
    }

    public static class ArthurGazEffect_DrawCards extends ArthurGazEffect
    {
        public ArthurGazEffect_DrawCards()
        {
            super(3);

            Initialize(0, 0, 3, 2);
        }

        @Override
        public boolean CanExecute()
        {
            return player.hand.size() <= (BaseMod.MAX_HAND_SIZE - magicNumber);
        }

        @Override
        public void ExecuteEffect()
        {
            super.ExecuteEffect();

            GameActions.Bottom.Draw(magicNumber);
            GameActions.Bottom.ReshuffleFromHand(name, secondaryValue, false);
        }
    }

    public static class ArthurGazEffect_Metallicize extends ArthurGazEffect
    {
        public ArthurGazEffect_Metallicize()
        {
            super(4);

            Initialize(0, 0, 4);
        }

        @Override
        public boolean CanExecute()
        {
            return GameUtilities.GetPowerAmount(MetallicizePower.POWER_ID) <= 12;
        }

        @Override
        public void ExecuteEffect()
        {
            super.ExecuteEffect();

            GameActions.Bottom.GainMetallicize(magicNumber)
            .AddCallback(power ->
            {
                if (power != null)
                {
                    GameActions.Bottom.TakeDamageAtEndOfTurn(power.amount);
                }
            });
        }
    }

    public static class ArthurGazEffect_Energy extends ArthurGazEffect
    {
        public ArthurGazEffect_Energy()
        {
            super(5);

            Initialize(0, 0, 2);
        }

        @Override
        public void ExecuteEffect()
        {
            super.ExecuteEffect();

            GameActions.Bottom.GainEnergy(magicNumber);
            GameActions.Bottom.Add(new RandomCostIncrease(1, false));
        }
    }
}