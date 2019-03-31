package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.powers.PlayerStatistics;

public class Gillette extends AnimatorCard
{
    public static final String ID = CreateFullID(Gillette.class.getSimpleName());

    public Gillette()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(16,0);

        AddExtendedDescription();

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        RandomizedList<AbstractMonster> enemies = new RandomizedList<>();
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (!m.hasPower(StunMonsterPower.POWER_ID))
            {
                enemies.Add(m);
            }
        }

        AbstractMonster enemy = enemies.Retrieve(AbstractDungeon.miscRng);
        if (enemy != null)
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.ApplyPower(p, enemy, new StunMonsterPower(enemy, 1), 1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper.DrawCard(p, 2);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}