package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.animator.special.ChlammyZellScheme;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ChlammyZell extends AnimatorCard
{
    public static final String ID = Register(ChlammyZell.class);
    static
    {
        GetStaticData(ID).InitializePreview(new ChlammyZellScheme(), false);
    }

    public ChlammyZell()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.ALL);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(2);
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, magicNumber));

        if (GameUtilities.GetPowerAmount(IntellectPower.POWER_ID) >= 3 && EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(cardData.defaultPreview).SetOptions(false, true);
        }
    }
}