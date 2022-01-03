package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Father extends PCLCard implements OnAddToDeckListener, OnAddingToCardRewardListener
{
    private static final AbstractRelic relic = new PhilosopherStone();
    private static final PCLCardTooltip tooltip = new PCLCardTooltip(relic.name, relic.description);

    public static final PCLCardData DATA = Register(Father.class)
            .SetSkill(4, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();

    public Father()
    {
        super(DATA);

        Initialize(0, 0, 10, 46);
        SetCostUpgrade(-1);

        SetAffinity_Dark(1);
        SetAffinity_Silver(1);

        SetUnique(true, 1);
        SetPurge(true, false);
        SetHealing(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltip.id = cardID + ":" + tooltip.title;
            tooltips.add(tooltip);
        }
    }

    @Override
    public boolean OnAddToDeck()
    {
        final boolean add = !GR.PCL.Dungeon.BannedCards.contains(cardID);
        GR.PCL.Dungeon.Ban(cardData.ID);
        //AbstractDungeon.bossRelicPool.remove(relic.relicId);
        return add;
    }

    @Override
    public boolean ShouldCancel()
    {
        return GR.PCL.Dungeon.BannedCards.contains(cardID) || player == null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (PCLGameUtilities.HasRelic(relic.relicId)) {
            PCLActions.Bottom.GainTechnic(magicNumber);
            PCLActions.Bottom.ApplyPower(TargetHelper.Enemies(), PCLPowerHelper.Strength, 1);
        }
        else {
            p.decreaseMaxHealth((int)Math.ceil(p.maxHealth * (secondaryValue / 100f)));
            PCLActions.Bottom.VFX(new OfferingEffect(), 0.5f);
            PCLActions.Bottom.Callback(() -> PCLGameEffects.Queue.SpawnRelic(relic.makeCopy(), current_x, current_y));
            //AbstractDungeon.bossRelicPool.remove(relic.relicId);
        }
        p.energy.energy += 1;


    }
}