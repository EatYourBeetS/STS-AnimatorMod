package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Cocytus extends AnimatorCard
{
    public static final String ID = Register(Cocytus.class.getSimpleName(), EYBCardBadge.Synergy);

    public Cocytus()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(8,0, 2);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (GameUtilities.GetStrength() * (magicNumber - 1)));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActions.Bottom.GainForce(1);

        if (HasActiveSynergy())
        {
            if (AbstractDungeon.cardRandomRng.randomBoolean())
            {
                GameActions.Bottom.GainThorns(1);
            }
            else
            {
                GameActions.Bottom.GainPlatedArmor(1);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {          
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }
}