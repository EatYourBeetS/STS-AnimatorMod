package eatyourbeets.ui.unnamed;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.characters.UnnamedCharacter;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.interfaces.subscribers.OnVoidTurnStartSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.JavaUtilities;

public class Void extends CardGroup implements OnStartOfTurnPostDrawSubscriber
{
    private final VoidEnergyOrb energyOrb = new VoidEnergyOrb(this);

    public Void()
    {
        super(CardGroupType.UNSPECIFIED);
    }

    public void Initialize(boolean firstTime)
    {
        if (!firstTime || AbstractDungeon.player instanceof UnnamedCharacter)
        {
//          VoidEnergyPatches.SetOrb(energyOrb);
            CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        }
//        else
//        {
//            VoidEnergyPatches.SetOrb(null);
//        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        energyOrb.refill();

        for (AbstractCard c : group)
        {
            OnVoidTurnStartSubscriber card = JavaUtilities.SafeCast(c, OnVoidTurnStartSubscriber.class);
            if (card != null)
            {
                card.OnVoidTurnStart();
            }
        }
    }

    public boolean CanUse(AbstractCard card)
    {
        UnnamedCard c = JavaUtilities.SafeCast(card, UnnamedCard.class);
        if (c != null)
        {
            return CanUse(c);
        }

        return true;
    }

    public void UseMastery(AbstractCard card)
    {
        UnnamedCard c = JavaUtilities.SafeCast(card, UnnamedCard.class);
        if (c != null)
        {
            UseMastery(c);
        }
    }

    public boolean CanUse(UnnamedCard card)
    {
        return card.masteryCost <= energyOrb.currentEnergy;
    }

    public void UseMastery(UnnamedCard card)
    {
        energyOrb.useEnergy(card.masteryCost);
    }
}

/*

public class VoidEnergyPatches
{
    protected static VoidEnergyOrb orb = null;

    public static void SetOrb(VoidEnergyOrb orb)
    {
        VoidEnergyPatches.orb = orb;
    }

    @SpirePatch(clz = EnergyPanel.class, method = "update")
    public static class EnergyPanelPatches_Update
    {
        @SpirePostfixPatch
        public static void Method(EnergyPanel __instance)
        {
            if (orb != null)
            {
                orb.update(__instance);
            }
        }
    }

    @SpirePatch(clz = EnergyPanel.class, method = "render")
    public static class EnergyPanelPatches_Render
    {
        @SpirePostfixPatch
        public static void Method(EnergyPanel __instance, SpriteBatch sb)
        {
            if (orb != null)
            {
                orb.render(__instance, sb);
            }
        }
    }
}

*/