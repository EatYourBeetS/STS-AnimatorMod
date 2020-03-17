package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.events.shrines.Transmogrifier;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Vanir extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vanir.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public Vanir()
    {
        super(DATA);

        Initialize(12, 0, 3);
        SetUpgrade(1, 0, -1);
        SetScaling(1, 0, 0);

        SetSynergy(Synergies.Konosuba, true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.SelectFromPile(name, 1, player.drawPile)
        .SetOptions(false, true)
        .SetMessage(Transmogrifier.OPTIONS[2])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Top.ReplaceCard(cards.get(0).uuid, makeCopy()).SetUpgrade(upgraded);
            }
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SMASH);
        GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - c.magicNumber));
    }
}