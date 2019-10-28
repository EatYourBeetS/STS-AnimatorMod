package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.BurningPower;

public class Genos extends AnimatorCard
{
    public static final String ID = Register(Genos.class.getSimpleName(), EYBCardBadge.Synergy);

    public Genos()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(8, 0, 4);

        SetSynergy(Synergies.OnePunchMan);

//        if (InitializingPreview())
//        {
//            cardData.InitializePreview(new Overheat(), true);
//        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, this.magicNumber), this.magicNumber);
        GameActionsHelper.MakeCardInHand(new Overheat(), 1, false);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}