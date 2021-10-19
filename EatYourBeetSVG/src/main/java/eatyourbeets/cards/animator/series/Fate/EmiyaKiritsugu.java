package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;

public class EmiyaKiritsugu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EmiyaKiritsugu.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public EmiyaKiritsugu()
    {
        super(DATA);

        Initialize(10, 10, 2, 15);
        SetUpgrade(0,0,0,10);

        SetAffinity_Light(2);
        SetAffinity_Dark();
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(JUtils.Count(player.drawPile.group, c -> GameUtilities.HasAffinity(c, Affinity.Light) || GameUtilities.HasAffinity(c, Affinity.Dark)) < magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final WeightedList<AbstractCard> validCards = new WeightedList<>();
        for (AbstractCard c : p.drawPile.group)
        {
            if (GameUtilities.HasAffinity(c, Affinity.Light) || GameUtilities.HasAffinity(c, Affinity.Dark))
            {
                validCards.Add(c, (GameUtilities.IsHindrance(c) || (GameUtilities.GetAffinityLevel(c, Affinity.Star, true) > 0)) ? 1 : 10);
            }
        }

        if (validCards.Size() < magicNumber)
        {
            return;
        }

        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT)
        .SetSoundPitch(0.6f, 0.7f)
        .SetVFXColor(Color.GOLD);

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final AbstractCard c1 = validCards.Retrieve(rng, true);
        AbstractCard c2;
        do
        {
            c2 = validCards.Retrieve(rng, true);
        }
        while (c2.cardID.equals(c1.cardID) && validCards.Size() > 0);
        group.group.add(c1);
        group.group.add(c2);

        GameActions.Bottom.ExhaustFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(m, (enemy, cards) ->
        {
            for (AbstractCard c : cards)
            {
                EYBCardAffinities a = GameUtilities.GetAffinities(c);
                if (a != null)
                {
                    if (a.GetLevel(Affinity.Light, true) > 0)
                    {
                        GameActions.Bottom.RaiseDarkLevel(secondaryValue);
                    }
                    if (a.GetLevel(Affinity.Dark, true) > 0)
                    {
                        GameActions.Bottom.RaiseLightLevel(secondaryValue);
                    }
                }
            }
        });
    }
}