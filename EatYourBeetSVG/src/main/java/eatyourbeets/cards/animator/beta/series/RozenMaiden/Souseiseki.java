package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.WillpowerStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Souseiseki extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(Souseiseki.class)
    		.SetAttack(1, CardRarity.COMMON).SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Suiseiseki(), false);
    }

    public Souseiseki()
    {
        super(DATA);

        Initialize(5, 0, 0, 0);
        SetUpgrade(2, 0, 0, 0);
        SetAffinity_Orange(2, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Orange(1, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
        }

        GameActions.Bottom.ExhaustFromHand(name, 1, false)
                .SetOptions(false, false, false)
                .AddCallback(this::AfterExhaust);
    }

    public void AfterExhaust(ArrayList<AbstractCard> cards)
    {
        if (cards.size() > 0 && GameUtilities.IsCurseOrStatus(cards.get(0)))
            GameActions.Bottom.ChangeStance(WillpowerStance.STANCE_ID);
    }
}
