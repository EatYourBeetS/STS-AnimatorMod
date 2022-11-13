package eatyourbeets.cards.animatorClassic.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animatorClassic.SoraAction;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Sora extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Sora.class).SetSeriesFromClassPackage().SetSkill(2, CardRarity.RARE, EYBCardTarget.ALL);
    static
    {
        DATA.AddPreview(new Shiro(), true);
    }

    public Sora()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetMultiDamage(true);

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(new WaitAction(0.4f));
        GameActions.Bottom.Add(new SoraAction(name, magicNumber));
        GameActions.Bottom.Draw(1)
        .ShuffleIfEmpty(false)
        .SetFilter(c -> Shiro.DATA.ID.equals(c.cardID), false);

    }
}