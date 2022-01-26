package pinacolada.cards.pcl.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.actions.special.CreateRandomGoblins;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Status;
import pinacolada.cards.pcl.series.GoblinSlayer.GoblinSlayer;
import pinacolada.utilities.PCLActions;

public class GoblinChampion extends PCLCard_Status
{
    public static final PCLCardData DATA = Register(GoblinChampion.class)
            .SetStatus(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinChampion()
    {
        super(DATA, true);

        Initialize(0, 0);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            PCLActions.Bottom.Add(new CreateRandomGoblins(1));
        }
    }
}