package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.animator.beta.special.Oz;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Fischl extends AnimatorCard {
    public static final EYBCardData DATA = Register(Fischl.class).SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Elemental).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Oz(), false));

    public Fischl() {
        super(DATA);

        Initialize(2, 0, 1);
        SetUpgrade(1, 0, 1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.DARKNESS);
        GameActions.Bottom.ChannelOrb(rng.randomBoolean(0.5f) ? new Dark() : new Lightning());
        GameActions.Bottom.GainCorruption(magicNumber);

        if (isSynergizing && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.MakeCardInDiscardPile(new Oz()).SetUpgrade(upgraded, false);
        }
    }
}