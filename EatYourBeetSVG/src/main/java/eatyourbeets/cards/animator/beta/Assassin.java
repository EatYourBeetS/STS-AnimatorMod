package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Assassin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Assassin.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Piercing);

    public Assassin()
    {
        super(DATA);

        Initialize(3, 0, 0);
        SetUpgrade(2, 0, 0);
        SetScaling(0, 1, 0);

        SetRetain(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerWhenDrawn()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        if (c.type == CardType.ATTACK)
        {
            SetRetain(false);
        }
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        return super.ModifyDamage(enemy, damage + (GameUtilities.GetDebuffsCount(enemy) * magicNumber));
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }
}