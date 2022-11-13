package eatyourbeets.cards.animatorClassic.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YunYun extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(YunYun.class).SetSeriesFromClassPackage().SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public YunYun()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(4, 0);
        SetScaling(1, 0, 0);


        SetSpellcaster();
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Lightning());
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
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE");

        for (AbstractMonster m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(new LightningEffect(m1.drawX, m1.drawY));
        }

        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE);
    }

    public void RefreshCost()
    {
        int attacks = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (c != this && c.type == CardType.ATTACK)
            {
                attacks += 1;
            }
        }

        CostModifiers.For(this).Set(attacks);
    }
}