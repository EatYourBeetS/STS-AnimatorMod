package eatyourbeets.relics.animator;

import basemod.abstracts.CustomSavable;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.utilities.GameActions;

public class LivingPicture extends EnchantableRelic implements CustomSavable<Integer>, OnEquipUnnamedReignRelicListener, OnAffinitySealedSubscriber
{
    public static final String ID = CreateFullID(LivingPicture.class);
    public static final int USES = 3;
    public static final int CARD_DRAW = 1;

    public LivingPicture()
    {
        this(null);
    }

    public LivingPicture(Enchantment enchantment)
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, enchantment);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, USES, CARD_DRAW);
    }

    @Override
    public void OnEquipUnnamedReignRelic()
    {

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