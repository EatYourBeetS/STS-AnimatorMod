package eatyourbeets.cards.unnamed.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.interfaces.subscribers.OnPlayerMinionActionSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.utilities.GameActions;

public class SummoningArts extends UnnamedCard
{
    public static final int MAX_HP_PER_STACK = 2;
    public static final EYBCardData DATA = Register(SummoningArts.class)
            .SetPower(2, CardRarity.RARE);

    public SummoningArts()
    {
        super(DATA);

        Initialize(0, 0, 1, MAX_HP_PER_STACK);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new SummoningArtsPower(p, 1));
    }

    public static class SummoningArtsPower extends UnnamedPower implements OnPlayerMinionActionSubscriber
    {
        public SummoningArtsPower(AbstractCreature owner, int amount)
        {
            super(owner, SummoningArts.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, amount * MAX_HP_PER_STACK);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onPlayerMinionAction.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onPlayerMinionAction.Unsubscribe(this);
        }

        @Override
        public void OnMinionSummon(AbstractCreature minion)
        {
            GameActions.Bottom.GainStrength(owner, minion, amount);
            GameActions.Bottom.GainDexterity(owner, minion, amount);
            flashWithoutSound();
        }

        @Override
        public void OnMinionIntentChanged(AbstractCreature minion, AbstractMonster.Intent previous, AbstractMonster.Intent current)
        {
            minion.increaseMaxHp(amount * MAX_HP_PER_STACK, true);
            flashWithoutSound();
        }
    }
}