package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameUtilities;

public class Mitsurugi extends AnimatorCard
{
    public static final String ID = Register(Mitsurugi.class.getSimpleName(), EYBCardBadge.Exhaust);

    private AbstractMonster lastTargetEnemy = null;
    private AbstractMonster targetEnemy = null;

    public Mitsurugi()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(6, 0, 1, 4);

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

        GameActionsHelper.GainBlock(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            GameActionsHelper.GainForce(magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }

    private void updateCurrentEffect(AbstractMonster monster)
    {
        if (monster == null || GameUtilities.IsAttacking(monster.intent))
        {
            cardText.OverrideDescription(null, true);
        }
        else
        {
            cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);
        }
    }
//
//    private boolean CanDealDamage(AbstractMonster m)
//    {
//        return (m.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
//                m.intent == AbstractMonster.Intent.ATTACK_DEFEND || m.intent == AbstractMonster.Intent.ATTACK);
//    }
}