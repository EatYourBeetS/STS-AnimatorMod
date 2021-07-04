package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class BarbaraPegg extends AnimatorCard
{
    public static final EYBCardData DATA = Register(BarbaraPegg.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    private static final int UNIQUE_THRESHOLD = 4;

    public BarbaraPegg()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
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
        if (GameUtilities.GetUniqueOrbsCount() >= UNIQUE_THRESHOLD && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.Heal(magicNumber);
        }

    }

    private int GetTempHP() {
        return magicNumber + GameUtilities.GetUniqueOrbsCount();
    }
}