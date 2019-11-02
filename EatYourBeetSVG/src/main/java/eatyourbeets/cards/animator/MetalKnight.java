package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class MetalKnight extends AnimatorCard_Boost
{
    public static final String ID = Register(MetalKnight.class.getSimpleName(), EYBCardBadge.Discard);

    public MetalKnight()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(12, 0, 3);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        int orbs = AbstractDungeon.player.filledOrbCount();
        if (orbs > 0)
        {
            GameActionsHelper.GainBlock(AbstractDungeon.player, orbs * 2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY));
        GameActionsHelper.AddToBottom(new WaitAction(0.8F));
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);
        GameActionsHelper.ChannelOrb(new Plasma(), true);

        if (ProgressBoost())
        {
            GameActionsHelper.ApplyPower(p, p, new MetallicizePower(p, this.magicNumber), this.magicNumber);
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