package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;

public class Curse_Clumsy extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Clumsy.class)
            .SetCurse(-2, PCLCardTarget.None, false);

    public Curse_Clumsy()
    {
        super(DATA, false);

        Initialize(0, 0, 1);

        SetUnplayable(true);
        SetEthereal(true);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }
}