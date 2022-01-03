package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MegurineLuka extends PCLCard
{
    public static final PCLCardData DATA = Register(MegurineLuka.class).SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetMaxCopies(2).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Vocaloid);

    public MegurineLuka()
    {
        super(DATA);

        Initialize(0, 0, 2, 0);
        SetCostUpgrade(-1);
        SetExhaust(true);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    @Override
    public AbstractAttribute GetSecondaryInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final AbstractCard last = PCLGameUtilities.GetLastCardPlayed(true);
        boolean shouldRetain = info.IsSynergizing && last != null && last.cardID.equals(HatsuneMiku.DATA.ID) && CombatStats.TryActivateLimited(cardID);
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.StackPower(new MegurineLukaPower(p, magicNumber, shouldRetain));
    }

    public static class MegurineLukaPower extends PCLPower implements OnSynergyCheckSubscriber
    {
        private boolean shouldRetain;
        public MegurineLukaPower(AbstractPlayer owner, int amount, boolean shouldRetain)
        {
            super(owner, MegurineLuka.DATA);
            this.shouldRetain = shouldRetain;

            Initialize(amount);
        }

        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            if (shouldRetain) {
                shouldRetain = false;
            }
            else {
                RemovePower();
            }
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onSynergyCheck.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onSynergyCheck.Unsubscribe(this);
        }

        @Override
        public boolean OnSynergyCheck(AbstractCard abstractCard, AbstractCard abstractCard1) {
            return abstractCard instanceof PCLCard && enabled;
        }
    }
}