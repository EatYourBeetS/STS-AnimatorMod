package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.cards.pcl.special.MamiTomoe_Candeloro;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MamiTomoe extends PCLCard
{
    public static final PCLCardData DATA = Register(MamiTomoe.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Ranged)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new MamiTomoe_Candeloro(), false);
                data.AddPreview(new Curse_GriefSeed(), false);});

    public MamiTomoe()
    {
        super(DATA);

        Initialize(9, 1, 1, 1);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Orange(1);
        SetAffinity_Light(1, 0, 2);
        SetSoul(4, 0, MamiTomoe_Candeloro::new);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        int count = 0;
        String id = Curse_GriefSeed.DATA.ID;
        if (player.drawPile.findCardById(id) != null)
        {
            count += 1;
        }
        if (player.discardPile.findCardById(id) != null)
        {
            count += 1;
        }
        if (player.exhaustPile.findCardById(id) != null)
        {
            count += 1;
        }

        PCLGameUtilities.IncreaseHitCount(this, count, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SFX("ATTACK_HEAVY");
        PCLActions.Bottom.VFX(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.05f * magicNumber);
        PCLActions.Bottom.SFX("ATTACK_FIRE");
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE);
        PCLActions.Bottom.GainBlock(block);

        //GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.MED));

        cooldown.ProgressCooldownAndTrigger(m);

        if (info.TryActivateSemiLimited()) {
            PCLActions.Bottom.CreateGriefSeeds(1);
        }
    }
}
