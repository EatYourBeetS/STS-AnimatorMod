package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class Rider extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rider.class).SetSkill(2, CardRarity.COMMON);

    public Rider()
    {
        super(DATA);

        Initialize(0, 6, 3);
        SetUpgrade(0, 0, 1);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Fate);
        SetMartialArtist();
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public ColoredString GetBottomText()
    {
        return null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        GameActions.Bottom.Callback(() ->
        {
            tagCache.clear();
            for(AbstractCard c : player.hand.group)
            {
                tagCache.addAll(c.tags);
            }
            if (tagCache.contains(MARTIAL_ARTIST) && tagCache.contains(SPELLCASTER))
            {
                GameActions.Bottom.GainAgility(1);
                GameActions.Bottom.GainIntellect(1);
            }
        });
    }
}