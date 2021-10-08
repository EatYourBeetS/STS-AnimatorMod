package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.blights.animator.UpgradedHand;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnReloadPreDiscardSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class MS06ZakuII extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MS06ZakuII.class).SetPower(3, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetMaxCopies(1).SetSeries(CardSeries.Gundam);
    public static final String[] TEXT = GR.GetUIStrings(GR.Animator.CreateID("CardMods")).TEXT;
    private static final UpgradedHand blight = new UpgradedHand();
    private static final EYBCardTooltip tooltip = new EYBCardTooltip(blight.name, TEXT[0]);

    public MS06ZakuII()
    {
        super(DATA);

        Initialize(0, 0, 3, 12);

        SetAffinity_Red(1);

        SetAffinityRequirement(Affinity.General, 4);
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
        SetRetainOnce(true);
        if (auxiliaryData.form == 0) {
            SetInnate(true);
        }
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

        GameActions.Bottom.StackPower(new MS06ZakuIIPower(p, this.magicNumber));

        if (CheckSpecialCondition(true) && CombatStats.TryActivateLimited(cardID)) {
            GameUtilities.IncreaseHandSizePermanently(hb.cX, hb.cY);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return !player.hasBlight(UpgradedHand.ID) && CheckAffinity(Affinity.General) && JUtils.Find(Affinity.Basic(), a -> CombatStats.Affinities.GetPowerThreshold((Affinity) a) > secondaryValue) != null;
    }

    public static class MS06ZakuIIPower extends AnimatorPower implements OnReloadPreDiscardSubscriber
    {
        public MS06ZakuIIPower(AbstractPlayer owner, int amount)
        {
            super(owner, MS06ZakuII.DATA);

            Initialize(amount);
            CombatStats.onReloadPreDiscard.Subscribe(this);
            updateDescription();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onReloadPreDiscard.Unsubscribe(this);
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