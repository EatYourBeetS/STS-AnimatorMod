package pinacolada.cards.pcl.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Laby extends PCLCard
{
    public static final PCLCardData DATA = Register(Laby.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Laby()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.StackPower(new LabyPower(p, secondaryValue, magicNumber));
    }

    public static class LabyPower extends PCLPower implements OnTryApplyPowerListener
    {
        protected int secondaryAmount;

        public LabyPower(AbstractCreature owner, int amount, int secondaryAmount)
        {
            super(owner, Laby.DATA);

            this.secondaryAmount = secondaryAmount;
            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            ResetAmount();

            PCLActions.Bottom.ApplyConstricted(TargetHelper.AllCharacters(), secondaryAmount)
            .ShowEffect(false, true);
            SFX.Play(SFX.POWER_CONSTRICTED);
            this.flashWithoutSound();
        }

        @Override
        public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action)
        {
            if (amount > 0) {
                PCLPowerHelper ph = PCLJUtils.Find(PCLGameUtilities.GetPCLCommonDebuffs(), ph2 -> ph2.ID.equals(power.ID));
                int applyAmount = power.amount;
                if (target == owner && ph != null && source != null && source != owner && power.amount > 0)
                {
                    if (VulnerablePower.POWER_ID.equals(ph.ID) || WeakPower.POWER_ID.equals(ph.ID) || FrailPower.POWER_ID.equals(ph.ID)) {
                        applyAmount += 1;
                    }
                    GameActions.Bottom.StackPower(TargetHelper.Normal(source), ph, applyAmount);
                    amount -= 1;
                    flashWithoutSound();
                    return false;
                }
            }

            return true;
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, secondaryAmount);
        }

    }
}