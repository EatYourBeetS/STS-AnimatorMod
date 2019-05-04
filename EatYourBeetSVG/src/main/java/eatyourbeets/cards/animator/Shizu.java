package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.VariableExhaustAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Fire;

import java.util.ArrayList;

public class Shizu extends AnimatorCard
{
    public static final String ID = CreateFullID(Shizu.class.getSimpleName());

    public Shizu()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(16, 0, 2);

        AddExtendedDescription();

        this.isEthereal = true;

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.DrawCard(p, this.magicNumber);

        GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(new Burn(), 2));
        GameActionsHelper.AddToBottom(new VariableExhaustAction(p, 3, m, this::OnExhaust));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
            //upgradeMagicNumber(1);
        }
    }

    private void OnExhaust(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == null || cards == null || cards.size() == 0)
        {
            return;
        }

        AbstractMonster m = (AbstractMonster)state;
        AbstractPlayer p = AbstractDungeon.player;

        for (int i = 0; i < cards.size(); i++)
        {
            GameActionsHelper.ChannelOrb(new Fire(), true);
        }
        //int burning = cards.size() * 2;
        //GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, burning), burning);
    }
}