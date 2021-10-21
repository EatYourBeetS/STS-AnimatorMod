package eatyourbeets.relics.animator;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class VividPicture extends EnchantableRelic implements OnSynergySubscriber, CustomSavable<Integer>
{
    public static final String ID = CreateFullID(VividPicture.class);

    public VividPicture()
    {
        this(null);
    }

    public VividPicture(Enchantment enchantment)
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, enchantment);
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
    public void OnSynergy(AbstractCard card)
    {
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.GainEnergy(1);
        SetEnabled(false);
        flash();
    }

    @Override
    public void obtain()
    {
        ArrayList<AbstractRelic> relics = player.relics;
        for (int i = 0; i < relics.size(); i++)
        {
            LivingPicture relic = JUtils.SafeCast(relics.get(i), LivingPicture.class);
            if (relic != null)
            {
                ApplyEnchantment(relic.enchantment1);
                instantObtain(player, i, true);
                return;
            }
        }

        super.obtain();
    }

    @Override
    public boolean canSpawn()
    {
        return super.canSpawn() && player.hasRelic(LivingPicture.ID);
    }
}