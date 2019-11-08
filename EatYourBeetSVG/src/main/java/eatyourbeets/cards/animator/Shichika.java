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

        Initialize(0, 0, 1, 2);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new ShichikaKyotouryuu(), true);
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        MartialArtist.ApplyScaling(this, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new ThornsPower(p, secondaryValue), secondaryValue);
        GameActionsHelper.GainForce(magicNumber);

        if (HasActiveSynergy())
        {
            GameActionsHelper.MakeCardInHand(new ShichikaKyotouryuu(), 1, false);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
            upgradeMagicNumber(1);
        }
    }
}