package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class YuiTsuruno extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuiTsuruno.class)
            .SetAttack(0, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public YuiTsuruno()
    {
        super(DATA);

        Initialize(8, 0, 2);

        SetAffinity_Red(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(info, (info2, cards) ->
        {
            if (info2.CanActivateSemiLimited)
            {
                for (AbstractCard c : cards)
                {
                    if (upgraded)
                    {
                        GameUtilities.Retain(c);
                    }

                    if (Curse_GriefSeed.DATA.IsCard(c))
                    {
                        GameActions.Bottom.ChannelOrb(new Fire());
                    }
                }
            }

            final RandomizedList<AbstractCard> toDiscard = new RandomizedList<>();
            for (AbstractCard c : player.hand.group)
            {
                if (!cards.contains(c) && !c.uuid.equals(uuid))
                {
                    toDiscard.Add(c);
                }
            }

            int discarded = 0;
            while (toDiscard.Size() > 0 && discarded < magicNumber)
            {
                GameActions.Delayed.Discard(toDiscard.Retrieve(rng), player.hand)
                .ShowEffect(true, true, 0.1f);
                discarded += 1;
            }
        });
    }
}