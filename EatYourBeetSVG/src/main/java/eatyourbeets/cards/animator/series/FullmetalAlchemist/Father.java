package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.common.GenesisPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Father extends AnimatorCard implements OnAddToDeckListener, OnAddingToCardRewardListener
{
    private static final AbstractRelic relic = new PhilosopherStone();
    private static final EYBCardTooltip tooltip = new EYBCardTooltip(relic.name, relic.description);

    public static final EYBCardData DATA = Register(Father.class)
            .SetSkill(4, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();

    public Father()
    {
        super(DATA);

        Initialize(0, 0, 0, 46);
        SetCostUpgrade(-1);

        SetAffinity_Dark(2);
        SetAffinity_Silver(2);

        SetUnique(true, false);
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
        final boolean add = !GR.Animator.Dungeon.BannedCards.contains(cardID);
        GR.Animator.Dungeon.Ban(cardData.ID);
        //AbstractDungeon.bossRelicPool.remove(relic.relicId);
        return add;
    }

    @Override
    public boolean ShouldCancel()
    {
        return GR.Animator.Dungeon.BannedCards.contains(cardID) || player == null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (GameUtilities.HasRelic(relic.relicId)) {
            GameActions.Bottom.StackPower(new GenesisPower(player, 1));
            GameActions.Bottom.ApplyPower(TargetHelper.Enemies(), PowerHelper.Strength, 1);
        }
        else {
            p.decreaseMaxHealth((int)Math.ceil(p.maxHealth * (secondaryValue / 100f)));
            GameActions.Bottom.VFX(new OfferingEffect(), 0.5f);
            GameActions.Bottom.Callback(() -> GameEffects.Queue.SpawnRelic(relic.makeCopy(), current_x, current_y));
            //AbstractDungeon.bossRelicPool.remove(relic.relicId);
        }
        p.energy.energy += 1;


    }
}