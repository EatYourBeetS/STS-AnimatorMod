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
    public static final int USES = 3;
    public static final int CARD_DRAW = 2;

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
        return FormatDescription(0, USES, CARD_DRAW);
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

        SetCounter(USES);
        SetEnabled(true);
        CombatStats.onAffinitySealed.Subscribe(this);
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        super.DeactivateBattleEffect();

        SetCounter(-1);
        SetEnabled(true);
        CombatStats.onAffinitySealed.Unsubscribe(this);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        if (counter > 0)
        {
            SetEnabled(true);
        }
    }

    @Override
    public void OnAffinitySealed(EYBCard card, boolean manual)
    {
        if (manual && IsEnabled())
        {
            GameActions.Bottom.Draw(CARD_DRAW);
            SetEnabled(false);
            AddCounter(-1);
            flash();
        }
    }
}