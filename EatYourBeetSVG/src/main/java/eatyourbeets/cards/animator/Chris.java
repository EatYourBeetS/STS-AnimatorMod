package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.DrawSpecificCardAction;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.StolenGoldPower;

public class Chris extends AnimatorCard
{
    public static final String ID = Register(Chris.class.getSimpleName(), EYBCardBadge.Discard);

    public Chris()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(4,0, 4);

        AddExtendedDescription();

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper.AddToBottom(new DrawSpecificCardAction((c) ->
                (c.costForTurn == 0 && c.type != CardType.CURSE && c.type != CardType.STATUS)));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (m != null)
        {
            GameActionsHelper.ApplyPowerSilently(p, m, new StolenGoldPower(m, this.magicNumber), this.magicNumber);
        }

        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
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