package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.series.Fate.MatouShinji;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MatouShinji_CommandSpell extends PCLCard
{
    public static final PCLCardData DATA = Register(MatouShinji_CommandSpell.class)
            .SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeries(MatouShinji.DATA.Series);

    public MatouShinji_CommandSpell()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1);

        SetRetain(true);
        SetPurge(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(player.orbs.isEmpty());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.maxOrbs > 0)
        {
            PCLActions.Bottom.Add(new DecreaseMaxOrbAction(1));
            PCLActions.Bottom.FetchFromPile(name, 1, p.discardPile)
            .SetOptions(false, true)
            .SetFilter(c ->
            {
                final PCLCardAffinities a = PCLGameUtilities.GetPCLAffinities(c);
                return a != null && (a.GetLevel(PCLAffinity.Red) > 0 || a.GetLevel(PCLAffinity.Orange) > 0);
            })
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    PCLActions.Bottom.IncreaseScaling(c, PCLAffinity.Star, c.costForTurn + magicNumber);
                    PCLActions.Bottom.Motivate(c, 1);
                }
            });
        }
    }
}
