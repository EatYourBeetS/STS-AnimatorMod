package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Amber extends AnimatorCard {
    public static final EYBCardData DATA = Register(Amber.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged);

    public Amber() {
        super(DATA);

        Initialize(3, 3, 2);
        SetUpgrade(1, 1, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.GenshinImpact);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            GameActions.Bottom.StackPower(p, new LockOnPower(m, magicNumber));
            GameActions.Bottom.ChannelOrb(new Fire());
        }

        if (HasSynergy() && !CombatStats.HasActivatedSemiLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Fire());
        }
    }
}