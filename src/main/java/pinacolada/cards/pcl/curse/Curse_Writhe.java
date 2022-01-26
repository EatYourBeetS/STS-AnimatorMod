package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.utilities.PCLActions;

public class Curse_Writhe extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Writhe.class)
            .SetCurse(-2, PCLCardTarget.None, false);

    public Curse_Writhe()
    {
        super(DATA, true);

        Initialize(0, 0, 1);

        SetUnplayable(true);
        SetInnate(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.Draw(magicNumber);
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }
}