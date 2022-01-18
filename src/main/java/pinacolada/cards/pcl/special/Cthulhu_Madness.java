package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.actions.unique.MadnessAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.markers.Hidden;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.ultrarare.Cthulhu;
import pinacolada.utilities.PCLActions;

public class Cthulhu_Madness extends PCLCard implements Hidden
{
    public static final PCLCardData DATA = Register(Cthulhu_Madness.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(Cthulhu.DATA.Series);

    public Cthulhu_Madness()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetAffinity_Star(1);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Add(new MadnessAction());
    }
}