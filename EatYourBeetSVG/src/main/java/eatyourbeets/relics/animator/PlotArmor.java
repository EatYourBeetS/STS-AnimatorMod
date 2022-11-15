package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnModifyDamageLastSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameUtilities;

public class PlotArmor extends AnimatorRelic implements OnModifyDamageLastSubscriber
{
    public static final String ID = CreateFullID(PlotArmor.class);
    public static final int MINIMUM_ASCENSION = 7;

    public PlotArmor()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);

        SetCounter(AbstractDungeon.ascensionLevel);
        updateDescription();
    }

    @Override
    public boolean canSpawn()
    {
        return super.canSpawn() && GameUtilities.GetActualAscensionLevel() >= MINIMUM_ASCENSION;
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, MINIMUM_ASCENSION);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        SetCounter(GameUtilities.GetActualAscensionLevel());
    }

    @Override
    protected void RefreshBattleEffect(boolean enabled)
    {
        super.RefreshBattleEffect(enabled);

        CombatStats.onModifyDamageLast.ToggleSubscription(this, enabled);
    }

    @Override
    public int OnModifyDamageLast(AbstractCreature target, DamageInfo info, int damage)
    {
        if (target != player)
        {
            return damage;
        }

        final int hp = GameUtilities.GetHP(target, true, false);
        final int excessDamage = 1 + damage - hp;
        if (hp > 0 && excessDamage > 0 && counter >= excessDamage)
        {
            damage -= excessDamage;
            AddCounter(-excessDamage);
            target.decreaseMaxHealth(excessDamage);
            flash();
        }

        if (counter <= 0)
        {
            SetCounter(0);
            SetEnabled(false);
        }

        return damage;
    }
}