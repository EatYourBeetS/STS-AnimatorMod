package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Assassin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Assassin.class).SetAttack(0, AbstractCard.CardRarity.COMMON);

    public Assassin()
    {
        super(DATA);

        this.baseMagicNumber = this.magicNumber = 1;
        this.baseDamage = this.damage = 0;
        SetUpgrade(0, 0, 1);
        SetScaling(0, 1, 0);
        SetRetain(true);
        SetSynergy(Synergies.Fate);
    }
    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        if (enemy != null)
        {
            damage += GameUtilities.GetDebuffsCount(enemy.powers);
        }

        return super.ModifyDamage(enemy, damage);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return DamageAttribute.Instance.SetCard(this).AddMultiplier(3);
    }
    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if(HasSynergy())
            return true;
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }
}