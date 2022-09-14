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
                data.AddPreview(AffinityToken.GetCard(Affinity.Light), true);
                data.AddPreview(AffinityToken.GetCard(Affinity.Dark), true);
            });
    public static final int CARD_CHOICE = 2;

    public EmiyaKiritsugu()
    {
        super(DATA);

        Initialize(9, 9, 0, 2);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);
        SetAffinity_Green(0, 0, 3);
        SetAffinity_Blue(0, 0, 1);

        SetExhaust(true);
        SetInnate(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(JUtils.Count(player.drawPile.group, c -> c.rarity == CardRarity.UNCOMMON) < CARD_CHOICE);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final WeightedList<AbstractCard> uncommonCards = new WeightedList<>();
        for (AbstractCard c : p.drawPile.group)
        {
            if (c.rarity == CardRarity.UNCOMMON && GameUtilities.CanSeal(c))
            {
                uncommonCards.Add(c, (GameUtilities.IsHindrance(c) || (GameUtilities.GetAffinityLevel(c, Affinity.Star, true) > 0)) ? 1 : 10);
            }
        }

        if (uncommonCards.Size() < CARD_CHOICE)
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
                    GameActions.Bottom.SealAffinities(c, false);

                    if (a.GetLevel(Affinity.Light, true) > 0)
                    {
                        GameActions.Bottom.ObtainAffinityToken(Affinity.Dark, true);
                    }
                    if (a.GetLevel(Affinity.Dark, true) > 0)
                    {
                        GameActions.Bottom.ObtainAffinityToken(Affinity.Light, true);
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