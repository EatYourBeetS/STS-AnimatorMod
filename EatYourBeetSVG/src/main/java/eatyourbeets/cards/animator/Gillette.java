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

        Initialize(15, 9, 1);

        //AddExtendedDescription();

        SetSynergy(Synergies.Chaika);
    }

//    @Override
//    public void triggerOnExhaust()
//    {
//        super.triggerOnExhaust();
//
//        RandomizedList<AbstractMonster> enemies = new RandomizedList<>();
//        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
//        {
//            if (!m.hasPower(StunMonsterPower.POWER_ID))
//            {
//                enemies.Add(m);
//            }
//        }
//
//        AbstractMonster enemy = enemies.Retrieve(AbstractDungeon.miscRng);
//        if (enemy != null)
//        {
//            AbstractPlayer p = AbstractDungeon.player;
//            GameActionsHelper.ApplyPower(p, enemy, new StunMonsterPower(enemy, 1), 1);
//        }
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GameActionManager.totalDiscardedThisTurn > 0)
        {
            GameActionsHelper.GainBlock(p, this.block);
        }

        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

//        int discarded = 0;
//        for (AbstractCard c : p.hand.getAttacks().group)
//        {
//            if (c != this)
//            {
//                GameActionsHelper.AddToBottom(new DiscardSpecificCardAction(c, p.hand));
//                discarded += 1;
//            }
//        }
//
//        if (discarded > 0)
//        {
//            GameActionsHelper.ApplyPower(p, p, new DrawCardNextTurnPower(p, discarded), discarded);
//        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeBlock(2);
        }
    }
}