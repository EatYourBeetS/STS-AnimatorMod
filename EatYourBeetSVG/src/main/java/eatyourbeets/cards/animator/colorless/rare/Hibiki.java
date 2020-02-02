package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class Hibiki extends AnimatorCard
{
    public static final String ID = Register(Hibiki.class);

    public Hibiki()
    {
        super(ID, 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ALL_ENEMY);

        Initialize(2, 0, 3, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAttackType(EYBAttackType.Ranged);
        SetSynergy(Synergies.Kancolle);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
            .SetOptions(true, false);
        }

        GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.baseMagicNumber += secondaryValue);
    }
}