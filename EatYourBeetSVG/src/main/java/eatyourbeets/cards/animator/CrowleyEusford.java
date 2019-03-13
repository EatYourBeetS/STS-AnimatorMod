package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.powers.PlayerStatistics;

public class CrowleyEusford extends AnimatorCard
{
    public static final String ID = CreateFullID(CrowleyEusford.class.getSimpleName());

    public CrowleyEusford()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(7,0,6);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        this.baseDamage += this.magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        RandomizedList<AbstractMonster> enemies = new RandomizedList<>(PlayerStatistics.GetCurrentEnemies(true));
        enemies.Remove(m);

        AbstractMonster m1 = enemies.Retrieve(AbstractDungeon.miscRng);
        if (m1 != null)
        {
            GameActionsHelper.DamageTarget(p, m1, this.damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(2);
            upgradeMagicNumber(2);
        }
    }
}