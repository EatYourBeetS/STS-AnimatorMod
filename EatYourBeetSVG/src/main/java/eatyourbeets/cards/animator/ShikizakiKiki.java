package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.ShikizakiKikiUpgradeAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.ShikizakiKikiPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

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
        GameActionsHelper.ApplyPower(p, p, new ShikizakiKikiPower(p, upgraded), 1);

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper.AddToBottom(new ShikizakiKikiUpgradeAction());
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