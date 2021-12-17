package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class LeleiLaLalena extends PCLCard
{
    public static final PCLCardData DATA = Register(LeleiLaLalena.class)
            .SetSkill(0, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public LeleiLaLalena()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);

        SetEvokeOrbCount(1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && HasSynergy())
        {
            PCLGameUtilities.GetPCLIntent(m).AddWeak();
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        target = HasSynergy() ? CardTarget.SELF_AND_ENEMY : CardTarget.SELF;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.IsSynergizing)
        {
            if (m == null)
            {
                m = PCLGameUtilities.GetRandomEnemy(true);
            }

            PCLActions.Bottom.ApplyWeak(p, m, 1);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, 1, !upgraded)
        .ShowEffect(!upgraded, !upgraded)
        .SetOptions(false, false, false)
        .AddCallback(() -> PCLActions.Bottom.ChannelOrbs(Frost::new, magicNumber));
    }
}