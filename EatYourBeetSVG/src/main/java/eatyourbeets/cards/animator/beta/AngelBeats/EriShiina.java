package eatyourbeets.cards.animator.beta.AngelBeats;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.utilities.GameActions;

public class EriShiina extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EriShiina.class).SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Normal);
    static
    {
        for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
        {
            DATA.AddPreview(knife, false);
        }
    }
    public EriShiina()
    {
        super(DATA);

        Initialize(8, 0, 0, 0);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(0, 2, 0);

        SetSynergy(Synergies.AngelBeats);
        SetMartialArtist();
        CardModifierManager.addModifier(this, new AfterLifeMod());
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
        //The Throwing Knife stuff is hardcoded into AfterlifeMod >_>
    }
}