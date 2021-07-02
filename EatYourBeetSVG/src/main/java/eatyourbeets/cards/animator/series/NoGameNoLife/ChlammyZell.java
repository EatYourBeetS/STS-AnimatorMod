package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.animator.special.ChlammyZellScheme;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;

public class ChlammyZell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ChlammyZell.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new ChlammyZellScheme(), false);
    }

    public ChlammyZell()
    {
        super(DATA);

        Initialize(0, 0, 1, IntellectPower.GetThreshold(1));
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.NoGameNoLife);
        SetAlignment(0, 0, 2, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(2);
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, magicNumber));

        if (IntellectPower.GetCurrentLevel() > 1 && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new ChlammyZellScheme());
        }
    }
}