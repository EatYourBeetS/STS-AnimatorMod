package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class Yoichi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yoichi.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public Yoichi()
    {
        super(DATA);

        Initialize(0,0, 2);
        SetUpgrade(0,2, 0);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (block > 0)
        {
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.DiscardFromHand(name, 1, false);
        GameActions.Bottom.StackPower(new SupportDamagePower(p, 1))
        .AddCallback(power ->
        {
            if (HasSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
            {
                SupportDamagePower supportDamage = JavaUtilities.SafeCast(power, SupportDamagePower.class);
                if (supportDamage != null)
                {
                    supportDamage.atEndOfTurn(true);
                }
            }
        });
    }
}