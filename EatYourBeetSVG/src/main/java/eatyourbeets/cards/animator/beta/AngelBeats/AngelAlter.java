package eatyourbeets.cards.animator.beta.AngelBeats;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.utilities.GameActions;

public class AngelAlter extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(AngelAlter.class).SetAttack(4, CardRarity.SPECIAL, EYBAttackType.Piercing).SetColor(CardColor.COLORLESS);

    public AngelAlter()
    {
        super(DATA);

        Initialize(30, 0, 1, 0);
        SetUpgrade(15, 0, 0, 0);
        SetScaling(0, 3, 3);

        SetExhaust(true);
        SetSynergy(Synergies.AngelBeats);
        CardModifierManager.addModifier(this, new AfterLifeMod());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        GameActions.Bottom.GainForce(magicNumber, true);
        GameActions.Bottom.GainAgility(magicNumber, true);

        GameActions.Bottom.MakeCardInDrawPile(this).SetUpgrade(false, true);
        GameActions.Bottom.MakeCardInDiscardPile(this).SetUpgrade(false, true);
    }
}