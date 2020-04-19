package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class Sonic extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sonic.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
        {
            DATA.AddPreview(knife, false);
        }
    }

    public Sonic()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.OnePunchMan);
        SetMartialArtist();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlur(magicNumber);
        GameActions.Bottom.ChangeStance(new AgilityStance());

        if (HasSynergy())
        {
            GameActions.Bottom.CreateThrowingKnives(1);
        }
    }
}