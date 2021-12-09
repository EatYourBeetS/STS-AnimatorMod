package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Melzalgald_1;
import eatyourbeets.cards.animator.special.Melzalgald_2;
import eatyourbeets.cards.animator.special.Melzalgald_3;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Melzalgald extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Melzalgald.class)
            .SetAttack(3, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Melzalgald_1(), true);
                data.AddPreview(new Melzalgald_2(), true);
                data.AddPreview(new Melzalgald_3(), true);
            });

    public Melzalgald()
    {
        super(DATA);

        Initialize(18, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1, 0, 3);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return heal <= 0 ? null : HPAttribute.Instance.SetCard(this, false).SetText(new ColoredString(heal, Colors.Cream(1f)));
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = GameUtilities.GetHealthRecoverAmount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.RecoverHP(magicNumber);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);

        GameActions.Bottom.MakeCardInHand(new Melzalgald_1()).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInHand(new Melzalgald_2()).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInHand(new Melzalgald_3()).SetUpgrade(upgraded, false);
    }
}