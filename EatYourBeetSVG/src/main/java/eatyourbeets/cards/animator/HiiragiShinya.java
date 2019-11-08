package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActionsHelper;

import java.util.List;

public class HiiragiShinya extends AnimatorCard
{
    public static final String ID = Register(HiiragiShinya.class.getSimpleName(), EYBCardBadge.Synergy);

    public HiiragiShinya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,4, 2);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    private void OnFetch(List<AbstractCard> cards)
    {
        if (cards != null && cards.size() == 1)
        {
            AbstractCard c = cards.get(0);

            c.applyPowers();
            PlayerStatistics.OnCostRefresh(c);
            c.setCostForTurn(c.costForTurn + 1);
            c.retain = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, block);
        GameActionsHelper.AddToBottom(new FetchAction(p.discardPile, 1, this::OnFetch));

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new SupportDamagePower(p, magicNumber), magicNumber);
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