package eatyourbeets.relics.animator;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class VividPicture extends EnchantableRelic implements CustomSavable<Integer>, OnEquipUnnamedReignRelicListener, OnAffinitySealedSubscriber
{
    public static final String ID = CreateFullID(VividPicture.class);
    public static final int INSPIRATION = 2;
    public static final int HP_RECOVER = 2;

    public VividPicture()
    {
        this(null);
    }

    public VividPicture(Enchantment enchantment)
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, enchantment);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, INSPIRATION, HP_RECOVER);
    }

    @Override
    public void OnEquipUnnamedReignRelic()
    {
        final int index = player.relics.indexOf(this);
        final LivingPicture relic = new LivingPicture(enchantment);
        relic.currentX = currentX;
        relic.currentY = currentY;
        relic.targetX = targetX;
        relic.targetY = targetY;
        relic.instantObtain(player, index >= 0 ? index : player.relics.size(), true);
        relic.OnEquipUnnamedReignRelic();
        player.relics.remove(this);
    }

    @Override
    public void obtain()
    {
        final ArrayList<AbstractRelic> relics = player.relics;
        for (int i = 0; i < relics.size(); i++)
        {
            LivingPicture relic = JUtils.SafeCast(relics.get(i), LivingPicture.class);
            if (relic != null)
            {
                ApplyEnchantment(relic.enchantment);
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

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        CombatStats.onAffinitySealed.Subscribe(this);
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        super.DeactivateBattleEffect();

        CombatStats.onAffinitySealed.Unsubscribe(this);
    }

    @Override
    public void OnAffinitySealed(EYBCard card, boolean manual)
    {
        if (IsEnabled() && player.hand.contains(card))
        {
            GameActions.Bottom.GainInspiration(INSPIRATION);
            GameActions.Bottom.GainTemporaryHP(HP_RECOVER);
            flash();
        }
    }
}