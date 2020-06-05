package eatyourbeets.cards.animator.beta.AngelBeats;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Noda extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noda.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);

    public Noda()
    {
        super(DATA);

        Initialize(7, 0, 1, 1);
        SetUpgrade(1, 0, 0, 0);

        SetSynergy(Synergies.AngelBeats);
        SetExhaust(true);
        CardModifierManager.addModifier(this, new AfterLifeMod());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainForce(secondaryValue, upgraded);
        }
    }
}