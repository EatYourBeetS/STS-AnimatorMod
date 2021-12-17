package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnCooldownTriggeredSubscriber;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class Walpurgisnacht extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(Walpurgisnacht.class)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.MadokaMagica);
    private static final int POWER_ENERGY_COST = 7;

    private static final RandomizedList<PCLCard> spellcasterPool = new RandomizedList<>();

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
        PCLActions.Bottom.ApplyPower(p, p, new WalpurgisnachtPower(p, magicNumber));
    }

    public static class WalpurgisnachtPower extends PCLClickablePower implements OnCooldownTriggeredSubscriber
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
            PCLActions.Bottom.CreateGriefSeeds(amount).AddCallback(c -> {
                PCLActions.Bottom.Motivate(c, 1);
            });
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onCooldownTriggered.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onCooldownTriggered.Unsubscribe(this);
        }


        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount);
        }

        @Override
        public boolean OnCooldownTriggered(AbstractCard card, PCLCardCooldown cooldown) {
            PCLActions.Bottom.GainTemporaryHP(amount);
            return true;
        }
    }
}