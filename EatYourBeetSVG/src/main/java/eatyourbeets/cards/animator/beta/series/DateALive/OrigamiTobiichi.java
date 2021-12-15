package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.InverseOrigami;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrigamiTobiichi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrigamiTobiichi.class).SetPower(2, CardRarity.RARE).SetMaxCopies(2).SetSeriesFromClassPackage()
            .SetMultiformData(2)
            .PostInitialize(data -> data.AddPreview(new InverseOrigami(), false));
    private static final int THRESHOLD = 10;

    public OrigamiTobiichi()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(2, 0, 0);
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
    };

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(THRESHOLD);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainVitality(secondaryValue);
        if (upgraded) {
            GameActions.Bottom.GainOrbSlots(secondaryValue);
        }
        GameActions.Bottom.StackPower(new OrigamiTobiichiPower(p, magicNumber));
    }

    public static class OrigamiTobiichiPower extends AnimatorPower
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

                GameActions.Bottom.StackPower(new SupportDamagePower(player, amount)).AddCallback(this::InverseOrigamiCheck);
            }
            else
            {
                InverseOrigamiCheck();
            }
        }

        private void InverseOrigamiCheck()
        {
            if (GameUtilities.GetPowerAmount(SupportDamagePower.POWER_ID) >= (THRESHOLD))
            {
                GameActions.Bottom.MakeCardInDrawPile(new InverseOrigami()).SetUpgrade(false, false);
                GameActions.Bottom.RemovePower(player, player, this);
            }
        }
    }
}