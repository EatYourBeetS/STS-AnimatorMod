package eatyourbeets.cards.animator.series.Konosuba;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.events.shrines.Transmogrifier;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Vanir extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vanir.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public Vanir()
    {
        super(DATA);

        Initialize(12, 0, 3);
        SetUpgrade(1, 0, -1);

        SetAffinity_Blue(0, 0, 1);
        SetAffinity_Star(1, 1, 0);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - c.magicNumber));
    }
}