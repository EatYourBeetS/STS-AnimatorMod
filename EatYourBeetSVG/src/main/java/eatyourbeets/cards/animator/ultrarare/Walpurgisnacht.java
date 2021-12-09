package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnCooldownTriggeredSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Walpurgisnacht extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Walpurgisnacht.class)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.MadokaMagica);
    private static final int POWER_ENERGY_COST = 7;

    private static final RandomizedList<AnimatorCard> spellcasterPool = new RandomizedList<>();

    public Walpurgisnacht()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyPower(p, p, new WalpurgisnachtPower(p, magicNumber));
    }

    public static class WalpurgisnachtPower extends AnimatorClickablePower implements OnCooldownTriggeredSubscriber
    {
        public WalpurgisnachtPower(AbstractPlayer owner, int amount)
        {
            super(owner, Walpurgisnacht.DATA, PowerTriggerConditionType.Affinity, POWER_ENERGY_COST);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.CreateGriefSeeds(amount).AddCallback(c -> {
                GameActions.Bottom.Motivate(c, 1);
            });
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onCooldownTriggered.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onCooldownTriggered.Unsubscribe(this);
        }


        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount);
        }

        @Override
        public boolean OnCooldownTriggered(AbstractCard card, EYBCardCooldown cooldown) {
            GameActions.Bottom.GainTemporaryHP(amount);
            return true;
        }
    }
}