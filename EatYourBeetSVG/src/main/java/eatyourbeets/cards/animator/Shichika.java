package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.utilities.GameActionsHelper;

public class Shichika extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Shichika.class.getSimpleName(), EYBCardBadge.Synergy);

    public Shichika()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new ShichikaKyotouryuu(), false);
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        MartialArtist.ApplyScaling(this, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainForce(magicNumber);
        GameActionsHelper.MakeCardInHand(new ShichikaKyotouryuu(), 1, false);

        if (HasActiveSynergy())
        {
            GameActionsHelper.GainAgility(1);
            GameActionsHelper.ApplyPower(p, p, new ThornsPower(p, 2), 2);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}