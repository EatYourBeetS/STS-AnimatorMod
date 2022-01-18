package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AlinaGray extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(AlinaGray.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public AlinaGray()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Green(1);
        SetAffinity_Blue(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Cycle(name, magicNumber);
        PCLActions.Bottom.EvokeOrb(1).AddCallback(orbs -> {
            for (AbstractOrb o : orbs) {
                PCLActions.Bottom.ChannelOrb(o);
            }
        });

        if (info.IsSynergizing && info.TryActivateSemiLimited()) {
            PCLGameUtilities.AddAffinityRerolls(1);
        }
    }
}