package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

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

        Initialize(9, 7, 3, 2);
        SetUpgrade(2, 2);

        SetAffinity_Light(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Green(0, 0, 3);
        SetAffinity_Blue(0, 0, 1);

        SetExhaust(true);
        SetRetainOnce(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(JUtils.Count(player.drawPile.group, c -> c.rarity == CardRarity.UNCOMMON) < magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final RandomizedList<AbstractCard> uncommonCards = new RandomizedList<>();
        uncommonCards.AddAll(JUtils.Filter(p.drawPile.group, c -> c.rarity == CardRarity.UNCOMMON));
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (group.size() < magicNumber && uncommonCards.Size() > 0) {
            group.addToBottom(uncommonCards.Retrieve(rng, true));
        }
        if (group.size() < magicNumber)
        {
            return;
        }

        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d
        .SetSoundPitch(0.6f, 0.7f)
        .SetVFXColor(Color.GOLD));

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
                        GameActions.Bottom.GainEnergyNextTurn(1);
                    }
                }
            }
        });
    }
}