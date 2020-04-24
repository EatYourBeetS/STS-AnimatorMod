package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class YoshinoHaruhiko extends AnimatorCard {
    public static final EYBCardData DATA = Register(YoshinoHaruhiko.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.None);

    public YoshinoHaruhiko() {
        super(DATA);

        Initialize(11, 0, 2);
        SetUpgrade(2, 0, 0);

        SetMartialArtist();

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamageToRandomEnemy(damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (player.stance.ID.equals(ForceStance.STANCE_ID))
        {
            for (AbstractCard card : player.hand.group)
            {
                if (card instanceof EYBCard)
                {
                    ((EYBCard) card).forceScaling += magicNumber;
                    card.flash();
                }
            }
        }
        else
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        }
    }
}