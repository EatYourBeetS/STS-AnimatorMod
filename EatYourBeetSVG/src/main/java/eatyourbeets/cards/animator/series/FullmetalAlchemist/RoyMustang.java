package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.Fire;
import eatyourbeets.utilities.GameUtilities;

public class RoyMustang extends AnimatorCard
{
    public static final String ID = Register(RoyMustang.class.getSimpleName(), EYBCardBadge.Synergy);

    public RoyMustang()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(6,0, 4);

        SetEvokeOrbCount(1);
        SetMultiDamage(true);
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

        if (HasActiveSynergy())
        {
            GameActions.Bottom.VFX(new FlameBarrierEffect(p.hb.cX, p.hb.cY), 0.5F);
            GameActions.Bottom.StackPower(new FlameBarrierPower(p, this.magicNumber));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}