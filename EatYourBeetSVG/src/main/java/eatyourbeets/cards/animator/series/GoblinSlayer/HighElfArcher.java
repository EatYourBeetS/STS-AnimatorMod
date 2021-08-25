package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class HighElfArcher extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HighElfArcher.class)
            .SetAttack(0, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    private final ArrayList<AbstractCard> unplayableCards = new ArrayList<>();

    public HighElfArcher()
    {
        super(DATA);

        Initialize(2, 0, 2, 1);
        SetUpgrade(1, 0);

        SetAffinity_Green(1, 1, 1);

        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Green, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SFX(SFX.ANIMATOR_ARROW);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowDagger(c.hb, 0.15f).SetColor(Color.TAN)).duration * 0.5f);

        GameActions.Bottom.Draw(1).AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameUtilities.SetUnplayableThisTurn(c);
            }
        });

        if (CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        }
        if (CheckAffinity(Affinity.Green))
        {
            GameActions.Bottom.ApplyLockOn(p, m, secondaryValue);
        }
    }
}