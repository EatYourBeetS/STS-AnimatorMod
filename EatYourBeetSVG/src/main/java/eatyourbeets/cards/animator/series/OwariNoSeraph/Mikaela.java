package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mikaela extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mikaela.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Wound(), false));

    public Mikaela()
    {
        super(DATA);

        Initialize(6, 0, 2, 5);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Dark(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.GainForce(1, true);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ExhaustFromPile(name, 1, p.hand, p.discardPile)
            .SetOptions(false, false);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.TakeDamageAtEndOfTurn(secondaryValue);
        }
    }
}