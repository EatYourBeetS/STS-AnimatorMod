package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class DolaStephanie extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaStephanie.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (EYBCardData d : AffinityToken.GetCardData())
                {
                    data.AddPreview(d.CreateNewInstance(), true);
                }
            });

    public DolaStephanie()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Star(1);

        SetAffinityRequirement(Affinity.Sealed, 1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetExhaust(false);
        SetFading(true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(1);

        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(info, (info2, cards) ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Top.MoveCard(c, player.hand, player.drawPile);

                final EYBCardAffinities a = GameUtilities.GetAffinities(c);
                if (a != null && !a.sealed && CheckSpecialCondition(true))
                {
                    final int star = a.GetLevel(Affinity.Star, true);
                    if (star > 0)
                    {
                        GameActions.Bottom.ObtainAffinityToken(Affinity.Star, star > 1);
                    }
                    else
                    {
                        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for (EYBCardAffinity aff : a.List)
                        {
                            if (aff.level > 0)
                            {
                                group.group.add(AffinityToken.GetCopy(aff.type, aff.level > 1));
                            }
                        }

                        if (group.size() == 1)
                        {
                            GameActions.Bottom.MakeCardInHand(group.group.get(0));
                        }
                        else if (group.size() > 1)
                        {
                            GameActions.Bottom.SelectFromPile(name, 1, group)
                            .SetOptions(false, false)
                            .AddCallback(tokens ->
                            {
                                for (AbstractCard token : tokens)
                                {
                                    GameActions.Bottom.MakeCardInHand(token);
                                }
                            });
                        }
                    }
                }
            }

            GameActions.Bottom.Add(new RefreshHandLayout());
        });
    }
}