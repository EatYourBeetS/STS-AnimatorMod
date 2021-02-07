package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.CostModifier;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YunYun extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YunYun.class).SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    private CostModifier costModifier = null;

    public YunYun()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(4, 0);
        SetScaling(1, 0, 0);

        SetSynergy(Synergies.Konosuba);
        SetSpellcaster();
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Lightning(), true);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        GameActions.Bottom.Callback(this::RefreshCost);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        YunYun copy = (YunYun) super.makeStatEquivalentCopy();
        if (costModifier != null)
        {
            copy.costModifier = CostModifier.Initialize(copy);
            copy.costModifier.baseAmount = costModifier.baseAmount;
        }

        return copy;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE");

        for (AbstractMonster m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(new LightningEffect(m1.drawX, m1.drawY));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);
    }

    public void RefreshCost()
    {
        if (costModifier == null)
        {
            costModifier = CostModifier.Initialize(this);
        }

        int attacks = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (c != this && c.type == CardType.ATTACK)
            {
                attacks += 1;
            }
        }

        costModifier.SetModifier(attacks);
    }
}