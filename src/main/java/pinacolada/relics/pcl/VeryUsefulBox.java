package pinacolada.relics.pcl;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.pcl.enchantments.Enchantment;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.PCLEnchantableRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class VeryUsefulBox extends PCLEnchantableRelic implements OnSynergySubscriber, CustomSavable<Integer>
{
    public static final String ID = CreateFullID(VeryUsefulBox.class);

    public VeryUsefulBox()
    {
        this(null);
    }

    public VeryUsefulBox(Enchantment enchantment)
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, 2, true, enchantment);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        PCLCombatStats.onSynergy.SubscribeOnce(this);
        SetEnabled(true);
    }

    @Override
    public void OnSynergy(AbstractCard card)
    {
        PCLActions.Bottom.Draw(1);
        SetEnabled(false);
        flash();
    }

    @Override
    public void obtain()
    {
        ArrayList<AbstractRelic> relics = player.relics;
        for (int i = 0; i < relics.size(); i++)
        {
            UsefulBox relic = PCLJUtils.SafeCast(relics.get(i), UsefulBox.class);
            if (relic != null)
            {
                ApplyEnchantment(relic.enchantment);
                instantObtain(player, i, true);
                setCounter(relic.counter);
                return;
            }
        }

        super.obtain();
    }

    @Override
    public boolean canSpawn()
    {
        return super.canSpawn() && player.hasRelic(UsefulBox.ID);
    }
}