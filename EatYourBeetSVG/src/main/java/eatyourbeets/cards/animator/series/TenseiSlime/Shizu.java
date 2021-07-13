package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.animator.FlamingWeaponPower;
import eatyourbeets.utilities.GameActions;

public class Shizu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shizu.class).SetAttack(2, CardRarity.RARE);

    public Shizu()
    {
        super(DATA);

        Initialize(13, 0);
        SetUpgrade(3, 0);
        SetScaling(0, 2, 0);

        SetExhaust(true);
        SetSeries(CardSeries.TenSura);
        SetAffinity(1, 2, 0, 2, 0);
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
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.MakeCardInDiscardPile(new Burn());
    }
}