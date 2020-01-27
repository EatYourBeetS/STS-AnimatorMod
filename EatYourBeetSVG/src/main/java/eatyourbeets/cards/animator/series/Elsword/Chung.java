package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Chung extends AnimatorCard
{
    public static final String ID = Register(Chung.class, EYBCardBadge.Exhaust);

    public Chung()
    {
        super(ID, 1, CardRarity.COMMON, CardType.SKILL, CardTarget.ALL);

        Initialize(0, 11, 3);
        SetUpgrade(0, 2, -1);

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
}