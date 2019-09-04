package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Kuroyukihime extends AnimatorCard
{
    public static final String ID = CreateFullID(Kuroyukihime.class.getSimpleName());

    public Kuroyukihime()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 2);

        this.exhaust = true;

        //AddExtendedDescription();

        SetSynergy(Synergies.AccelWorld);
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
        if (GetOtherCardsInHand().size() >= this.magicNumber)
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

    private static AbstractCard preview;

    @Override
    protected AbstractCard GetCardPreview()
    {
        if (preview == null)
        {
            preview = new BlackLotus();
        }

        return preview;
    }
}