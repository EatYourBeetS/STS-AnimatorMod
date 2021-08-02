package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Strike extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Strike.class).SetAttack(1, CardRarity.BASIC);

    public Strike()
    {
        super(DATA);

        Initialize(6, 0);
        SetUpgrade(3, 0);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.ShootingStars(player.hb, c.hb.height)).duration);
    }
}