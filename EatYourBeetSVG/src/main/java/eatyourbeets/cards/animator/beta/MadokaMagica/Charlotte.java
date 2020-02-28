package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;

public class Charlotte extends AnimatorCard implements Spellcaster {
    public static final EYBCardData DATA = Register(Charlotte.class).SetAttack(4, CardRarity.SPECIAL, EYBAttackType.Normal);

    public Charlotte() {
        super(DATA);

        Initialize(8, 0, 0);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int curseCount = player.hand.getCardsOfType(CardType.CURSE).size();
        int damageCount = damage;

        for (int i=0; i<curseCount; i++)
        {
            damageCount *= 2;
        }

        GameActions.Bottom.DealDamage(p, m, damageCount, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE);
    }
}