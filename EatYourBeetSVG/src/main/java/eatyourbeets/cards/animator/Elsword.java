package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Fire;

public class Elsword extends AnimatorCard
{
    public static final String ID = Register(Elsword.class.getSimpleName(), EYBCardBadge.Discard);

    public Elsword()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(11,0, 4, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper.ChannelOrb(new Fire(), false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, magicNumber), magicNumber);
        GameActionsHelper.CycleCardAction(secondaryValue, name);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeSecondaryValue(1);
        }
    }
}