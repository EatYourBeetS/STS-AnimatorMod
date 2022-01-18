package pinacolada.relics.pcl;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.pcl.enchantments.Enchantment;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.PCLEnchantableRelic;
import pinacolada.utilities.PCLActions;

public class UsefulBox extends PCLEnchantableRelic implements OnSynergySubscriber, CustomSavable<Integer>
{
    public static final String ID = CreateFullID(UsefulBox.class);

    public UsefulBox()
    {
        this(null);
    }

    public UsefulBox(Enchantment enchantment)
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, enchantment);
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
    public void OnSynergy(AbstractCard c)
    {
        PCLActions.Bottom.Draw(1);
        SetEnabled(false);
        flash();
    }
}