package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.animator.beta.special.Oz;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Fischl extends AnimatorCard {
    public static final EYBCardData DATA = Register(Fischl.class).SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Elemental).SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Oz(), true);
    }

    public Fischl() {
        super(DATA);

        Initialize(3, 0, 0);
        SetUpgrade(3, 0, 0);
        SetAffinity_Blue(1);

        SetExhaust(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.LIGHTNING);
        GameActions.Bottom.ChannelOrb(rng.randomBoolean(0.5f) ? new Dark() : new Lightning());

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.MakeCardInDiscardPile(new Oz()).SetUpgrade(upgraded, false);
        }
    }
}