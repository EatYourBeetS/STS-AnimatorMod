package pinacolada.cards.pcl.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SeriousSaitama extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(SeriousSaitama.class)
            .SetSkill(-1, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.OnePunchMan);

    public SeriousSaitama()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1);

        SetPurge(true);
        SetDelayed(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final int x = PCLGameUtilities.UseXCostEnergy(this);
        final int amount = x + magicNumber;

        if (amount > 0)
        {
            PCLActions.Bottom.GainMight(amount);
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), amount);
        }

        if (x > 1)
        {
            for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true))
            {
                PCLActions.Bottom.ApplyPower(p, new StunMonsterPower(enemy, 1));
            }
        }
    }
}