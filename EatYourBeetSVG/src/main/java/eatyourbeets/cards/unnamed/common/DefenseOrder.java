package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class DefenseOrder extends UnnamedCard
{
    public static final EYBCardData DATA = Register(DefenseOrder.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public DefenseOrder()
    {
        super(DATA);

        Initialize(0, 4, 1);
        SetUpgrade(0, 2, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ActivateDoll(1)
        .SetFilter(d -> GameUtilities.IsDefending(d.intent))
        .AddCallback(dolls ->
        {
            for (UnnamedDoll doll : dolls)
            {
                doll.increaseMaxHp(magicNumber, true);
            }
        });
    }
}