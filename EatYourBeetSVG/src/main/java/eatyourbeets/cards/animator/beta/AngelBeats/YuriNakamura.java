package eatyourbeets.cards.animator.beta.AngelBeats;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.utilities.GameActions;

public class YuriNakamura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuriNakamura.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged);

    public YuriNakamura()
    {
        super(DATA);

        Initialize(10, 0, 1, 0);
        SetUpgrade(4, 0, 0, 0);

        SetSynergy(Synergies.AngelBeats);

        CardModifierManager.addModifier(this, new AfterLifeMod());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
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

        if (HasSynergy()) {
            GameActions.Bottom.SelectFromHand(name, magicNumber, false)
            .SetOptions(false, false, false)
            .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[1])
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    AbstractCard card = cards.get(0);
                    if (!CardModifierManager.hasModifier(card, AfterLifeMod.ID)) {
                        CardModifierManager.addModifier(card, new AfterLifeMod());
                    }
                    card.flash();
                }
            });
        }
    }
}