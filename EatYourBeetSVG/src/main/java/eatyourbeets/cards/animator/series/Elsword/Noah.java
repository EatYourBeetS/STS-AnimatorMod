package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Noah extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noah.class)
            .SetMaxCopies(2)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Noah()
    {
        super(DATA);

        Initialize(17, 0, 1, 7);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1, 0, 0);
        SetAffinity_Dark(2, 0, 4);

        SetAffinityRequirement(AffinityType.Dark, 3);

        SetHaste(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            GameUtilities.GetIntent(m).AddFreezing();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.PURPLE, Color.LIGHT_GRAY, Color.VIOLET, Color.BLUE).duration * 0.6f);
        GameActions.Bottom.GainCorruption(1, true);
        GameActions.Bottom.ApplyFreezing(p, m, magicNumber);

        if (!CheckAffinity(AffinityType.Dark))
        {
            GameActions.Bottom.StackPower(new SelfDamagePower(p, secondaryValue));
        }
    }
}