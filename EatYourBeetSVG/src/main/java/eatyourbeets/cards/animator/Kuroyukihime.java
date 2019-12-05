package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Kuroyukihime extends AnimatorCard implements OnCallbackSubscriber
{
    public static final String ID = Register(Kuroyukihime.class.getSimpleName());

    public Kuroyukihime()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.AccelWorld);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new BlackLotus(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DelayedAction(this);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            if (GameUtilities.GetOtherCardsInHand(this).size() >= this.magicNumber)
            {
                GameActionsHelper.Discard(this.magicNumber, false);
                GameActionsHelper.MakeCardInHand(new BlackLotus(), 1, false);

                this.exhaustOnUseOnce = true;
            }
        }
    }
}