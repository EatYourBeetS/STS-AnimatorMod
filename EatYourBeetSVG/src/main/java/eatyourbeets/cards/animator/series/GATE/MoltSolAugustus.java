package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.MoltSolAugustus_ImperialArchers;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MoltSolAugustus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MoltSolAugustus.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new MoltSolAugustus_ImperialArchers(), false));
    private static final int POWER_AFFINITY_COST = 3;
    private static final int ARCHERS_AMOUNT = 2;

    public MoltSolAugustus()
    {
        super(DATA);

        Initialize(0, 0, 1, ARCHERS_AMOUNT);
        SetUpgrade(0, 6);

        SetAffinity_Red(1);

        SetDelayed(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new MoltSolAugustusPower(p, 1));
    }

    public static class MoltSolAugustusPower extends AnimatorClickablePower implements OnAffinitySealedSubscriber
    {
        public MoltSolAugustusPower(AbstractCreature owner, int amount)
        {
            super(owner, MoltSolAugustus.DATA, PowerTriggerConditionType.Affinity_Red, MoltSolAugustus.POWER_AFFINITY_COST);

            triggerCondition.SetUses(1, true, true);
            canBeZero = true;

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAffinitySealed.Subscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, ARCHERS_AMOUNT, amount);
        }

        @Override
        public void OnAffinitySealed(EYBCard card, boolean manual)
        {
            if (GameUtilities.HasRedAffinity(card))
            {
                CombatStats.Affinities.AddAffinitySealUses(amount);
                GameActions.Bottom.GainDrawEssence(amount);
                flash();
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.SFX(SFX.RELIC_DROP_FLAT);
            GameActions.Bottom.MakeCardInDrawPile(new MoltSolAugustus_ImperialArchers())
            .Repeat(ARCHERS_AMOUNT).SetDuration(0.1f, false);
            GameActions.Bottom.SFX(SFX.ANIMATOR_ARROW);
        }
    }
}