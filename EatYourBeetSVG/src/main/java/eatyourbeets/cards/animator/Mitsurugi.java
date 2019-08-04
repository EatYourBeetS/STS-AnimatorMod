package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Mitsurugi extends AnimatorCard
{
    public static final String ID = CreateFullID(Mitsurugi.class.getSimpleName());

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

        AbstractPlayer p = AbstractDungeon.player;
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (CanDealDamage(m))
        {
            GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
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
        if (monster == null || CanDealDamage(monster))
        {
            rawDescription = cardStrings.DESCRIPTION;
        }
        else
        {
            rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        }

        initializeDescription();
    }

    private boolean CanDealDamage(AbstractMonster m)
    {
        return (m.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
                m.intent == AbstractMonster.Intent.ATTACK_DEFEND || m.intent == AbstractMonster.Intent.ATTACK);
    }
}