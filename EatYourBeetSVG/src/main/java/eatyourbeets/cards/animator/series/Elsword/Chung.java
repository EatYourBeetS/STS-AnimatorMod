package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Chung extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chung.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Chung()
    {
        super(DATA);

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
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseBlock = Math.max(0, c.baseBlock - c.magicNumber));
    }
}