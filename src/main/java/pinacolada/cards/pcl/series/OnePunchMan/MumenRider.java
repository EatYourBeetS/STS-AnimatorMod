package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;

public class MumenRider extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(MumenRider.class)
            .SetSkill(0, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    private int turns;

    public MumenRider()
    {
        super(DATA);

        Initialize(0, 3, 1, 3);

        SetAffinity_Red(1);
        SetAffinity_Light(1, 0, 1);

        SetRicochet(4, -1, this::OnCooldownCompleted);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity, PCLAffinity.Red, PCLAffinity.Light)
                .SetOptions(false, true);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.MoveCard(this, player.exhaustPile, player.hand)
                .ShowEffect(true, true);
    }
}