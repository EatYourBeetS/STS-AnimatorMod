package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.DiscardFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Tet extends AnimatorCard
{
    public static final String ID = CreateFullID(Tet.class.getSimpleName());

    public Tet()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0,2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new DiscardFromPileAction(p.drawPile, this.magicNumber, true));
        GameActionsHelper.DrawCard(p, 2);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new EnergizedBluePower(p, 1), 1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
        }
    }
}