package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.replacement.TemporaryRetainPower;
import eatyourbeets.utilities.GameActions;

public class Preparation extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Preparation.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Preparation()
    {
        super(DATA);

        Initialize(0, 3, 1);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        if (IsSolo())
        {
            GameActions.Bottom.FetchFromPile(name, 1, p.drawPile);
        }

        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new TemporaryRetainPower(p, magicNumber));
        GameActions.Bottom.ApplyPower(new NoDrawPower(p)).ShowEffect(false, true);
    }
}