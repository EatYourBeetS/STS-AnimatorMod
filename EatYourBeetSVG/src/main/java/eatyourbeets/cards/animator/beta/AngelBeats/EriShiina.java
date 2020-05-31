package eatyourbeets.cards.animator.beta.AngelBeats;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_OnSpecialPlay;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.utilities.GameActions;

public class EriShiina extends AnimatorCard_OnSpecialPlay
{
    public static final EYBCardData DATA = AnimatorCard.Register(EriShiina.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal);
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

        Initialize(7, 0, 2, 0);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.AngelBeats);
        SetMartialArtist();
        SetExhaust(true);
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
    }

    @Override
    public void OnSpecialPlay() {
        GameActions.Bottom.CreateThrowingKnives(magicNumber);
    }
}