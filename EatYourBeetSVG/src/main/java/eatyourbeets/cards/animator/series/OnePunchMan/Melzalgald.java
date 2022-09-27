package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Melzalgald_R;
import eatyourbeets.cards.animator.special.Melzalgald_B;
import eatyourbeets.cards.animator.special.Melzalgald_G;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Melzalgald extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Melzalgald.class)
            .SetAttack(3, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Melzalgald_R(), true);
                data.AddPreview(new Melzalgald_B(), true);
                data.AddPreview(new Melzalgald_G(), true);
            });

    public Melzalgald()
    {
        super(DATA);

        Initialize(16, 0, 4);
        SetUpgrade(0, 0, 0);

        SetAffinity_Star(1, 1, 3);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCardHeal(this);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = GameUtilities.GetHealthRecoverAmount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.RecoverHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        GameActions.Bottom.MakeCardInHand(new Melzalgald_R()).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInHand(new Melzalgald_B()).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInHand(new Melzalgald_G()).SetUpgrade(upgraded, false);
    }
}