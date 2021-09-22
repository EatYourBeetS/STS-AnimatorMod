package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Fate.MatouShinji;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MatouShinji_CommandSpell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MatouShinji_CommandSpell.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(MatouShinji.DATA.Series);

    public MatouShinji_CommandSpell()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 2);

        SetAffinity_Blue(1);
        SetAffinity_Dark(2);

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
            GameActions.Bottom.Add(new DecreaseMaxOrbAction(1));
            GameActions.Bottom.FetchFromPile(name, 1, p.discardPile)
            .SetOptions(false, true)
            .SetFilter(c ->
            {
                final EYBCardAffinities a = GameUtilities.GetAffinities(c);
                return a != null && (a.GetLevel(Affinity.Red) > 0 || a.GetLevel(Affinity.Orange) > 0);
            })
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.IncreaseScaling(c, Affinity.Star, c.costForTurn + magicNumber);
                    GameActions.Bottom.Motivate(c, 1);
                }
            });
        }
    }
}
