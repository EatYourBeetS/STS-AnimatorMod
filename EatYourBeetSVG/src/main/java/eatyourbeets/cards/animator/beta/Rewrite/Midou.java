package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Midou extends AnimatorCard {
    public static final EYBCardData DATA = Register(Midou.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Elemental);

    public Midou() {
        super(DATA);

        Initialize(3, 0, 1, 1);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);

        GameActions.Bottom.ApplyBurning(p,m,magicNumber);

        boolean exitIntellect = (player.stance.ID.equals(IntellectStance.STANCE_ID));
        GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);

        if (exitIntellect && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Fire(), true);
        }
    }
}