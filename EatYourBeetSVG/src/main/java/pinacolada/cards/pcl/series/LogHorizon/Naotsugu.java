package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Naotsugu extends PCLCard
{
    public static final PCLCardData DATA = Register(Naotsugu.class)
            .SetAttack(3, CardRarity.COMMON, PCLAttackType.Normal)
            .SetSeriesFromClassPackage();

    public Naotsugu()
    {
        super(DATA);

        Initialize(7, 5);
        SetUpgrade(3, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(PCLAffinity.Red, 10);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY)
                .forEach(d -> d.AddCallback(e ->
        {
            AbstractCard best = null;
            int maxBlock = e.lastDamageTaken;
            boolean playAll = TrySpendAffinity(PCLAffinity.Red);
            for (AbstractCard c : player.hand.group)
            {
                if (c.block > 0 && c.block < maxBlock)
                {
                    if (playAll)
                    {
                        PCLActions.Top.PlayCard(c, player.hand, (AbstractMonster) e)
                        .SetDuration(Settings.ACTION_DUR_MED, true);
                    }
                    else if (best == null || best.block > c.block)
                    {
                        best = c;
                    }
                }
            }

            if (best != null)
            {
                PCLActions.Top.PlayCard(best, player.hand, (AbstractMonster) e);
            }
        }));
    }
}