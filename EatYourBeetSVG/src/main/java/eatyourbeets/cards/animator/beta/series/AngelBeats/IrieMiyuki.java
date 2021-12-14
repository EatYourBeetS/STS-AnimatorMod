package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class IrieMiyuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IrieMiyuki.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public IrieMiyuki()
    {
        super(DATA);

        Initialize(0, 3, 2, 5);
        SetUpgrade(0,0,0,0);
        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0, 1);

        SetHealing(true);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this).SetText(GetXValue(), Settings.CREAM_COLOR);
    }

    @Override
    public int GetXValue() {
        return magicNumber + CombatStats.CardsExhaustedThisTurn().size() * magicNumber;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainWisdom(magicNumber);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryHP(GetXValue());

        if (info.IsSynergizing && CombatStats.Affinities.GetLastAffinitySynergy() == Affinity.Blue && info.TryActivateLimited()) {
            GameActions.Bottom.Heal(secondaryValue);
        }
    }
}