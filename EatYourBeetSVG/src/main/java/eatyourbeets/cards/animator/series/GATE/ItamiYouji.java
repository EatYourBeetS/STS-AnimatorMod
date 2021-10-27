package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ItamiYouji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ItamiYouji.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public ItamiYouji()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(0, 0, 2);

        SetAffinity_Thunder(2);
        SetAffinity_Earth(1);

        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(1.3f, 1.5f);

        GameActions.Bottom.Draw(magicNumber)
        .AddCallback(cards -> {
            int amountSupportDamageTrigger = 0;

            for (AbstractCard card : cards)
            {
                GameUtilities.Retain(card);

                if (card.type == CardType.ATTACK)
                {
                    amountSupportDamageTrigger += 2;
                }
            }

            if (amountSupportDamageTrigger > 0)
            {
                GameUtilities.TriggerSupportDamage(amountSupportDamageTrigger);
            }
        });


    }
}