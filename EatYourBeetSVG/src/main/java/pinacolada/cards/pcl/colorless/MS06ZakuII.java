package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.blights.common.UpgradedHand;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnReloadPreDiscardSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class MS06ZakuII extends PCLCard
{
    public static final PCLCardData DATA = Register(MS06ZakuII.class).SetPower(3, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetMaxCopies(1).SetMultiformData(2).SetSeries(CardSeries.Gundam);
    private static final UpgradedHand blight = new UpgradedHand();
    private static final PCLCardTooltip tooltip = new PCLCardTooltip(blight.name, GR.PCL.Strings.CardMods.HandSize);

    public MS06ZakuII()
    {
        super(DATA);

        Initialize(0, 0, 3, 30);

        SetAffinity_Red(1);
        SetAffinity_Silver(1);

        SetAffinityRequirement(PCLAffinity.General, 12);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(tooltip);
        }
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetRetainOnce(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form == 0);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.StackPower(new MS06ZakuIIPower(p, this.magicNumber));

        if (CheckSpecialCondition(true) && CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                PCLGameUtilities.IncreaseHandSizePermanently(hb.cX, hb.cY);
            });
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return !player.hasBlight(UpgradedHand.ID) && CheckAffinity(PCLAffinity.General) && PCLJUtils.Find(PCLAffinity.Extended(), a -> PCLCombatStats.MatchingSystem.GetPowerAmount((PCLAffinity) a) > secondaryValue) != null;
    }

    public static class MS06ZakuIIPower extends PCLPower implements OnReloadPreDiscardSubscriber
    {
        public MS06ZakuIIPower(AbstractPlayer owner, int amount)
        {
            super(owner, MS06ZakuII.DATA);

            Initialize(amount);
            PCLCombatStats.onReloadPreDiscard.Subscribe(this);
            updateDescription();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onReloadPreDiscard.Unsubscribe(this);
        }


        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public AbstractCard OnReloadPreDiscard(AbstractCard card)
        {
            if (amount > 0)
            {
                this.amount -= 1;
                updateDescription();
                return card.makeSameInstanceOf();
            }
            return null;
        }
    }
}