package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;

public class Chung extends AnimatorCard
{
    public static final String ID = Register(Chung.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Chung()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(0, 11, 3);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ChannelOrb(new Frost(), true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.block > 0)
        {
            GameActions.Bottom.GainBlock(this.block);
        }

        GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.baseBlock = Math.max(0, c.baseBlock - c.magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(2);
            upgradeMagicNumber(-1);
        }
    }
}