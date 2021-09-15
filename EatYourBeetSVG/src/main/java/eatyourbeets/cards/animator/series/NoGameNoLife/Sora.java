package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.SoraAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Sora extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sora.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.ALL)
            .SetSeries(CardSeries.NoGameNoLife)
            .PostInitialize(data -> data.AddPreview(new Shiro(), false));

    public Sora()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(2);

        SetMultiDamage(true);
        SetProtagonist(true);
        SetHarmonic(true);
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