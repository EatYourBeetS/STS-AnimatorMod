package eatyourbeets.cards.animator;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;

import java.util.List;

public class IchigoKurosaki extends AnimatorCard implements MartialArtist, OnCallbackSubscriber
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

//    @Override
//    public List<TooltipInfo> getCustomTooltips()
//    {
//        if (cardText.index == 1)
//        {
//            return super.getCustomTooltips();
//        }
//
//        return null;
//    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        MartialArtist.ApplyScaling(this, 4);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.DelayedAction(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainForce(magicNumber);

        if (upgraded)
        {
            GameActionsHelper.GainAgility(1);
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            if (GameUtilities.GetStrength() >= secondaryValue)
            {
                GameActionsHelper.MakeCardInDrawPile(new IchigoBankai(), 1, false);
            }
        }
    }
}