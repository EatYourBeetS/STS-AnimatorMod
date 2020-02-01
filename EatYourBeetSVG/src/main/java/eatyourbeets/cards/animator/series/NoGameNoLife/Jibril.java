package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Jibril extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Jibril.class);

    public Jibril()
    {
        super(ID, 2, CardRarity.COMMON, EYBAttackType.Elemental, true);

        Initialize(8, 0);
        SetUpgrade(4, 0);
        SetScaling(2, 0, 0);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Add(new TriggerPassiveAction(1));
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);
    }
}