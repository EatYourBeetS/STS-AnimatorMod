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

    public OrigamiTobiichi()
    {
        super(DATA);

        Initialize(0, 0, 3, 9);
        SetUpgrade(0, 0, 0);
        SetAffinity_Water(1, 0, 0);
        SetAffinity_Light(1, 1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
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
        GameActions.Bottom.StackPower(new OrigamiTobiichiPower(p, magicNumber, secondaryValue));
    }

    public static class OrigamiTobiichiPower extends AnimatorPower
    {
        private final int supportDamageLimit;

        public OrigamiTobiichiPower(AbstractPlayer owner, int amount, int limit)
        {
            super(owner, OrigamiTobiichi.DATA);

            this.amount = amount;
            this.supportDamageLimit = limit;

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
            description = FormatDescription(0, amount, supportDamageLimit);
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
            if (GameUtilities.GetPowerAmount(SupportDamagePower.POWER_ID) > (supportDamageLimit))
            {
                GameActions.Bottom.MakeCardInDrawPile(new InverseOrigami()).SetUpgrade(false, false);
                GameActions.Bottom.RemovePower(player, player, this);
            }
        }
    }
}