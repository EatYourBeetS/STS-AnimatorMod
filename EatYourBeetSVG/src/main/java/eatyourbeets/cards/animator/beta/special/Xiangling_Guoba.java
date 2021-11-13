package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Xiangling_Guoba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Xiangling_Guoba.class).SetPower(0, CardRarity.SPECIAL).SetSeries(CardSeries.GenshinImpact);
    public static final int BURNING_BONUS = 5;

    public Xiangling_Guoba()
    {
        super(DATA);

        Initialize(0, 0, 2, 5);
        SetUpgrade(0,0,1,0);
        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new GuobaPower(p, this.magicNumber));
    }

    public static class GuobaPower extends AnimatorPower
    {

        public GuobaPower(AbstractPlayer owner, int amount)
        {
            super(owner, Xiangling_Guoba.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), amount);

            ReducePower(1);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            GameActions.Bottom.Callback(() -> BurningPower.AddPlayerAttackBonus(BURNING_BONUS));
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, BURNING_BONUS);
        }
    }
}