package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class MatouShinji_CommandSpell extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(MatouShinji_CommandSpell.class).SetSkill(1, CardRarity.SPECIAL);

    public MatouShinji_CommandSpell()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetPurge(true);
        this.series = CardSeries.Fate;
        SetSpellcaster();
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && player.maxOrbs > 0;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.maxOrbs > 0)
        {
            GameActions.Bottom.Add(new DecreaseMaxOrbAction(1));
            GameActions.Bottom.PlayFromPile(name, 1, m, p.discardPile)
            .SetOptions(false, false)
            .SetFilter(this, CardSeries.Synergy::WouldSynergize);
        }
    }
}
