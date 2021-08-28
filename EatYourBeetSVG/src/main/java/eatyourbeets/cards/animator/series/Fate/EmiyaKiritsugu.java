package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.*;

public class EmiyaKiritsugu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EmiyaKiritsugu.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(AffinityToken.GetCard(Affinity.Light), false);
                data.AddPreview(AffinityToken.GetCard(Affinity.Dark), false);
            });

    public EmiyaKiritsugu()
    {
        super(DATA);

        Initialize(9, 7, 0, 2);
        SetUpgrade(3, 2);

        SetAffinity_Light(1, 1, 0);
        SetAffinity_Dark(1, 1, 0);
        SetAffinity_Green(0, 0, 3);
        SetAffinity_Blue(0, 0, 1);

        SetExhaust(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && JUtils.Count(player.drawPile.group, c -> c.rarity == CardRarity.UNCOMMON) >= 2;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        final WeightedList<AbstractCard> uncommonCards = new WeightedList<>();
        for (AbstractCard c : p.drawPile.group)
        {
            if (c.rarity == CardRarity.UNCOMMON)
            {
                uncommonCards.Add(c, (GameUtilities.IsHindrance(c) || (GameUtilities.GetAffinityLevel(c, Affinity.Star, true) > 0)) ? 1 : 10);
            }
        }

        if (uncommonCards.Size() < 2)
        {
            return;
        }

        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT)
        .SetSoundPitch(0.6f, 0.7f)
        .SetVFXColor(Color.GOLD);

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final AbstractCard c1 = uncommonCards.Retrieve(rng, true);
        AbstractCard c2;
        do
        {
            c2 = uncommonCards.Retrieve(rng, true);
        }
        while (c2.cardID.equals(c1.cardID) && uncommonCards.Size() > 0);
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
                        GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Dark, false));
                    }
                    if (a.GetLevel(Affinity.Dark, true) > 0)
                    {
                        GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Light, false));
                    }
                    if (a.GetLevel(Affinity.Blue, true) > 0)
                    {
                        GameActions.Bottom.ApplyVulnerable(player, enemy, secondaryValue);
                    }
                }
            }
        });
    }
}