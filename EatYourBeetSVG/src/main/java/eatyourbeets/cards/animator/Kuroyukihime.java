package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Kuroyukihime extends AnimatorCard
{
    public static final String ID = Register(Kuroyukihime.class.getSimpleName());

    public Kuroyukihime()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 2);

        SetExhaust(true);
        SetSynergy(Synergies.AccelWorld);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new BlackLotus(), false);
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GameUtilities.GetOtherCardsInHand(this).size() >= this.magicNumber)
        {
            GameActionsHelper.Discard(this.magicNumber, false);
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new BlackLotus(), 1));
            this.exhaust = true;
        }
        else
        {
            this.exhaust = false;
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}