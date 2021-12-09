package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Rumia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rumia.class).SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Elemental).SetSeriesFromClassPackage(true);

    public Rumia()
    {
        super(DATA);

        Initialize(4, 0, 3, 7);
        SetUpgrade(1, 0, 0, 2);
        SetAffinity_Dark(1, 0, 2);
        SetAffinity_Blue(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.ChannelOrb(new Dark());
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.ChannelOrb(new Dark());
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.DARK);
        AbstractOrb firstDark = GameUtilities.GetFirstOrb(Dark.ORB_ID);
        if (firstDark != null) {
            int times = secondaryValue;
            while (firstDark.evokeAmount >= magicNumber && times > 0) {
                firstDark.evokeAmount -= magicNumber;
                times -= 1;
                GameActions.Bottom.GainTemporaryHP(1);
            }

        }
    }
}

