package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.common.RefreshHandLayoutAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class HiiragiShinya extends AnimatorCard
{
    public static final String ID = Register(HiiragiShinya.class.getSimpleName(), EYBCardBadge.Synergy);

    public HiiragiShinya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,4, 2);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.FetchFromPile(name, 1, p.discardPile)
        .SetMessage(MoveCardsAction.TEXT[0])
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                AbstractCard c = cards.get(0);
                c.setCostForTurn(c.costForTurn + 1);
                c.retain = true;
                GameActionsHelper_Legacy.AddToBottom(new RefreshHandLayoutAction());
            }
        });

        if (HasActiveSynergy())
        {
            GameActionsHelper_Legacy.ApplyPower(p, p, new SupportDamagePower(p, magicNumber), magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}