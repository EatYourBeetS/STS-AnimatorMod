package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.List;

public class KaguyaHouraisan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaguyaHouraisan.class).SetAttack(1,CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public KaguyaHouraisan()
    {
        super(DATA);

        Initialize(10, 0, 4);
        SetUpgrade(0, 0, 1);
        SetAffinity_Blue(2, 0, 2);
        SetAffinity_Silver(2, 0, 0);

        SetHitCount(4,1);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return (player.drawPile.isEmpty());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.PSYCHOKINESIS);
        GameActions.Bottom.Add(AffinityToken.SelectTokenAction(name, 1)
                .SetOptions(true, false)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        GameActions.Bottom.MakeCardInDrawPile(c);
                    }
                }));

        if (info.IsSynergizing)
        {
            List<AbstractCard> cardsToPlay = new ArrayList<>();

            cardsToPlay.addAll(player.hand.group);

            for (AbstractCard card : cardsToPlay)
            {
                GameActions.Bottom.PlayCard(card, player.hand, null)
                        .SpendEnergy(false);
            }
        }
    }
}

