package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class LimeBell extends AnimatorCard
{
    public static final String ID = Register(LimeBell.class);

    public LimeBell()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 7, 2);
        SetUpgrade(0, 4, 0);

        SetExhaust(true);
        SetSynergy(Synergies.AccelWorld);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.Reload(name, cards -> GameActions.Bottom.GainTemporaryHP(cards.size() * magicNumber));
    }
}