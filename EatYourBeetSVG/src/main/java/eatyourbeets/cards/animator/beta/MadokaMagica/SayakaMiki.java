package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SayakaMiki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SayakaMiki.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    static
    {
        DATA.AddPreview(new Oktavia(), false);
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public SayakaMiki()
    {
        super(DATA);

        Initialize(0, 6, 0);
        SetUpgrade(0, 2, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        if (GameUtilities.GetHealthPercentage(player) < 0.5f)
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Oktavia()).SetOptions(upgraded, false);
            GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
            this.exhaustOnUseOnce = true;
        }
        else
        {
            if (HasSynergy())
            {
                GameActions.Bottom.ChannelOrb(new Frost(), true);
            }
        }
    }
}
