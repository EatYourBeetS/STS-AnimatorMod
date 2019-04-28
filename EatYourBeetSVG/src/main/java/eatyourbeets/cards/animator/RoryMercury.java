package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class RoryMercury extends AnimatorCard
{
    public static final String ID = CreateFullID(RoryMercury.class.getSimpleName());

    public RoryMercury()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(8,0, 4);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        DamageRandomEnemy(AbstractDungeon.player);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        if (HasActiveSynergy())
        {
            DamageRandomEnemy(p);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }

    private void DamageRandomEnemy(AbstractPlayer p)
    {
        AbstractMonster m = PlayerStatistics.GetRandomEnemy(true);
        GameActionsHelper.DamageTargetPiercing(p, m, this.magicNumber, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL).bypassBlock = false;
    }
}