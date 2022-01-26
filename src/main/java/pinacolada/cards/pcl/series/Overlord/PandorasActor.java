package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class PandorasActor extends PCLCard
{
    public static final PCLCardData DATA = Register(PandorasActor.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public PandorasActor()
    {
        super(DATA);

        Initialize(0, 3, 4);
        SetUpgrade(0, 2, 2);

        SetAffinity_Star(1, 0, 0);
        SetAffinity_Dark(0,0,1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.MakeCardInHand(this.makeStatEquivalentCopy());
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            PCLGameEffects.List.ShowCopy(this);
            PCLActions.Bottom.GainBlock(magicNumber);
        }
    }
}