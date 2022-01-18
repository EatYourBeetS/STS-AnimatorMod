package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Konayuki extends PCLCard
{
    public static final PCLCardData DATA = Register(Konayuki.class)
            .SetSkill(2, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final int THRESHOLD = 3;

    public Konayuki()
    {
        super(DATA);

        Initialize(0, 9, 1, 3);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Red(1, 0, 2);
    }

    @Override
    protected float GetInitialBlock()
    {
        int bonus = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (c.uuid != uuid && PCLGameUtilities.IsHighCost(c))
            {
                bonus += secondaryValue;
            }
        }

        return super.GetInitialBlock() + bonus;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainMight(magicNumber);

        if (CheckSpecialCondition(true))
        {
            PCLActions.Bottom.GainEnergy(1);
            this.exhaustOnUseOnce = true;
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return PCLGameUtilities.GetPowerAmount(PCLAffinity.Red) >= THRESHOLD;
    }
}