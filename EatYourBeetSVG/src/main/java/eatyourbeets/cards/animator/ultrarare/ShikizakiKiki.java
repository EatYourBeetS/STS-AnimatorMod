package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.ShikizakiKikiUpgradeAction;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.ShikizakiKikiPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class ShikizakiKiki extends AnimatorCard_UltraRare
{
    public static final String ID = Register(ShikizakiKiki.class.getSimpleName(), EYBCardBadge.Special);

    public static int BASE_POWER_AMOUNT = 3;
    public static int UPGRADED_POWER_AMOUNT = 4;

    public ShikizakiKiki()
    {
        super(ID, 3, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, BASE_POWER_AMOUNT);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new ShikizakiKikiPower(p, upgraded));

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Add(new ShikizakiKikiUpgradeAction());
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(UPGRADED_POWER_AMOUNT - BASE_POWER_AMOUNT);
        }
    }
}