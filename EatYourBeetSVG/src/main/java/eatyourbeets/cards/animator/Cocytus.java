package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;
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
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActionsHelper.GainForce(1);

        if (HasActiveSynergy())
        {
            if (AbstractDungeon.cardRandomRng.randomBoolean())
            {
                GameActionsHelper.ApplyPower(p, p, new ThornsPower(p, 1), 1);
            }
            else
            {
                GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, 1), 1);
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