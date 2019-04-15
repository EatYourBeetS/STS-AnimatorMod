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
import eatyourbeets.powers.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class Shizu extends AnimatorCard
{
    public static final String ID = CreateFullID(Shizu.class.getSimpleName());

    public Shizu()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(14, 0, 2);

        AddExtendedDescription();

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.DrawCard(p, this.magicNumber);

        GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(new Burn(), 2));
        GameActionsHelper.AddToBottom(new VariableExhaustAction(p, 3, this, this::OnExhaust));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(6);
            //upgradeMagicNumber(1);
        }
    }

    private void OnExhaust(Object state, ArrayList<AbstractCard> cards)
    {
        if (state != this || cards == null || cards.size() == 0)
        {
            return;
        }

        AbstractPlayer p = AbstractDungeon.player;
        int burning = cards.size() * 2;
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, burning), burning);
        }
    }
}