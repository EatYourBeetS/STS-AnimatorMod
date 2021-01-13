package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Nyanta extends AnimatorCard {
    public static final EYBCardData DATA = Register(Nyanta.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing);

    public Nyanta() {
        super(DATA);

        Initialize(3, 0, 2, 3);
        SetUpgrade(1,0);
        SetScaling(0,1,0);
        SetExhaust(true);
        SetRetain(true);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
            .SetVFX(true, false);
        }

        int agilityToGain = CombatStats.SynergiesThisTurn();

        if (agilityToGain > secondaryValue)
        {
            agilityToGain = secondaryValue;
        }

        GameActions.Bottom.GainAgility(agilityToGain);
        GameActions.Bottom.Add(new PressEndTurnButtonAction());
    }
}