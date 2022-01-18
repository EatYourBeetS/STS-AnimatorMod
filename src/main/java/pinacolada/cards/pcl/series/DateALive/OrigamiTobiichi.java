package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.InverseOrigami;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class OrigamiTobiichi extends PCLCard
{
    public static final PCLCardData DATA = Register(OrigamiTobiichi.class).SetPower(2, CardRarity.RARE).SetMaxCopies(2).SetSeriesFromClassPackage()
            .SetMultiformData(2)
            .PostInitialize(data -> data.AddPreview(new InverseOrigami(), false));
    private static final int THRESHOLD = 10;

    public OrigamiTobiichi()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form == 1);
            SetRetainOnce(form == 1);
            this.cardText.OverrideDescription(form == 1 ? cardData.Strings.DESCRIPTION : null, true);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(THRESHOLD);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainVitality(secondaryValue);
        if (upgraded) {
            PCLActions.Bottom.GainOrbSlots(secondaryValue);
        }
        PCLActions.Bottom.StackPower(new OrigamiTobiichiPower(p, magicNumber));
    }

    public static class OrigamiTobiichiPower extends PCLPower
    {

        public OrigamiTobiichiPower(AbstractPlayer owner, int amount)
        {
            super(owner, OrigamiTobiichi.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, THRESHOLD);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            if (isPlayer)
            {
                flash();

                PCLActions.Bottom.StackPower(new SupportDamagePower(player, amount)).AddCallback(this::InverseOrigamiCheck);
            }
            else
            {
                InverseOrigamiCheck();
            }
        }

        private void InverseOrigamiCheck()
        {
            if (PCLGameUtilities.GetPowerAmount(SupportDamagePower.POWER_ID) >= (THRESHOLD))
            {
                PCLActions.Bottom.MakeCardInDrawPile(new InverseOrigami()).SetUpgrade(false, false);
                PCLActions.Bottom.RemovePower(player, player, this);
            }
        }
    }
}