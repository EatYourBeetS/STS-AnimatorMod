package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

public class Mitsurugi extends AnimatorCard
{
    public static final String ID = Register(Mitsurugi.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Exhaust);

    private AbstractMonster lastTargetEnemy = null;
    private AbstractMonster targetEnemy = null;

    public Mitsurugi()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(8, 0, 2);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        targetEnemy = mo;
    }

    @Override
    public void update()
    {
        super.update();

        if (lastTargetEnemy != targetEnemy)
        {
            updateCurrentEffect(targetEnemy);

            lastTargetEnemy = targetEnemy;
        }

        targetEnemy = null;
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.GainForce(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (PlayerStatistics.IsAttacking(m.intent))
        {
            GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.GainForce(1);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }

    private void updateCurrentEffect(AbstractMonster monster)
    {
        if (monster == null || PlayerStatistics.IsAttacking(monster.intent))
        {
            rawDescription = cardData.strings.DESCRIPTION;
        }
        else
        {
            rawDescription = cardData.strings.EXTENDED_DESCRIPTION[0];
        }

        initializeDescription();
    }
//
//    private boolean CanDealDamage(AbstractMonster m)
//    {
//        return (m.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
//                m.intent == AbstractMonster.Intent.ATTACK_DEFEND || m.intent == AbstractMonster.Intent.ATTACK);
//    }
}