package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

public class Enchantment2 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment2.class);
    public static final int INDEX = 2;
    public Affinity currentAffinity;

    public Enchantment2()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 7) {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 7;
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        currentAffinity = GetAffinity();
        if (currentAffinity == null) {
            currentAffinity = GameUtilities.GetRandomElement(Affinity.Basic());
        }
        CombatStats.Affinities.BonusAffinities.Add(currentAffinity, magicNumber);
    }

    @Override
    public void AtEndOfTurnEffect(boolean isPlayer) {
        if (currentAffinity != null) {
            CombatStats.Affinities.BonusAffinities.Add(currentAffinity, -magicNumber);
            currentAffinity = null;
        }
    }

    public Affinity GetAffinity()
    {
        switch (auxiliaryData.form)
        {
            case 1: return Affinity.Fire;
            case 2: return Affinity.Air;
            case 3: return Affinity.Water;
            case 4: return Affinity.Earth;
            case 5: return Affinity.Light;
            case 6: return Affinity.Dark;
            default: return null;
        }
    }
}