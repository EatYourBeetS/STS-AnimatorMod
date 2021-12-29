package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Rumia extends PCLCard
{
    public static final PCLCardData DATA = Register(Rumia.class).SetAttack(0, CardRarity.UNCOMMON, PCLAttackType.Dark).SetSeriesFromClassPackage(true);

    public Rumia()
    {
        super(DATA);

        Initialize(3, 0, 3, 7);
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
            PCLActions.Bottom.ChannelOrb(new Dark());
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.ChannelOrb(new Dark());
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.DARK);
        AbstractOrb firstDark = PCLGameUtilities.GetFirstOrb(Dark.ORB_ID);
        if (firstDark != null) {
            int times = secondaryValue;
            while (firstDark.evokeAmount >= magicNumber && times > 0) {
                firstDark.evokeAmount -= magicNumber;
                times -= 1;
                PCLActions.Bottom.GainTemporaryHP(2);
            }

        }
    }
}

