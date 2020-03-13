package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class Rider extends AnimatorCard implements MartialArtist, Spellcaster
{
    public static final EYBCardData DATA = Register(Rider.class).SetSkill(2, CardRarity.COMMON);

    public Rider()
    {
        super(DATA);

        Initialize(0, 5, 3);
        SetUpgrade(0, 2, 1);
        SetScaling(0, 1, 0);
        SetSynergy(Synergies.Fate);
    }
    @Override
    public ColoredString GetBottomText(){ return null;}
    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.ReduceStrength(m, magicNumber,true);
    }
}