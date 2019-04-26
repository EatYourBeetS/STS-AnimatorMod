package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Gillette extends AnimatorCard
{
    public static final String ID = CreateFullID(Gillette.class.getSimpleName());

    public Gillette()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(19, 9, 1);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GameActionManager.totalDiscardedThisTurn > 0)
        {
            GameActionsHelper.GainBlock(p, this.block);
        }

        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
            upgradeBlock(2);
        }
    }
}