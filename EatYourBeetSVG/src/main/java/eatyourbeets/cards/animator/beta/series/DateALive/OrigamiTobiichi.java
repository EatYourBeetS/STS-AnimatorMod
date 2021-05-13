package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.InverseOrigami;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrigamiTobiichi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrigamiTobiichi.class).SetPower(2, CardRarity.UNCOMMON);
    static
    {
        DATA.AddPreview(new InverseOrigami(), false);
    }

    public OrigamiTobiichi()
    {
        super(DATA);

        Initialize(0, 3, 1, 10);
        SetUpgrade(0, 4, 0);

        SetSpellcaster();
        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new OrigamiTobiichiPower(p, magicNumber, secondaryValue));
    }

    public static class OrigamiTobiichiPower extends AnimatorPower
    {
        public static final int SUPPORT_DAMAGE_AMOUNT = 1;

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
            description = FormatDescription(0, SUPPORT_DAMAGE_AMOUNT * amount, supportDamageLimit);
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
                for (int i = 0; i < amount; i++)
                {
                    GameActions.Bottom.MakeCardInDrawPile(new InverseOrigami()).SetUpgrade(false, false);
                }

                GameActions.Bottom.RemovePower(player, player, this);
            }
        }
    }
}