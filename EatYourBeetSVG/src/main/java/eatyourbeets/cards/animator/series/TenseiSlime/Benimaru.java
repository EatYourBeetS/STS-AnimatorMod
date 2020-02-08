package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Benimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Benimaru.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.Normal);

    public Benimaru()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(1, 0, 1);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Fire(), true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE)
        .SetDamageEffect(enemy -> GameActions.Top.StackPower(player, new BurningPower(player, enemy, magicNumber)));
    }
}