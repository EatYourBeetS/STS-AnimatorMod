package eatyourbeets.cards.animator.series.TenseiSlime;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.FlamingWeaponPower;
import eatyourbeets.utilities.GameActions;

public class Shizu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shizu.class)
            .SetAttack(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Shizu()
    {
        super(DATA);

        Initialize(13, 0);
        SetUpgrade(3, 0);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Red(1);
        SetAffinity_Light(2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new FlamingWeaponPower(p, 1));
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.MakeCardInDiscardPile(new Burn());
    }
}