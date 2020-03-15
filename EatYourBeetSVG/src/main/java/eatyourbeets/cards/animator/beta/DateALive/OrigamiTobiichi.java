package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new OrigamiTobiichiPower(p, magicNumber, upgraded));
    }

    public static class OrigamiTobiichiPower extends AnimatorPower
    {
        private static final int SUPPORT_DAMAGE_AMOUNT = 1;
        private static final int SUPPORT_DAMAGE_LIMIT = 20;
        private final boolean upgraded;

        public OrigamiTobiichiPower(AbstractPlayer owner, int amount, boolean upgraded)
        {
            super(owner, OrigamiTobiichi.DATA);

            this.amount = amount;
            this.upgraded = upgraded;

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
            description = FormatDescription(0, SUPPORT_DAMAGE_AMOUNT * amount, SUPPORT_DAMAGE_LIMIT);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            if (isPlayer)
            {
                flash();

                int stackAmount = player.filledOrbCount() * amount;
                if (stackAmount > 0)
                {
                    GameActions.Bottom.StackPower(new SupportDamagePower(player, stackAmount)).AddCallback(this::InverseOrigamiCheck);
                }
            }
            else
            {
                InverseOrigamiCheck();
            }
        }

        private void InverseOrigamiCheck()
        {
            if (GameUtilities.GetPowerAmount(SupportDamagePower.POWER_ID) >= 20)
            {
                GameActions.Bottom.MakeCardInDrawPile(new InverseOrigami()).SetUpgrade(this.upgraded, false);
                GameActions.Bottom.RemovePower(player, player, this);
            }
        }
    }
}