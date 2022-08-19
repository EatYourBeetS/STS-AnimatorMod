package eatyourbeets.cards.animatorClassic.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.FlamingWeaponPower;
import eatyourbeets.utilities.GameActions;

public class Shizu extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Shizu.class).SetAttack(2, CardRarity.RARE);

    public Shizu()
    {
        super(DATA);

        Initialize(13, 0);
        SetUpgrade(3, 0);
        SetScaling(0, 2, 0);

        SetExhaust(true);
        SetSeries(CardSeries.TenseiSlime);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new FlamingWeaponPower(p, 1));
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.MakeCardInDiscardPile(new Burn());
    }
}