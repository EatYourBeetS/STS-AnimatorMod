package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.player.GainGold;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnEnemyDyingSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GuildGirl extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GuildGirl.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int GOLD_GAIN = 4;

    public GuildGirl()
    {
        super(DATA);

        Initialize(0, 0, GOLD_GAIN);

        SetAffinity_Green(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new GuildGirlPower(p, 1));
    }

    public static class GuildGirlPower extends AnimatorPower implements OnEnemyDyingSubscriber
    {
        public GuildGirlPower(AbstractCreature owner, int amount)
        {
            super(owner, GuildGirl.DATA);

            this.canBeZero = true;

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, GOLD_GAIN);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onEnemyDying.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onEnemyDying.Unsubscribe(this);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ResetAmount();
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (amount > 0 && GameUtilities.IsSealed(usedCard))
            {
                GameActions.Bottom.Cycle(name, 1);
                reducePower(1);
                flashWithoutSound();
            }
        }

        @Override
        public void OnEnemyDying(AbstractMonster monster, boolean triggerRelics)
        {
            if (GameUtilities.IsFatal(monster, false))
            {
                GameActions.Top.Add(new GainGold(GOLD_GAIN, true));
                this.flash();
            }
        }
    }
}