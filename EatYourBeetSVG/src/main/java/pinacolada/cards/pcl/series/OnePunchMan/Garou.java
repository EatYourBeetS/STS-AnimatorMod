package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Garou extends PCLCard
{
    public static final PCLCardData DATA = Register(Garou.class)
            .SetSkill(1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Garou()
    {
        super(DATA);

        Initialize(0, 1, 6);
        SetUpgrade(0, 0, 2);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(player.drawPile.size() < 3);
        PCLGameUtilities.ModifySecondaryValue(this, PCLJUtils.Count(CombatStats.CardsExhaustedThisTurn(), PCLGameUtilities::HasLightAffinity), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        if (p.drawPile.size() >= 3)
        {
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryStrength, magicNumber);
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryDexterity, magicNumber);
            PCLActions.Bottom.MoveCards(p.drawPile, p.exhaustPile, 3)
            .ShowEffect(true, true)
            .SetOrigin(CardSelection.Top);
            PCLActions.Last.Callback(() ->
            {
                final int light = PCLJUtils.Count(CombatStats.CardsExhaustedThisTurn(), PCLGameUtilities::HasLightAffinity);
                if (light > 0)
                {
                    PCLActions.Bottom.GainMight(light);
                }
            });
        }
    }
}