package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

public class IchigoKurosaki extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(IchigoKurosaki.class.getSimpleName(), EYBCardBadge.Exhaust);

    public IchigoKurosaki()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 2, 6);

        SetExhaust(true);
        SetSynergy(Synergies.Bleach);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new IchigoBankai(), false);
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        MartialArtist.ApplyScaling(this, 3);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.Callback(new WaitAction(0.1f), this::OnCompletion, this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainForce(magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private void OnCompletion(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            if (PlayerStatistics.GetStrength() >= secondaryValue)
            {
                GameActionsHelper.MakeCardInDrawPile(new IchigoBankai(), 1, false);
            }
        }
    }
}