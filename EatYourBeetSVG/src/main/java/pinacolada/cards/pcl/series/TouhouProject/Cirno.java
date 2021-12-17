package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Cirno extends PCLCard
{
    public static final PCLCardData DATA = Register(Cirno.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Elemental, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Cirno()
    {
        super(DATA);

        Initialize(3, 0, 3, 2);
        SetUpgrade(1, 0, 1);

        SetAffinity_Blue(1, 0, 1);

        SetEthereal(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents())
        {
            intent.AddFreezing();
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.ChannelOrb(new Frost());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Callback(() ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + 5;

            for (int i = 0; i < frostCount; i++)
            {
                PCLGameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }

            PCLActions.Top.Wait(0.3f);
        });

        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.BLUNT_LIGHT).forEach(d -> d
        .SetVFX(true, false)
        .SetDamageEffect((c, __) -> {
            PCLActions.Bottom.ReduceStrength(c, secondaryValue, true);
            PCLActions.Bottom.ApplyFreezing(player, c, magicNumber).ShowEffect(true, true);
        }));
    }
}

