package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YuriNakamura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuriNakamura.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged);

    public YuriNakamura()
    {
        super(DATA);

        Initialize(4, 8, 1, 0);
        SetUpgrade(1, 2);
        SetScaling(0, 1, 0);
        SetExhaust(true);
        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (!CombatStats.HasActivatedLimited(cardID))
        {
            GameActions.Bottom.SelectFromPile(name, magicNumber, p.exhaustPile)
            .SetFilter(c -> !GameUtilities.IsCurseOrStatus(c) && !AfterLifeMod.IsAdded(c))
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