package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MetalKnight extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MetalKnight.class).SetAttack(3, CardRarity.UNCOMMON);

    public MetalKnight()
    {
        super(DATA);

        Initialize(11, 0, 3);
        SetUpgrade(2, 0, 0);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.OnePunchMan);
        SetAffinity(2, 0, 2, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY));
        GameActions.Bottom.Wait(0.8f);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.ChannelOrb(new Plasma());

        if (magicNumber > 0)
        {
            GameActions.Bottom.GainMetallicize(magicNumber);
            GameActions.Bottom.ModifyAllInstances(uuid, c -> GameUtilities.DecreaseMagicNumber(c, 1, false));
        }
    }
}