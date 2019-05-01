package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.TemporaryEnvenomPower;

public class AcuraAkari extends AnimatorCard
{
    public static final String ID = CreateFullID(AcuraAkari.class.getSimpleName());

    public AcuraAkari()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 4, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ChooseAndDiscard(1, false);
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.ApplyPower(p, p, new TemporaryEnvenomPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(4);
            upgradeMagicNumber(1);
        }
    }
}