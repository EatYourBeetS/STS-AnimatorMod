package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class Shichika extends AnimatorCard
{
    public static final String ID = CreateFullID(Shichika.class.getSimpleName());

    public Shichika()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1, 2);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);

        if (InitializingPreview())
        {
            cardPreview.Initialize(new ShichikaKyotouryuu(), true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new ThornsPower(p, secondaryValue), secondaryValue);
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, magicNumber), magicNumber);

        GameActionsHelper.MakeCardInHand(new ShichikaKyotouryuu(), 1, upgraded);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}