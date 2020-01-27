package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.SoraAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Sora extends AnimatorCard
{
    public static final String ID = Register(Sora.class, EYBCardBadge.Special);

    public Sora()
    {
        super(ID, 2, CardRarity.RARE, CardType.SKILL, CardTarget.ALL);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetMultiDamage(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.Draw(1)
        .ShuffleIfEmpty(false)
        .SetFilter(c -> Shiro.ID.equals(c.cardID), false);

        GameActions.Bottom.Add(new WaitAction(0.4f));
        GameActions.Bottom.Add(new SoraAction(name, magicNumber));
    }
}