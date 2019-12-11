package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard_Boost;
import eatyourbeets.cards.base.Synergies;

public class MetalKnight extends AnimatorCard_Boost
{
    public static final String ID = Register(MetalKnight.class.getSimpleName(), EYBCardBadge.Discard);

    public MetalKnight()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(12, 0, 3);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        int orbs = AbstractDungeon.player.filledOrbCount();
        if (orbs > 0)
        {
            GameActions.Bottom.GainBlock(orbs * 2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY));
        GameActions.Bottom.Wait(0.8F);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.ChannelOrb(new Plasma(), true);

        if (ProgressBoost())
        {
            GameActions.Bottom.GainMetallicize(magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }

    @Override
    protected int GetBaseBoost()
    {
        return 1;
    }
}