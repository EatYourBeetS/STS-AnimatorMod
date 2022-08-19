package eatyourbeets.cards.animatorClassic.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animatorClassic.BorosPower;
import eatyourbeets.utilities.GameActions;

public class Boros extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Boros.class).SetPower(4, CardRarity.RARE);

    public Boros()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetCostUpgrade(-1);

        SetEthereal(true);
        SetSeries(CardSeries.OnePunchMan);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.StackPower(new RegenPower(player, magicNumber));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyPower(p, p, new BorosPower(p));
    }
}