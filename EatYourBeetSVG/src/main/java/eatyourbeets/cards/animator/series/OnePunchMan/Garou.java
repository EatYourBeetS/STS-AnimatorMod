package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.*;

public class Garou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Garou.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Garou()
    {
        super(DATA);

        Initialize(0, 1, 6);
        SetUpgrade(0, 0, 2);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(player.drawPile.size() < 3);
        GameUtilities.ModifySecondaryValue(this, JUtils.Count(CombatStats.CardsExhaustedThisTurn(), GameUtilities::HasLightAffinity), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        if (p.drawPile.size() >= 3)
        {
            GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.TemporaryStrength, magicNumber);
            GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.TemporaryDexterity, magicNumber);
            GameActions.Bottom.MoveCards(p.drawPile, p.exhaustPile, 3)
            .ShowEffect(true, true)
            .SetOrigin(CardSelection.Top);
            GameActions.Last.Callback(() ->
            {
                final int light = JUtils.Count(CombatStats.CardsExhaustedThisTurn(), GameUtilities::HasLightAffinity);
                if (light > 0)
                {
                    GameActions.Bottom.GainMight(light);
                }
            });
        }
    }
}