package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class YoshinoHaruhiko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YoshinoHaruhiko.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.Random);

    public YoshinoHaruhiko()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(0, 0, 1);
        SetExhaust(true);

        SetSynergy(Synergies.Rewrite);
        SetMartialArtist();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }

        GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);

        if (IsStarter())
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
    }
}