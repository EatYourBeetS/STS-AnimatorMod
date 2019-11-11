package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Konayuki extends AnimatorCard
{
    public static final String ID = Register(Konayuki.class.getSimpleName(), EYBCardBadge.Drawn);

    public Konayuki()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF);

        Initialize(30,0, 3);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (PlayerStatistics.GetStrength() == 0 && EffectHistory.TryActivateSemiLimited(cardID))
        {
            modifyCostForTurn(-1);
            this.flash();
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (PlayerStatistics.GetStrength() >= 10)
        {
            this.target = CardTarget.ENEMY;
        }
        else
        {
            this.target = CardTarget.SELF;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (PlayerStatistics.GetStrength(p) >= 10)
        {
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
        else
        {
            GameActionsHelper.GainForce(magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(10);
            upgradeMagicNumber(1);
        }
    }
}