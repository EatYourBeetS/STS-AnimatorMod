package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RoyMustang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoyMustang.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public RoyMustang()
    {
        super(DATA);

        Initialize(7, 0, 4);
        SetUpgrade(4, 0, 0);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);

        int max = p.orbs.size();
        int i = 0;

        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            if (i < max)
            {
                GameActions.Bottom.ChannelOrb(new Fire(), true);
            }

            i += 1;
        }

        if (HasSynergy())
        {
            GameActions.Bottom.VFX(new FlameBarrierEffect(p.hb.cX, p.hb.cY), 0.5F);
            GameActions.Bottom.StackPower(new FlameBarrierPower(p, this.magicNumber));
        }
    }
}