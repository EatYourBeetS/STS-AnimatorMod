package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class MetalKnight extends AnimatorCard
{
    public static final String ID = Register_Old(MetalKnight.class);

    public MetalKnight()
    {
        super(ID, 3, CardRarity.UNCOMMON, EYBAttackType.Normal);

        Initialize(11, 0, 3);
        SetUpgrade(2, 0, 0);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY));
        GameActions.Bottom.Wait(0.8F);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.ChannelOrb(new Plasma(), true);

        if (magicNumber > 0)
        {
            GameActions.Bottom.GainMetallicize(magicNumber);
            GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.baseMagicNumber = Math.max(0, c.baseMagicNumber - 1));
        }
    }
}