package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.BorosPower;
import eatyourbeets.utilities.GameActions;

public class Boros extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Boros.class).SetPower(4, CardRarity.RARE);

    public Boros()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetCostUpgrade(-1);

        SetEthereal(true);
        SetSynergy(Synergies.OnePunchMan);
        SetAffinity(2, 2, 0, 0, 2);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ApplyPower(p, p, new BorosPower(p));
    }
}