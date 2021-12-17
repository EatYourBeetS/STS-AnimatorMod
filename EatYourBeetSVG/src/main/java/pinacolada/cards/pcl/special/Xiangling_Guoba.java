package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLTriggerablePower;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;

public class Xiangling_Guoba extends PCLCard
{
    public static final PCLCardData DATA = Register(Xiangling_Guoba.class).SetPower(0, CardRarity.SPECIAL).SetSeries(CardSeries.GenshinImpact);
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
        PCLActions.Bottom.StackPower(new GuobaPower(p, this.magicNumber));
    }

    public static class GuobaPower extends PCLPower
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

            PCLActions.Bottom.ApplyBurning(TargetHelper.Enemies(), amount);

            ReducePower(1);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLActions.Bottom.Callback(() -> PCLTriggerablePower.AddEffectBonus(BurningPower.POWER_ID, BURNING_BONUS));
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, BURNING_BONUS);
        }
    }
}