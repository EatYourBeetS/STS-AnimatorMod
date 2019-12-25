package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mitsurugi extends AnimatorCard
{
    public static final String ID = Register(Mitsurugi.class.getSimpleName(), EYBCardBadge.Exhaust);

    // TODO: use a class for this and for Nanami
    private AbstractMonster lastTargetEnemy = null;
    private AbstractMonster targetEnemy = null;

    public Mitsurugi()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(7, 0, 1, 4);
        SetUpgrade(4, 0, 0, 0);

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

        GameActions.Bottom.GainBlock(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            GameActions.Bottom.GainForce(magicNumber);
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
}