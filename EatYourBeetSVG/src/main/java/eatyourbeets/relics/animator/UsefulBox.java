package eatyourbeets.relics.animator;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.utilities.GameActions;

public class UsefulBox extends EnchantableRelic implements OnSynergySubscriber, CustomSavable<Integer>
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

        CombatStats.onSynergy.SubscribeOnce(this);
        SetEnabled(true);
    }

    @Override
    public void OnSynergy(AbstractCard c)
    {
        GameActions.Bottom.Draw(1);
        SetEnabled(false);
        flash();
    }
}