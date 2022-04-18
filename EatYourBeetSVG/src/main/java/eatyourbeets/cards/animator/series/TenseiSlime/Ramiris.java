package eatyourbeets.cards.animator.series.TenseiSlime;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.SFX;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.*;

public class Ramiris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ramiris.class)
            .SetSkill(X_COST, CardRarity.RARE, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Ramiris()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Light(2);
        SetAffinity_Blue(2);
        SetAffinity_Green(1);

        SetExhaust(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (CheckSpecialCondition(false))
        {
            for (EnemyIntent intent : GameUtilities.GetIntents())
            {
                intent.AddWeak();
                intent.AddStrength(-secondaryValue);
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final int charge = GameUtilities.UseXCostEnergy(this) * 2;
        if (charge > 0)
        {
            GameActions.Bottom.SelectFromPile(name, charge, p.discardPile)
            .SetOptions(true, true)
            .SetFilter(c -> !c.hasTag(HASTE))
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    GameActions.Bottom.SFX(SFX.TINGSHA, 1.2f, 1.4f, 0.9f);

                    for (AbstractCard c : cards)
                    {
                        GameUtilities.SetCardTag(c, HASTE, true);
                        GameEffects.Queue.ShowCopy(c,
                                Settings.WIDTH * 0.8f + (MathUtils.random(-0.25f, 0.25f) * AbstractCard.IMG_WIDTH),
                                Settings.HEIGHT * 0.75f + (MathUtils.random(-0.33f, 0.33f) * AbstractCard.IMG_HEIGHT));
                    }
                }
            });
        }

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Weak, magicNumber);
            GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Shackles, secondaryValue);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn)
        {
            if (c.type == CardType.ATTACK)
            {
                return false;
            }
        }

        return true;
    }
}