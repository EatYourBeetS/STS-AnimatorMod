package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.orbs.RemoveOrb;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Kreideprinz extends PCLCard
{
    public static final PCLCardData DATA = Register(Kreideprinz.class).SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.Normal).SetSeriesFromClassPackage(true);

    public Kreideprinz()
    {
        super(DATA);

        Initialize(0, 2, 4, 2);
        SetUpgrade(0, 3, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainEnergy(secondaryValue);

        boolean hadFrost = false;
        boolean hadEarth = false;
        boolean hadDark = false;
        for (int i = 0; i < player.filledOrbCount(); i++) {
            AbstractOrb o = player.orbs.get(i);
            if (PCLGameUtilities.IsValidOrb(o)) {
                PCLActions.Bottom.Add(new RemoveOrb(o));
                PCLActions.Bottom.ChannelOrb(new Earth());
            }
            hadFrost = hadFrost | Frost.ORB_ID.equals(o.ID);
            hadEarth = hadEarth | Earth.ORB_ID.equals(o.ID);
            hadDark = hadDark | Dark.ORB_ID.equals(o.ID);
        }

        if (hadFrost) {
            PCLActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), magicNumber);
        }
        if (hadEarth) {
            PCLActions.Bottom.GainInspiration(1);
        }
        if (hadDark) {
            PCLActions.Bottom.CreateGriefSeeds(secondaryValue);
        }
    }
}