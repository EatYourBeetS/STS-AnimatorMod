package eatyourbeets.relics.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.effects.ObtainRelicEffect;
import eatyourbeets.interfaces.AllowedUnnamedReignRelic;
import eatyourbeets.relics.UnnamedRelic;
import eatyourbeets.utilities.GameActionsHelper;

public class InfinitePower extends UnnamedRelic implements AllowedUnnamedReignRelic
{
    public static final String ID = CreateFullID(InfinitePower.class.getSimpleName());

    private static final int STRENGTH_AMOUNT = 2;
    private static final int MAX_HP_AMOUNT = 1;
    private static final int HEAL_AMOUNT = 6;

    public InfinitePower()
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + STRENGTH_AMOUNT + DESCRIPTIONS[1] + HEAL_AMOUNT + DESCRIPTIONS[2] + MAX_HP_AMOUNT + DESCRIPTIONS[3];
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        AbstractPlayer p = AbstractDungeon.player;
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, STRENGTH_AMOUNT), STRENGTH_AMOUNT);
        p.decreaseMaxHealth(MAX_HP_AMOUNT);
        p.heal(HEAL_AMOUNT);

        this.flash();
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        AbstractDungeon.effectsQueue.add(new ObtainRelicEffect(this));
    }

    @Override
    public void OnEquipUnnamedReignRelic()
    {

    }
}