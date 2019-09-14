package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.BorosPower;

public class Boros extends AnimatorCard
{
    public static final String ID = CreateFullID(Boros.class.getSimpleName());

    public Boros()
    {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 2);

        isEthereal = true;

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, magicNumber), magicNumber);
        //GameActionsHelper.GainTemporaryHP(p, p, secondaryValue);

        if (!p.hasPower(BorosPower.POWER_ID))
        {
            GameActionsHelper.ApplyPower(p, p, new BorosPower(p));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            isEthereal = false;
        }
    }
}