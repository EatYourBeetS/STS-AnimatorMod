package eatyourbeets.cards.animatorClassic.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.Melzalgald_1;
import eatyourbeets.cards.animatorClassic.special.Melzalgald_2;
import eatyourbeets.cards.animatorClassic.special.Melzalgald_3;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Melzalgald extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Melzalgald.class).SetAttack(3, CardRarity.UNCOMMON);
    static
    {
        DATA.AddPreview(new Melzalgald_1(), true);
        DATA.AddPreview(new Melzalgald_2(), true);
        DATA.AddPreview(new Melzalgald_3(), true);
    }

    public Melzalgald()
    {
        super(DATA);

        Initialize(21, 0);
        SetScaling(2, 2, 2);

        SetExhaust(true);
        SetSeries(CardSeries.OnePunchMan);
        SetShapeshifter();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        GameActions.Bottom.MakeCardInHand(new Melzalgald_1()).SetUpgrade(upgraded, false).AddCallback(GameUtilities::Retain);
        GameActions.Bottom.MakeCardInHand(new Melzalgald_2()).SetUpgrade(upgraded, false).AddCallback(GameUtilities::Retain);
        GameActions.Bottom.MakeCardInHand(new Melzalgald_3()).SetUpgrade(upgraded, false).AddCallback(GameUtilities::Retain);
    }
}