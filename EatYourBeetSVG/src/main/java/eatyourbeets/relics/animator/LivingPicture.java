package eatyourbeets.relics.animator;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.relics.EnchantableRelic;

public class LivingPicture extends EnchantableRelic implements OnSynergySubscriber, CustomSavable<Integer>
{
    public static final String ID = CreateFullID(LivingPicture.class);

    public LivingPicture()
    {
        this(null);
    }

    public LivingPicture(Enchantment enchantment)
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, enchantment);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
    }

   /* @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        CombatStats.onSynergy.SubscribeOnce(this);
        SetEnabled(true);
    }*/

    @Override
    public void OnSynergy(AbstractCard c)
    {
        /*GameActions.Bottom.Draw(1);
        SetEnabled(false);
        flash();*/
    }
}