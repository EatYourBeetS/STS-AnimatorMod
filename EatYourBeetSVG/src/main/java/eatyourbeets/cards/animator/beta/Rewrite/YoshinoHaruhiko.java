package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class YoshinoHaruhiko extends AnimatorCard {
    public static final EYBCardData DATA = Register(YoshinoHaruhiko.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.Random);

    public YoshinoHaruhiko() {
        super(DATA);

        Initialize(3, 0, 6,2);
        SetUpgrade(1, 0, -2);

        SetMartialArtist();

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0;i<secondaryValue;i++)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }

        if (player.stance.ID.equals(ForceStance.STANCE_ID))
        {
            for (AbstractCard card : player.hand.group)
            {
                if (card instanceof EYBCard && card.type == CardType.ATTACK)
                {
                    ((EYBCard) card).forceScaling += 1;
                    card.flash();
                }
            }
        }
        else
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
            GameActions.Bottom.StackPower(new SelfDamagePower(p, magicNumber));
        }
    }
}