package eatyourbeets.cards.animatorClassic.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.animatorClassic.special.ChlammyZell_Scheme;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ChlammyZell extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(ChlammyZell.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new ChlammyZell_Scheme(), false);
    }

    public ChlammyZell()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 1, 0);

        SetSeries(CardSeries.NoGameNoLife);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(2);
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, magicNumber));

        if (GameUtilities.GetPowerAmount(Affinity.Blue) > 1 && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new ChlammyZell_Scheme());
        }
    }
}