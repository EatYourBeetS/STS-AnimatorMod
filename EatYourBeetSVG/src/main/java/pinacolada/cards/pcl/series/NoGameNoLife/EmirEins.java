package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.common.ImpairedPower;
import pinacolada.utilities.PCLActions;

public class EmirEins extends PCLCard
{
    public static final PCLCardData DATA = Register(EmirEins.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public EmirEins()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1);
        SetAffinity_Silver(1);

        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();

        PCLActions.Bottom.Add(new ShuffleAction(player.drawPile));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (player.filledOrbCount() > 0) {
            PCLActions.Bottom.Reload(name, cards -> {
                for (int i = 0; i < magicNumber; i++) {
                    PCLActions.Bottom.InduceOrb(player.orbs.get(0).makeCopy(), true);
                }
                if (cards.size() > 0) {
                    PCLActions.Bottom.StackPower(new ImpairedPower(player, secondaryValue));
                }
            });
        }
    }
}