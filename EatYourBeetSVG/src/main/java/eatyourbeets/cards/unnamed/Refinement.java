package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

public class Refinement extends UnnamedCard
{
    public static final String ID = CreateFullID(Refinement.class.getSimpleName());

    public Refinement()
    {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0,2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DrawCard(p, 1);

        int amount = PlayerStatistics.GetStrength(p);
        if (amount > 0)
        {
            GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(p, p, StrengthPower.POWER_ID));
            GameActionsHelper.ApplyPower(p, p, new FocusPower(p, amount));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}