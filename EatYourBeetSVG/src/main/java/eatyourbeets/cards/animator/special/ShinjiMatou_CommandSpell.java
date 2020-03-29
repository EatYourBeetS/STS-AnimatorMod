package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class ShinjiMatou_CommandSpell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShinjiMatou_CommandSpell.class).SetSkill(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public ShinjiMatou_CommandSpell()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetPurge(true);
        SetSynergy(Synergies.Fate);
        SetSpellcaster();
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && player.maxOrbs > 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
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
