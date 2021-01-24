package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class SajinKomamura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SajinKomamura.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public SajinKomamura()
    {
        super(DATA);

        Initialize(0, 7, 2,1);
        SetUpgrade(0, 3, 0);
        SetScaling(0,1,0);

        SetMartialArtist();

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        if (ForceStance.IsActive())
        {
            GameActions.Bottom.GainForce(magicNumber);
        }
        else if (AgilityStance.IsActive())
        {
            GameActions.Bottom.GainAgility(magicNumber);
        }
        else if (IntellectStance.IsActive())
        {
            GameActions.Bottom.GainIntellect(magicNumber);
        }
        else
        {
            GameActions.Bottom.Draw(magicNumber);
        }

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainMetallicize(secondaryValue);
        }
    }
}