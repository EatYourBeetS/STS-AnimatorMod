package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class Defend_OnePunchMan extends Defend
{
    public static final String ID = Register(Defend_OnePunchMan.class.getSimpleName());

    public Defend_OnePunchMan()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);

        SetHealing(true);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper.ApplyPower(p, p, new EnergizedBluePower(p, 1), 1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }

    protected boolean ProgressBoost()
    {
        if (this.secondaryValue > 0)
        {
            int newValue = this.secondaryValue - 1;

            for (AbstractCard c : GetAllInBattleInstances())
            {
                AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
                if (card != null)
                {
                    if (newValue == 0)
                    {
                        card.baseSecondaryValue = 1;
                        card.secondaryValue = 0;
                        card.isSecondaryValueModified = true;
                    }
                    else
                    {
                        card.baseSecondaryValue = card.secondaryValue = newValue;
                    }
                }
            }

            return true;
        }

        return false;
    }
}