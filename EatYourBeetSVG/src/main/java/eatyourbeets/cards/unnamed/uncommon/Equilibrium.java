package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Equilibrium extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Equilibrium.class)
            .SetAttack(1, CardRarity.UNCOMMON);

    public Equilibrium()
    {
        super(DATA);

        Initialize(3, 3, 2);
        SetUpgrade(2, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.ReshuffleFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.DrawNextTurn(cards.size());
            }
        });
    }
}