package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Crystallize extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Crystallize.class)
            .SetStatus(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Crystallize()
    {
        super(DATA);

        Initialize(0, 0, 4, 3);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(false);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!this.dontTriggerOnUseCard)
        {
            if (GameUtilities.GetHP(player, true, false) > (secondaryValue * 2))
            {
                GameActions.Bottom.SFX(SFX.ORB_FROST_EVOKE, 1f, 1.2f);
                GameActions.Bottom.LoseHP(secondaryValue, AttackEffects.SLASH_VERTICAL).CanKill(false);
                GameActions.Bottom.GainMetallicize(magicNumber);
                GameActions.Bottom.LoseHP(secondaryValue, AttackEffects.SLASH_VERTICAL).CanKill(false);
            }
        }
    }
}