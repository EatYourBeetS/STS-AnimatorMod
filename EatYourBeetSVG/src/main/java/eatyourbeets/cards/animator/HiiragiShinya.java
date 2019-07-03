package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

import java.util.List;

public class HiiragiShinya extends AnimatorCard
{
    public static final String ID = CreateFullID(HiiragiShinya.class.getSimpleName());

    public HiiragiShinya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,3, 2, 5);

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
        GameActionsHelper.ApplyPower(p, p, new SupportDamagePower(p, magicNumber), magicNumber);
        GameActionsHelper.AddToBottom(new FetchAction(p.discardPile, 1, this::OnFetch));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }

    private void ReturnToHand()
    {
        AbstractPlayer p = AbstractDungeon.player;
        SupportDamagePower d = Utilities.GetPower(p, SupportDamagePower.POWER_ID);
        if (d != null && d.amount >= secondaryValue)
        {
            GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.hand));
            GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, d, secondaryValue));
        }
    }
}