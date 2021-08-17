package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class NobleFencer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NobleFencer.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public NobleFencer()
    {
        super(DATA);

        Initialize(0, 2);
        SetUpgrade(0, 3);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            GameActions.Bottom.GainAgility(1);
            GameActions.Bottom.GainIntellect(1);
        }

        GameActions.Bottom.EvokeOrb(1).SetFilter(Lightning.class::isInstance);
        GameActions.Bottom.StackPower(new NobleFencerPower(p, 1));
    }

    public class NobleFencerPower extends AnimatorPower implements OnSynergySubscriber
    {
        public NobleFencerPower(AbstractCreature owner, int amount)
        {
            super(owner, NobleFencer.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        public void OnSynergy(AbstractCard card)
        {
            GameActions.Bottom.ChannelOrbs(Lightning::new, amount);

            this.flash();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}