package pinacolada.cards.pcl.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class YunYun extends PCLCard
{
    public static final PCLCardData DATA = Register(YunYun.class)
            .SetAttack(0, CardRarity.UNCOMMON, PCLAttackType.Electric, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public YunYun()
    {
        super(DATA);

        Initialize(7, 0);
        SetUpgrade(4, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(1, 0, 1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.ChannelOrb(new Lightning());
            PCLActions.Bottom.Flash(this);
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        PCLActions.Bottom.Callback(this::RefreshCost);
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
        PCLActions.Bottom.SFX("ORB_LIGHTNING_EVOKE");

        for (AbstractMonster m1 : PCLGameUtilities.GetEnemies(true))
        {
            PCLActions.Bottom.VFX(new LightningEffect(m1.drawX, m1.drawY));
        }

        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE);
        PCLActions.Bottom.DrawNextTurn(costForTurn);
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