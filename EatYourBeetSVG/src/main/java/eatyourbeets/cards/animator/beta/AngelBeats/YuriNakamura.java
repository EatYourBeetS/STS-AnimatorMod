package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YuriNakamura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuriNakamura.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    public YuriNakamura()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetOptions(false, false, false)
        .SetFilter(c -> !c.isEthereal)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                AbstractCard card = cards.get(0);
                card.selfRetain = true;
                card.flash();
            }
        });

        if (HasSynergy() && !CombatStats.HasActivatedLimited(cardID))
        {
            GameActions.Bottom.SelectFromPile(name, magicNumber, p.exhaustPile)
            .SetFilter(c -> !GameUtilities.IsCurseOrStatus(c) && !AfterLifeMod.IsAdded(c))
            .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[1])
            .AddCallback(cards ->
            {
                if (cards.size() > 0 && CombatStats.TryActivateSemiLimited(cardID))
                {
                    AbstractCard card = cards.get(0);
                    AfterLifeMod.Add(card);
                    card.exhaust = false;
                    card.tags.add(GR.Enums.CardTags.PURGE);
                    AfterLifeMod.AfterlifeAddToControlPile(card);
                }
            });
        }
    }
}