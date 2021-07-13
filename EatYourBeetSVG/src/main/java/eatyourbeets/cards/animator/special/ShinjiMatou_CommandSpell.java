package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Fate.ShinjiMatou;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class ShinjiMatou_CommandSpell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShinjiMatou_CommandSpell.class)
            .SetSkill(1, CardRarity.SPECIAL)
            .SetSeries(ShinjiMatou.DATA.Series);

    public ShinjiMatou_CommandSpell()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

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
            GameActions.Bottom.PlayFromPile(name, 1, m, p.discardPile)
            .SetOptions(false, false)
            .SetFilter(this::HasSynergy);
        }
    }
}
