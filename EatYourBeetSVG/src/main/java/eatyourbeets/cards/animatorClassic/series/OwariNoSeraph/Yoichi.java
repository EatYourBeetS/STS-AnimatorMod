package eatyourbeets.cards.animatorClassic.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Yoichi extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Yoichi.class).SetSeriesFromClassPackage().SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public Yoichi()
    {
        super(DATA);

        Initialize(0,0, 2);
        SetUpgrade(0,2, 0);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false);
        GameActions.Bottom.StackPower(new SupportDamagePower(p, 1))
        .AddCallback(power ->
        {
            if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
            {
                SupportDamagePower supportDamage = JUtils.SafeCast(power, SupportDamagePower.class);
                if (supportDamage != null)
                {
                    supportDamage.atEndOfTurn(true);
                }
            }
        });
    }
}