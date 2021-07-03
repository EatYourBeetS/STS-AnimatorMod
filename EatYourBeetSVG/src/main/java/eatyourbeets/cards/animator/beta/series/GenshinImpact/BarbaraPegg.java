package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.Set;
import java.util.stream.Collectors;

public class BarbaraPegg extends AnimatorCard
{
    public static final EYBCardData DATA = Register(BarbaraPegg.class).SetSkill(1, CardRarity.UNCOMMON);
    private static final int UNIQUE_THRESHOLD = 4;

    public BarbaraPegg()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetHealing(true);
        SetSpellcaster();
        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this).SetText(String.valueOf(this.GetTempHP()), Settings.CREAM_COLOR);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new RainbowCardEffect());
        GameActions.Bottom.GainTemporaryHP(this.GetTempHP());
        if (GameUtilities.GetUniqueOrbsCount() >= 4 && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.Heal(secondaryValue);
        }

    }

    private int GetTempHP() {
        return magicNumber * (GameUtilities.GetUniqueOrbsCount() + 1);
    }
}