package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BorosPower;

public class Boros extends AnimatorCard
{
    public static final String ID = CreateFullID(Boros.class.getSimpleName());

    public Boros()
    {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, magicNumber), magicNumber);
        GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, magicNumber), magicNumber);
        GameActionsHelper.ApplyPower(p, p, new FocusPower(p, magicNumber), magicNumber);

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
            upgradeMagicNumber(1);
        }
    }
}