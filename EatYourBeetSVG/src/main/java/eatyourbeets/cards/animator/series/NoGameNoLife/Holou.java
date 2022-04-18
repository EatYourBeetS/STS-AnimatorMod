package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

public class Holou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Holou.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final int TEMP_HP = 6;

    public Holou()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, false).SetText(TEMP_HP, Colors.Cream(1));
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifySecondaryValue(this, Mathf.CeilToInt(CombatStats.Affinities.GetPowerAmount(Affinity.Light) / 2f), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.POWER_TIME_WARP, 0.5f, 0.5f);
        GameActions.Bottom.MoveCards(p.hand, p.drawPile);
        GameActions.Bottom.MoveCards(p.discardPile, p.drawPile)
        .ShowEffect(true, false, Math.max(0.0075f, 0.09f - p.drawPile.size() * 0.01f));

        GameActions.Bottom.Add(new ShuffleAction(p.drawPile, false));
        GameActions.Bottom.Draw(secondaryValue);
        GameActions.Bottom.GainTemporaryHP(TEMP_HP);
        GameActions.Bottom.Motivate(magicNumber);
    }
}