package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.pcl.series.NoGameNoLife.Sora;
import pinacolada.utilities.PCLActions;

public class Sora_Strategy2 extends PCLCard
{
    public static final PCLCardData DATA = Register(Sora_Strategy2.class)
            .SetImagePath(Sora_Strategy1.DATA.ImagePath)
            .SetSkill(1, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetSeries(Sora.DATA.Series);

    public Sora_Strategy2()
    {
        super(DATA);

        Initialize(0, 20, 5);
        SetUpgrade(0,5,0);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void UpdateBlock(float amount)
    {
        super.UpdateBlock(baseBlock);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Exhaust(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }
}