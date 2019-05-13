package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PhantasmalPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.RetainCardsOfTypeAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class TanyaDegurechaff_Type95 extends AnimatorCard
{
    public static final String ID = CreateFullID(TanyaDegurechaff_Type95.class.getSimpleName());

    public TanyaDegurechaff_Type95()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        Initialize(0, 0);

        this.exhaust = true;

        SetSynergy(Synergies.YoujoSenki);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.CycleCardAction(2);
        GameActionsHelper.AddToBottom(new RetainCardsOfTypeAction(p, CardType.ATTACK));
        GameActionsHelper.ApplyPower(p, p, new PhantasmalPower(p, 1), 1);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
        }
    }
}