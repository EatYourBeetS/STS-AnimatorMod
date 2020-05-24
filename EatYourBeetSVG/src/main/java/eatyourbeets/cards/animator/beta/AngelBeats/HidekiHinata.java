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
import eatyourbeets.utilities.GameActions;

public class HidekiHinata extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HidekiHinata.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.ALL);

    public HidekiHinata()
    {
        super(DATA);

        Initialize(6, 0, 2, 2);
        SetUpgrade(3, 0, 0, 0);

        SetSynergy(Synergies.AngelBeats);
        CardModifierManager.addModifier(this, new AfterLifeMod());
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (HasSynergy() ? magicNumber : 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.Reload(name, cards ->
        {
            for (int i = 0; i < cards.size(); i++)
            {
                GameActions.Bottom.GainBlock(secondaryValue);
            }
        });
    }
}