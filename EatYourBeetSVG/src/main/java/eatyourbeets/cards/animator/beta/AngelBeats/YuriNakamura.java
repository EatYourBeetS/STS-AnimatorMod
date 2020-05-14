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
import eatyourbeets.misc.CardMods.RetainMod;
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
        String message = cardData.Strings.EXTENDED_DESCRIPTION[0];
        if (HasSynergy())
        {
            message = cardData.Strings.EXTENDED_DESCRIPTION[1];
        }
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
                .SetOptions(false, false, false)
                .SetMessage(message)
                .AddCallback(cards ->
                {
                    AbstractCard card = cards.get(0);
                    if (card != null)
                    {
                        if (HasSynergy()) {
                            if (!CardModifierManager.hasModifier(card, AfterLifeMod.ID)) {
                                CardModifierManager.addModifier(card, new AfterLifeMod());
                            }
                        }
                        if (!card.selfRetain) {
                            CardModifierManager.addModifier(card, new RetainMod());
                        }
                        card.flash();
                    }
                });
    }
}