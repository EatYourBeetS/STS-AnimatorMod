package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.SonicPower;
import eatyourbeets.utilities.GameActionsHelper;

public class Sonic extends AnimatorCard
{
    public static final String ID = CreateFullID(Sonic.class.getSimpleName());

    public Sonic()
    {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 0);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded)
        {
            GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, magicNumber), magicNumber);
        }

        GameActionsHelper.ApplyPower(p, p, new SonicPower(p, 1), 1);
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