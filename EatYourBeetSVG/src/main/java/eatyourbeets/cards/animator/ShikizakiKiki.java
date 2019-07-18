package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.unique.ApotheosisAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.ShikizakiKikiPower;
import eatyourbeets.utilities.GameActionsHelper;

public class ShikizakiKiki extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(ShikizakiKiki.class.getSimpleName());

    public ShikizakiKiki()
    {
        super(ID, 3, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        if (upgraded)
        {
            this.retain = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new ApotheosisAction());
        GameActionsHelper.ApplyPower(p, p, new ShikizakiKikiPower(p, magicNumber), magicNumber);
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