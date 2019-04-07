package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Sloth extends AnimatorCard
{
    public static final String ID = CreateFullID(Sloth.class.getSimpleName());

    public Sloth()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(2,0, 1);

        baseSecondaryValue = secondaryValue = 2;

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            Sloth card = Utilities.SafeCast(c, Sloth.class);
            if (card != null)
            {
                card.baseDamage += card.secondaryValue;

                if (card.baseDamage > 9999)
                {
                    card.baseDamage = 9999;
                }
                else
                {
                    card.secondaryValue *= 2;
                }
            }
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(1);
            upgradeSecondaryValue(1);
        }
    }
}