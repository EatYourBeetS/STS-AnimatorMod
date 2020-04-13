package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import com.megacrit.cardcrawl.powers.MalleablePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Greed extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Greed.class).SetPower(2, CardRarity.RARE);
    public static final int GOLD_DIVISOR = 150;

    public Greed()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 1);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new MalleablePower(p, secondaryValue));
        GameActions.Bottom.GainMetallicize(magicNumber);

        if (CombatStats.TryActivateLimited(cardID))
        {
            int energy = Math.floorDiv(player.gold, GOLD_DIVISOR);
            if (energy > 0)
            {
                GameActions.Bottom.StackPower(new EnergizedBluePower(p, energy));
            }
        }
    }
}