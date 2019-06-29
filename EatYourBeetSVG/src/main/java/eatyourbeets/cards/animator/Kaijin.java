package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.KaijinPower;

public class Kaijin extends AnimatorCard
{
    public static final String ID = CreateFullID(Kaijin.class.getSimpleName());

    public Kaijin()
    {
        super(ID, 2, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 1, 2);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new KaijinPower(p, this.magicNumber), this.magicNumber);

        if (upgraded)
        {
            GameActionsHelper.ApplyPowerSilently(p, p, new DrawCardNextTurnPower(p, secondaryValue), secondaryValue);
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}