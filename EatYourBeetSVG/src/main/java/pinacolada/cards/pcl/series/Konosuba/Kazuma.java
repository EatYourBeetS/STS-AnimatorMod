package pinacolada.cards.pcl.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Kazuma extends PCLCard
{
    public static final PCLCardData DATA = Register(Kazuma.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Kazuma()
    {
        super(DATA);

        Initialize(5, 2, 2, 3);
        SetUpgrade(2, 1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1);

        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this,m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        PCLActions.Bottom.GainBlock(block);

        this.baseDamage += magicNumber;

        if (PCLGameUtilities.GetCurrentMatchCombo() >= magicNumber) {
            this.baseBlock += 1;
        };
    }
}