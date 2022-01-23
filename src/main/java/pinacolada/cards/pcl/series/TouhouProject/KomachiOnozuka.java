package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.animator.ShinigamiFerry;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KomachiOnozuka extends PCLCard
{
    public static final PCLCardData DATA = Register(KomachiOnozuka.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Brutal).SetSeriesFromClassPackage();

    private static final AbstractRelic relicReward = new ShinigamiFerry();
    private static final PCLCardTooltip tooltip = new PCLCardTooltip(relicReward.name, relicReward.description);

    public KomachiOnozuka()
    {
        super(DATA);

        Initialize(10, 0, 2, 2);
        SetUpgrade(2, 0, 1, 0);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(tooltip);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY).forEach(d -> d
        .AddCallback(enemy ->
        {
            if (PCLGameUtilities.GetPowerAmount(enemy, VulnerablePower.POWER_ID) > 0) {
                PCLActions.Bottom.AddAffinity(PCLAffinity.Light, secondaryValue);
            }
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if ((room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
            && PCLGameUtilities.IsFatal(enemy, false)
            && CombatStats.TryActivateLimited(cardID))
            {
                ObtainReward();
            }
        }));
        PCLActions.Bottom.ApplyVulnerable(p, m, magicNumber);
    }

    public void ObtainReward()
    {
        if (UnnamedReignRelic.IsEquipped())
        {
            PCLGameUtilities.GetCurrentRoom(true).addRelicToRewards(new AncientMedallion());
        }
        else
        {
            PCLGameUtilities.GetCurrentRoom(true).addRelicToRewards(relicReward.makeCopy());
        }
    }
}

