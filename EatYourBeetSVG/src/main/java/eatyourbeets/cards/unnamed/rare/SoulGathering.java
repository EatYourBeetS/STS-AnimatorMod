package eatyourbeets.cards.unnamed.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.interfaces.subscribers.OnEnemyDyingSubscriber;
import eatyourbeets.interfaces.subscribers.OnPlayerMinionActionSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.UnnamedClickablePower;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public class SoulGathering extends UnnamedCard
{
    public static final int POWER_COST = 6;
    public static final ArrayList<SoulGatheringEffect> EFFECTS = new ArrayList<>();
    public static final EYBCardData DATA = Register(SoulGathering.class)
            .SetPower(1, CardRarity.RARE)
            .PostInitialize(data ->
            {
                EFFECTS.add(new SoulGatheringEffect_Draw());
                EFFECTS.add(new SoulGatheringEffect_Recover());
                EFFECTS.add(new SoulGatheringEffect_Summon());
                for (SoulGatheringEffect e : EFFECTS)
                {
                    data.AddPreview(e.makeCopy(), false);
                }
            });

    public SoulGathering()
    {
        super(DATA);

        Initialize(0, 0, POWER_COST);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new SoulGatheringPower(p, 1));
    }

    public static class SoulGatheringPower extends UnnamedClickablePower implements OnEnemyDyingSubscriber, OnPlayerMinionActionSubscriber
    {
        private int stacks;

        public SoulGatheringPower(AbstractCreature owner, int amount)
        {
            super(owner, SoulGathering.DATA, PowerTriggerConditionType.Special, POWER_COST);

            stacks = 0;
            triggerCondition.SetCondition(cost -> stacks >= cost).SetPayCost(cost -> stacks -= cost)
                            .SetUses(1, true, false);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onEnemyDying.Subscribe(this);
            CombatStats.onPlayerMinionAction.Subscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, amount);
        }

        @Override
        public void OnMinionDeath(AbstractCreature minion)
        {
            flashWithoutSound();
            stacks += amount;
        }

        @Override
        public void OnEnemyDying(AbstractMonster monster, boolean triggerRelics)
        {
            if (GameUtilities.IsFatal(monster, true))
            {
                flashWithoutSound();
                stacks += amount;
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.SelectFromPile(name, 1, GetEffects())
            .SetOptions(false, false)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    ((SoulGatheringEffect)c).ExecuteEffect();
                }
            });

        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(stacks, Colors.Cream(c.a));
        }

        @Override
        public AbstractPower makeCopy()
        {
            final SoulGatheringPower copy = (SoulGatheringPower)super.makeCopy();
            copy.stacks = stacks;
            return copy;
        }

        protected CardGroup GetEffects()
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (SoulGatheringEffect effect : EFFECTS)
            {
                if (effect.CanExecute())
                {
                    group.group.add(effect.makeCopy());
                }
            }

            return group;
        }
    }

    public static abstract class SoulGatheringEffect extends UnnamedCard implements Hidden
    {
        public SoulGatheringEffect(int index)
        {
            super(DATA);

            type = CardType.SKILL;
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
        public void OnUse(AbstractPlayer p, AbstractMonster m)
        {

        }

        public boolean CanExecute()
        {
            return true;
        }

        public void ExecuteEffect()
        {

        }
    }

    public static class SoulGatheringEffect_Draw extends SoulGatheringEffect
    {
        public SoulGatheringEffect_Draw()
        {
            super(1);

            Initialize(0, 0, 2);
        }

        @Override
        public void ExecuteEffect()
        {
            super.ExecuteEffect();

            GameActions.Bottom.Draw(magicNumber);
            GameActions.Bottom.Motivate();
        }
    }

    public static class SoulGatheringEffect_Recover extends SoulGatheringEffect
    {
        public SoulGatheringEffect_Recover()
        {
            super(2);

            Initialize(0, 6, 9);
        }

        @Override
        public void ExecuteEffect()
        {
            super.ExecuteEffect();

            GameActions.Bottom.GainBlock(block);
            GameActions.Bottom.RecoverHP(magicNumber);
        }
    }

    public static class SoulGatheringEffect_Summon extends SoulGatheringEffect
    {
        public SoulGatheringEffect_Summon()
        {
            super(3);

            Initialize(0, 0);
        }

        @Override
        public void ExecuteEffect()
        {
            super.ExecuteEffect();

            GameActions.Bottom.SummonDoll(1);
            GameActions.Bottom.SelectDoll(name)
            .AutoSelectSingleTarget(true)
            .IsCancellable(false)
            .AddCallback(doll ->
            {
                if (doll != null)
                {
                    GameActions.Bottom.ActivateDoll(doll, 1);
                }
            });
        }
    }
}