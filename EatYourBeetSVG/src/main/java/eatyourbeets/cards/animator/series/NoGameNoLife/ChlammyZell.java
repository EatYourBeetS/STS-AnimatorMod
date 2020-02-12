package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.animator.special.ChlammyZellScheme;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

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

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(2);
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, magicNumber));

        if (!EffectHistory.HasActivatedLimited(cardID))
        {
            IntellectPower intellect = GameUtilities.GetPower(player, IntellectPower.class);
            if (intellect != null && intellect.GetCurrentLevel() > 1)
            {
                GameActions.Bottom.MakeCardInHand(new ChlammyZellScheme());
                EffectHistory.TryActivateLimited(cardID);
            }
        }
    }
}