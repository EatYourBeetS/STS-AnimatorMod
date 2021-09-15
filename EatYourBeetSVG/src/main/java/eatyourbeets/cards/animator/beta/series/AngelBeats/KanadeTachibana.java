package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class KanadeTachibana extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KanadeTachibana.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None).SetSeriesFromClassPackage();

    public KanadeTachibana()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 1, 0);

        SetExhaust(true);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(2, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Top.FetchFromPile(name, magicNumber, p.discardPile)
        .SetOptions(false, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0]).AddCallback(
                cards -> {
                    AnimatorCard card = JUtils.SafeCast(cards.get(0), AnimatorCard.class);
                    int lightLevel = GameUtilities.GetAffinityLevel(card, Affinity.Light, true);
                    if (card != null && lightLevel < 2)
                    {
                        card.affinities.Set(Affinity.Light, lightLevel + 1);
                        card.flash();
                    }
                }
        );

        GameActions.Bottom.GainBlessing(secondaryValue,true);

        if (info.IsSynergizing && !CombatStats.HasActivatedLimited(cardID))
        {
            GameActions.Bottom.SelectFromPile(name, magicNumber, p.exhaustPile)
                    .SetFilter(c -> !GameUtilities.IsHindrance(c) && !AfterLifeMod.IsAdded(c))
                    .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[1])
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0 && CombatStats.TryActivateLimited(cardID))
                        {
                            AbstractCard card = cards.get(0);
                            AfterLifeMod.Add(card);
                            card.exhaust = false;
                            AfterLifeMod.AfterlifeAddToControlPile(card);
                        }
                    });
        }
    }
}