package pinacolada.cards.pcl.series.TenseiSlime;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Benimaru extends PCLCard
{
    public static final PCLCardData DATA = Register(Benimaru.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Fire, PCLCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public Benimaru()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Blue(1);

        SetAffinityRequirement(PCLAffinity.Red, 4);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.ChannelOrb(new Fire());
            PCLActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE).forEach(d -> d
        .SetDamageEffect(e -> PCLGameEffects.List.Add(VFX.Fireball(player.hb, e.hb)).SetColor(Color.RED, Color.ORANGE).SetRealtime(true).duration)
        .AddCallback(m, (enemy, __) -> PCLActions.Top.ApplyBurning(player, enemy, TrySpendAffinity(PCLAffinity.Red) ? magicNumber * 2 : magicNumber))
        .SetRealtime(true));
    }
}