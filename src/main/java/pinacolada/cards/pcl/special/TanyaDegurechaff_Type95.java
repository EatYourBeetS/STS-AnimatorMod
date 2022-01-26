package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.colorless.TanyaDegurechaff;
import pinacolada.utilities.PCLActions;

public class TanyaDegurechaff_Type95 extends PCLCard
{
    public static final PCLCardData DATA = Register(TanyaDegurechaff_Type95.class)
            .SetSkill(2, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(TanyaDegurechaff.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new OrbCore_Plasma(), false));

    public TanyaDegurechaff_Type95()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Silver(1);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Light, 3);
        SetAffinityRequirement(PCLAffinity.Blue, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (TrySpendAffinity(PCLAffinity.Light) && TrySpendAffinity(PCLAffinity.Blue))
        {
            PCLActions.Bottom.PlayCard(new OrbCore_Plasma(), m);
        }
        else
        {
            PCLActions.Bottom.ChannelOrb(new Plasma());
        }
    }
}