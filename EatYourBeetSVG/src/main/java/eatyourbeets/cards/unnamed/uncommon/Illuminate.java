package eatyourbeets.cards.unnamed.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;

public class Illuminate extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Illuminate.class)
            .SetMaxCopies(2)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Illuminate()
    {
        super(DATA);

        Initialize(0, 0, 5, 2);
        SetUpgrade(0, 0, 1, 1);

        SetRecast(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.BorderFlash(Color.GOLD);
        GameActions.Bottom.RecoverHP(magicNumber);
        GameActions.Bottom.HealDolls(secondaryValue);
    }
}