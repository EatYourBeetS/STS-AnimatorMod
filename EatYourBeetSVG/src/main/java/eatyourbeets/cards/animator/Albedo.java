package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;

public class Albedo extends AnimatorCard
{
    public static final String ID = Register(Albedo.class.getSimpleName(), EYBCardBadge.Special);

    public Albedo()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(8,0);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, damage));

        if (EffectHistory.HasActivatedLimited(Ainz.ID))
        {
            SetLoyal(true);
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
}