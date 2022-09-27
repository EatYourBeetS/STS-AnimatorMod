package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Holou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Holou.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Holou()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 4);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetPurge(true);
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
        GameActions.Bottom.SFX(SFX.POWER_TIME_WARP, 0.5f, 0.5f);
        GameActions.Bottom.MoveCards(p.hand, p.drawPile);
        GameActions.Bottom.MoveCards(p.discardPile, p.drawPile)
        .ShowEffect(true, false, Math.max(0.0075f, 0.09f - p.drawPile.size() * 0.01f));
        GameActions.Bottom.Add(new ShuffleAction(p.drawPile, false));

        final int amount = CombatStats.Affinities.GetPowerAmount(Affinity.Light) + CombatStats.Affinities.GetPowerAmount(Affinity.Dark);
        if (amount > 0)
        {
            GameActions.Bottom.Draw(amount)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.Motivate(c, 1);
                }
            });
        }
    }
}