package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.Oz;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Fischl extends PCLCard {
    public static final PCLCardData DATA = Register(Fischl.class).SetAttack(0, CardRarity.UNCOMMON, PCLAttackType.Elemental).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Oz(), false));

    public Fischl() {
        super(DATA);

        Initialize(2, 0, 2);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.DARKNESS);
        PCLActions.Bottom.ChannelOrb(rng.randomBoolean(0.5f) ? new Dark() : new Lightning());

        if (info.IsSynergizing && CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.MakeCardInDiscardPile(new Oz()).SetUpgrade(upgraded, false);
        }
    }
}