package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;

public class Renewal extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Renewal.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Minion);

    public Renewal()
    {
        super(DATA);

        Initialize(0, 4);
        SetUpgrade(0, 3);

        SetSummon(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SacrificeDoll(m)
        .AddCallback(() -> GameActions.Top.SummonDoll(1));
    }
}