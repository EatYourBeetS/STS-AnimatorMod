package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Fate.ShinjiMatou;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ShinjiMatou_CommandSpell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShinjiMatou_CommandSpell.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(ShinjiMatou.DATA.Series);

    public ShinjiMatou_CommandSpell()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(2);

        SetPurge(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && player.maxOrbs > 0;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (p.maxOrbs > 0)
        {
            GameActions.Bottom.Add(new DecreaseMaxOrbAction(1));
            GameActions.Bottom.FetchFromPile(name, 1, p.discardPile)
            .SetOptions(false, true)
            .SetFilter(c ->
            {
                final EYBCardAffinities a = GameUtilities.GetAffinities(c);
                return a != null && (a.GetLevel(Affinity.Red) > 0 || a.GetLevel(Affinity.Green) > 0);
            })
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.Motivate(c, 1);
                    GameActions.Bottom.IncreaseExistingScaling(c, magicNumber);
                }
            });
        }
    }
}
