package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.pcl.series.MadokaMagica.IrohaTamaki;
import pinacolada.cards.pcl.series.MadokaMagica.SayakaMiki;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;

public class IrohaTamaki_Giovanna extends PCLCard
{
    public static final PCLCardData DATA = Register(IrohaTamaki_Giovanna.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new IrohaTamaki(), false));

    public IrohaTamaki_Giovanna()
    {
        super(DATA);

        Initialize(0, 2, 10, 1);
        SetUpgrade(0, 0, 1, 0);
        SetPurge(true);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (IsStarter()) {
            PCLActions.Bottom.StackPower(TargetHelper.AllCharacters(), PCLPowerHelper.Shackles, magicNumber);
        }
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.FetchFromPile(name,1,player.exhaustPile).SetFilter(c -> IrohaTamaki.DATA.ID.equals(c.cardID));
    }
}
